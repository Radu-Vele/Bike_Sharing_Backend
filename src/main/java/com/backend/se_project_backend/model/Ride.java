package com.backend.se_project_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ride {

    @Id
    private String id;

    private long bikeExternalId;

    private String startStationName;

    private String endStationName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private boolean completed = false;

    private String recommendation;

    public void startTime() {
        this.startTime = LocalDateTime.now();
    }

    public void endTime() {
        this.endTime = LocalDateTime.now();
    }
}


