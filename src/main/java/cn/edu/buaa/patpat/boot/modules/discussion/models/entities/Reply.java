package cn.edu.buaa.patpat.boot.modules.discussion.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Reply extends HasCreatedAndUpdated {
    private int id;
    private int discussionId;
    private int parentId;
    private int authorId;

    private String content;

    private boolean verified;
}
