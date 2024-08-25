package cn.edu.buaa.patpat.boot.modules.message.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Message extends HasCreated {
    private int id;
    private int type;

    private int courseId;
    private int accountId;

    private String content;
    private String argument;

    private boolean read;
}