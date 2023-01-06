package com.backend.se_project_backend.model;

import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "station")
public class Station extends BaseEntity {

    private Double xCoordinate;
    private Double yCoordinate;
    //private int maximumCapacity; //TBD
    private String name;
    private List<Bike> bikeList = new ArrayList<>();

    public Station() {

    }

    @Column(nullable = false)
    public Double getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(Double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    @Column(nullable = false)
    public Double getYCoordinate() { return yCoordinate; }

    public void setYCoordinate(Double yCoordinate) { this.yCoordinate = yCoordinate; }

    @Column(nullable = false, unique = true)
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @OneToMany(fetch = FetchType.LAZY)
    public List<Bike> getBikeList() {
        return bikeList;
    }

    public void setBikeList(List<Bike> bikeList) { this.bikeList = bikeList; }
}
