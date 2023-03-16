package com.backend.se_project_backend.dto;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StationDTO {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @Min(0)
    private long maximumCapacity;

    @NotBlank
    private String name;
}
