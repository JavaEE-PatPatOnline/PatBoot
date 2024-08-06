package cn.edu.buaa.patpat.boot.modules.task.models.views;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import cn.edu.buaa.patpat.boot.config.Globals;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaskView extends HasCreatedAndUpdated {
    private int id;

    private String title;
    private String content;
    private boolean visible;

    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime startTime;
    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime deadlineTime;
    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime endTime;
}