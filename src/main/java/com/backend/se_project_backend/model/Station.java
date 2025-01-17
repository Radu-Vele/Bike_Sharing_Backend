package com.backend.se_project_backend.model;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
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

    private Double latitude;

    private Double longitude;

    private long maximumCapacity;

    @Indexed(unique = true)
    private String name;

    private List<Bike> bikeList = new ArrayList<>(); //don't use @DBRef as there's a limited number of bikes in a station
}