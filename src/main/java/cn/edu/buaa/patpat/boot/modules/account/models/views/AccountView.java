package cn.edu.buaa.patpat.boot.modules.account.models.views;

import cn.edu.buaa.patpat.boot.common.models.HasCreated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountView extends HasCreated {
    private int id;
    private String buaaId;

    private String name;

    private boolean teacher;
    private boolean ta;
}