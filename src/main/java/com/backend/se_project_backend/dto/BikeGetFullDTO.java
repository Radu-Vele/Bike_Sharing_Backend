package com.backend.se_project_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BikeGetFullDTO {
    private long externalId;

    private boolean available; //refers to bike availability (currently in use)

    private boolean usable; //refers to bike condition (broken)

    private Double rating;
}
