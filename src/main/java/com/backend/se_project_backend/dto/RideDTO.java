package com.backend.se_project_backend.dto;

import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Station;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//TODO: Replace with @Data and @Builder
public class RideDTO {

    @NotBlank
    private String startStationName;

    private String endStationName;

    @NotNull
    private long bikeExternalId;
}
