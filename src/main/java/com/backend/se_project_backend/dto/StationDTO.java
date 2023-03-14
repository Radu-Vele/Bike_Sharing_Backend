package com.backend.se_project_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StationDTO {

    private Double xCoordinate;

    private Double yCoordinate;

    private long maximumCapacity;

    private String name;
}
