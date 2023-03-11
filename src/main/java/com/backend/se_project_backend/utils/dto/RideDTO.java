package com.backend.se_project_backend.utils.dto;

import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Station;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class RideDTO {

    private String username;

    private String startStationId;

    private String endStationId;

    private String bikeId;
}
