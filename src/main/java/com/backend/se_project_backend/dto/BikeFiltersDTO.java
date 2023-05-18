package com.backend.se_project_backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BikeFiltersDTO {
    private String hostStation;

    private String externalId;

    private boolean onlyUsable; // TODO: why isn't @Data enough

    private boolean onlyNotUsable;

    public boolean getOnlyUsable() {
        return onlyUsable;
    }

    public boolean getOnlyNotUsable() {
        return onlyNotUsable;
    }
}