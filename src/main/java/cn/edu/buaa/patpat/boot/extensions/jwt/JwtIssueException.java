/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.extensions.jwt;

public class JwtIssueException extends Exception {
    public JwtIssueException(String message) {
        super(message);
    }

    public JwtIssueException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtIssueException(Throwable cause) {
        super(cause);
    }
}
