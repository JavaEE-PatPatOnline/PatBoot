package cn.edu.buaa.patpat.boot.modules.problem.controllers;

import cn.edu.buaa.patpat.boot.aspect.ValidateMultipartFile;
import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.dto.MessageResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission;
import cn.edu.buaa.patpat.boot.modules.problem.dto.CreateProblemRequest;
import cn.edu.buaa.patpat.boot.modules.problem.dto.CreateProblemResponse;
import cn.edu.buaa.patpat.boot.modules.problem.dto.UpdateProblemRequest;
import cn.edu.buaa.patpat.boot.modules.problem.dto.UpdateProblemResponse;
import cn.edu.buaa.patpat.boot.modules.problem.models.entities.Problem;
import cn.edu.buaa.patpat.boot.modules.problem.services.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@RestController
@RequestMapping("api/admin/problem")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Problem Admin", description = "Admin problem management API")
public class ProblemAdminController extends BaseController {
    private final ProblemService problemService;

    @PostMapping("create")
    @Operation(summary = "Create a new problem", description = "T.A. creates a new problem")
    @ValidateMultipartFile(extensions = { "zip" })
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<CreateProblemResponse> create(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam boolean hidden,
            @RequestParam MultipartFile file,
            HttpServletRequest servletRequest
    ) {
        var request = new CreateProblemRequest(title, description, hidden, file);
        request.validate();

        Problem problem = problemService.createProblem(request);
        var response = CreateProblemResponse.of(problem, mappers);

        return DataResponse.ok(M("problem.create.success"), response);
    }

    @PutMapping("update/{id}")
    @Operation(summary = "Update problem info", description = "T.A. updates problem info")
    @ValidateMultipartFile(allowNull = true, extensions = { "zip" })
    @ValidatePermission(AuthLevel.TA)
    public DataResponse<UpdateProblemResponse> update(
            @PathVariable int id,
            @RequestParam @Nullable String title,
            @RequestParam @Nullable String description,
            @RequestParam @Nullable Boolean hidden,
            @RequestParam @Nullable MultipartFile file,
            HttpServletRequest servletRequest
    ) {
        var request = new UpdateProblemRequest(title, description, hidden, file);
        Problem problem = problemService.updateProblem(id, request);
        var response = UpdateProblemResponse.of(problem, mappers);
        return DataResponse.ok(M("problem.update.success"), response);
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "Delete a problem", description = "T.A. deletes a problem")
    @ValidatePermission(AuthLevel.TA)
    public MessageResponse delete(
            @PathVariable int id,
            HttpServletRequest servletRequest
    ) {
        problemService.deleteProblem(id);
        return MessageResponse.ok(M("problem.delete.success"));
    }
}