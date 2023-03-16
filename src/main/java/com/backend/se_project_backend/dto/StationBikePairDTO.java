package com.backend.se_project_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class StationBikePairDTO {

    @NotNull
    private long bikeExternalId;

    @NotBlank
    private String stationName;
}
