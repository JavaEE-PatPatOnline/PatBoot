package cn.edu.buaa.patpat.boot.modules.judge.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SubmissionDto {
    private String buaaId;
    private int taskId;
    private String result;
}
