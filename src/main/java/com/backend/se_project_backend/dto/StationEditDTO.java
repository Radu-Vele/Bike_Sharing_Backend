package com.backend.se_project_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationEditDTO {
    @NotBlank
    private String currName;

    private String newName;

    private int newCapacity;
}
