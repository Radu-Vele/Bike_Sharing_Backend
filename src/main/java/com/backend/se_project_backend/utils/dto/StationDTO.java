package com.backend.se_project_backend.utils.dto;

import com.backend.se_project_backend.model.Bike;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StationDTO {

    private Double xCoordinate;

    private Double yCoordinate;

    private long maximumCapacity; //TBD

    private String name;

    private List<Bike> bikeList = new ArrayList<>();
}
