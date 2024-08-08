package cn.edu.buaa.patpat.boot.modules.task.services;

import cn.edu.buaa.patpat.boot.config.Globals;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.modules.account.models.mappers.AccountFilterMapper;
import cn.edu.buaa.patpat.boot.modules.account.models.views.TeacherView;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.Task;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskTypes;
import cn.edu.buaa.patpat.boot.modules.task.models.mappers.TaskScoreMapper;
import cn.edu.buaa.patpat.boot.modules.task.models.mappers.TaskStatisticMapper;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskScoreView;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskStudentView;
import cn.edu.buaa.patpat.boot.modules.task.services.impl.DownloadAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskAdminService extends TaskSubmissionService {
    private final TaskScoreMapper taskScoreMapper;
    private final TaskStatisticMapper taskStatisticMapper;
    private final AccountFilterMapper accountFilterMapper;
    private final DownloadAgent downloadAgent;

    public int batchGrade(int taskId, int score, List<Integer> studentIds) {
        if (studentIds.isEmpty()) {
            return 0;
        } else if (studentIds.size() == 1) {
            return grade(taskId, score, studentIds.get(0));
        }
        return taskScoreMapper.batchUpdateScore(taskId, score, studentIds);
    }

    public int grade(int taskId, int score, int studentId) {
        return taskScoreMapper.updateScore(taskId, score, studentId);
    }

    /**
     * Download all tasks submissions of a teacher in a task.
     * WARNING: This method has potential synchronization issues.
     */
    public synchronized Resource downloadAll(int taskId, int type, int courseId, int teacherId) {
        List<TaskStudentView> students = taskStatisticMapper.getStudentsByTeacher(courseId, teacherId);
        List<TaskScoreView> scores = taskStatisticMapper.getScores(taskId);
        String submissionPath = getSubmissionRootPath(taskId, TaskTypes.toTag(type));
        String archivePath = bucketApi.recordToPrivatePath(bucketApi.toRecord(
                Globals.TEMP_TAG,
                "task-download",
                String.format("%d-%d", taskId, teacherId)));
        String archiveName = getArchiveName(taskId, type, teacherId);
        try {
            return downloadAgent.download(students, scores, submissionPath, archivePath, archiveName);
        } catch (IOException e) {
            log.error("Failed to download task submissions {}", e.getMessage());
            throw new InternalServerErrorException(M("system.error.io"));
        }
    }

    private String getArchiveName(int taskId, int type, int teacherId) {
        Task task = taskMapper.findTitle(taskId);
        String index = task.getTitle().replaceAll("[^0-9]", "").strip();

        TeacherView teacher = accountFilterMapper.queryTeacher(teacherId);
        String name = teacher == null ? "Unknown" : teacher.getName();

        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern(Globals.FILE_DATE_FORMAT));

        return String.format("%s-%s-%s-%s.zip", TaskTypes.toString(type), index, name, date);
    }
}