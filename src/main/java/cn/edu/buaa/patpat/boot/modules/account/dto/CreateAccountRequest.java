/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.account.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Register is only enabled in development environment.
 */
@Data
public class CreateAccountRequest {
    @NotNull
    @Size(min = 1, max = 8)
    private String buaaId;

    @NotNull
    @Size(min = 1, max = 31)
    private String name;

    @NotNull
    @Min(0)
    @Max(2)
    private Integer gender;

    @NotNull
    @Size(min = 1, max = 31)
    private String school;

    @NotNull
    private Boolean teacher;

    @NotNull
    private Boolean ta;
}
