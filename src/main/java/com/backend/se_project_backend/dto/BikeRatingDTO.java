package com.backend.se_project_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BikeRatingDTO {

    private String bikeId;

    private Double currentRating;
}
