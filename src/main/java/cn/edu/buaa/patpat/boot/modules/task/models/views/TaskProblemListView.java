package cn.edu.buaa.patpat.boot.modules.task.models.views;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskProblemListView implements Serializable {
    private int problemId;
    private String title;
}
