package com.backend.se_project_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
public class BikeDTO {

    @NotNull
    private boolean available;

    @NotNull
    private boolean usable;

    @NotNull
    private Double rating;
}
