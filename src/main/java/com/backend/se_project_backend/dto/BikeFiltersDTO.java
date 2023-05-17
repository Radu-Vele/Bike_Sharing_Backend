package com.backend.se_project_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BikeFiltersDTO {
    private String hostStation;

    private String externalId;

    private boolean onlyUsable;

    private boolean onlyNotUsable;
}