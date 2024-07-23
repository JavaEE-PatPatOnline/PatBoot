package cn.edu.buaa.patpat.boot.modules.problem.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.config.Globals;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.problem.dto.CreateProblemRequest;
import cn.edu.buaa.patpat.boot.modules.problem.dto.UpdateProblemRequest;
import cn.edu.buaa.patpat.boot.modules.problem.exceptions.ProblemInitializeException;
import cn.edu.buaa.patpat.boot.modules.problem.models.entities.Problem;
import cn.edu.buaa.patpat.boot.modules.problem.models.mappers.ProblemMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProblemService extends BaseService {
    private final BucketApi bucketApi;
    private final ProblemInitializer problemInitializer;
    private final ProblemMapper problemMapper;

    public Problem createProblem(CreateProblemRequest request) {
        // validate problem configuration
        ProblemInitializer.InitializeResult result;
        try {
            result = problemInitializer.initialize(request.getFile());
        } catch (ProblemInitializeException e) {
            throw new BadRequestException(M("problem.init.error", e.getMessage()));
        }

        // save the problem to the database
        Problem problem = mappers.map(request, Problem.class);
        try {
            problem.setData(mappers.toJson(result.descriptor()));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize problem descriptor", e);
            throw new InternalServerErrorException(M("system.error.internal"));
        }
        problemMapper.save(problem);

        // finalize the problem by moving the files to the correct location
        try {
            problemInitializer.finalize(result, problem.getId());
        } catch (ProblemInitializeException e) {
            throw new InternalServerErrorException(M("problem.init.error", e.getMessage()));
        }

        return problem;
    }

    public Problem updateProblem(int id, UpdateProblemRequest request) {
        Problem problem = problemMapper.findById(id);
        if (problem == null) {
            throw new BadRequestException(M("problem.exists.not"));
        }
        mappers.map(request, problem);

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            try {
                var result = problemInitializer.initialize(request.getFile());
                problemInitializer.finalize(result, problem.getId());
                problem.setData(mappers.toJson(result.descriptor()));
            } catch (ProblemInitializeException e) {
                throw new BadRequestException(M("problem.init.error", e.getMessage()));
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize problem descriptor", e);
                throw new InternalServerErrorException(M("system.error.internal"));
            }
        }
        problem.update();

        problemMapper.update(problem);
        return problem;
    }

    public void deleteProblem(int id) {
        if (problemMapper.deleteById(id) == 0) {
            throw new NotFoundException(M("problem.exists.not"));
        }

        // delete the problem files
        String record = bucketApi.toRecord(Globals.PROBLEM_TAG, String.valueOf(id));
        String path = bucketApi.recordToPrivatePath(record);
        try {
            Medias.remove(path);
        } catch (IOException e) {
            log.error("Failed to purge problem files", e);
        }
    }
}