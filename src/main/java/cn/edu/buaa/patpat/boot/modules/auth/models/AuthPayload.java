/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.auth.models;

import lombok.Data;

@Data
public class AuthPayload {
    private int id;
    private String buaaId;
    private String name;
    private boolean ta;
    private boolean teacher;

    public boolean isStudent() {
        return !ta;
    }
}
