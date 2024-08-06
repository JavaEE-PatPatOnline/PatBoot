package cn.edu.buaa.patpat.boot.modules.task.controllers;

import cn.edu.buaa.patpat.boot.common.dto.DataResponse;
import cn.edu.buaa.patpat.boot.common.requets.BaseController;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskTypes;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskListView;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskView;
import cn.edu.buaa.patpat.boot.modules.task.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/task")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Task", description = "Task API")
public class TaskController extends BaseController {
    private final TaskService taskService;

    @GetMapping("{type}/query")
    @Operation(summary = "Get all visible tasks", description = "Get all visible tasks (lab or iter)")
    @ValidateCourse
    public DataResponse<List<TaskListView>> query(
            @PathVariable String type,
            @CourseId Integer courseId
    ) {
        var tasks = taskService.query(courseId, TaskTypes.fromString(type), true);
        return DataResponse.ok(tasks);
    }

    @GetMapping("{type}/query/{id}")
    @Operation(summary = "Get task by id", description = "Get task by id, return forbidden if not started or ended")
    @ValidateCourse
    public DataResponse<TaskView> queryById(
            @PathVariable String type,
            @PathVariable Integer id,
            @CourseId Integer courseId
    ) {
        var task = taskService.query(id, courseId, TaskTypes.fromString(type), true);
        return DataResponse.ok(task);
    }
}