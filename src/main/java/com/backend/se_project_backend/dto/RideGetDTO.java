package com.backend.se_project_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideGetDTO {
    @NotBlank
    private String startStationName;

    @NotBlank
    private String endStationName;

    @NotNull
    private long bikeExternalId;

    @NotNull
    private String startTime; //TODO: is it ok to return them as string?

    @NotNull
    private String endTime;
}
