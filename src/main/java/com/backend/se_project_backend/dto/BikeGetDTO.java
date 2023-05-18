package com.backend.se_project_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BikeGetDTO extends BikeDTO {
    private long externalId;

    private boolean available; //refers to bike availability (currently in use)

    private boolean usable; //refers to bike condition (broken)

    private Double rating;
}
