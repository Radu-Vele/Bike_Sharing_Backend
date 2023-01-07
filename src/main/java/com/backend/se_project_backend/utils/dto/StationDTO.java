package com.backend.se_project_backend.utils.dto;

import com.backend.se_project_backend.model.Bike;

import java.util.ArrayList;
import java.util.List;

public class StationDTO {

    private Double xCoordinate;
    private Double yCoordinate;
    private long maximumCapacity; //TBD
    private String name;
    private List<Bike> bikeList = new ArrayList<>();

    public Double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(Double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(Double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public long getMaximumCapacity() { return maximumCapacity; }

    public void setMaximumCapacity(long maximumCapacity) { this.maximumCapacity = maximumCapacity; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Bike> getBikeList() {
        return bikeList;
    }

    public void setBikeList(List<Bike> bikeList) {
        this.bikeList = bikeList;
    }
}
