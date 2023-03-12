package com.backend.se_project_backend.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BikeDTO {

    private boolean available;

    private boolean usable;

    private Double rating;
}
