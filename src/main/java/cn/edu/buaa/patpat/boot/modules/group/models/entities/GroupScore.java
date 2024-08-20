package cn.edu.buaa.patpat.boot.modules.group.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupScore extends HasCreatedAndUpdated {
    private int groupId;
    private int courseId;
    private int score;

    private String record;
}