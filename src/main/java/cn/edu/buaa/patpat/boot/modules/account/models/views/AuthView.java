package cn.edu.buaa.patpat.boot.modules.account.models.views;

import lombok.Data;

/**
 * Used for authentication.
 */
@Data
public class AuthView {
    private int id;
    private String buaaId;
    private String name;
    private boolean isTeacher;
    private boolean isTa;
    private String password;
}