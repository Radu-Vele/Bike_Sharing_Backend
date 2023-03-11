package com.backend.se_project_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "stations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Station {
    @Id
    private String id;

    private Double xCoordinate;

    private Double yCoordinate;

    private long maximumCapacity;

    private String name;

    private List<Bike> bikeList = new ArrayList<>();
}
