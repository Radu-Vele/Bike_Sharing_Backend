package com.backend.se_project_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@NoArgsConstructor
@Data
public class DatesIntervalDTO {
    @Min(1990)
    @Max(2030)
    private int startYear;

    @Min(1)
    @Max(12)
    private int startMonth;

    @Min(1)
    @Max(31)
    private int startDay;

    @Min(1990)
    @Max(2030)
    private int endYear;

    @Min(1)
    @Max(12)
    private int endMonth;

    @Min(1)
    @Max(31)
    private int endDay;
}
