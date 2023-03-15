package com.backend.se_project_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BikeRatingDTO {

    @Min(1)
    private long externalId;

    @NotNull
    @Min(0)
    @Max(10)
    private Double givenRating;
}
