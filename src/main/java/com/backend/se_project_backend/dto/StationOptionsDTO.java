package com.backend.se_project_backend.dto;

import com.backend.se_project_backend.model.Station;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StationOptionsDTO extends StationDTO {
    private boolean saveToCSV;
    private boolean fillHalf;
}
