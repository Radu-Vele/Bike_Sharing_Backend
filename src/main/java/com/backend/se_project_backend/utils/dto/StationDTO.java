package com.backend.se_project_backend.utils.dto;

import com.backend.se_project_backend.model.Bike;

import java.util.ArrayList;
import java.util.List;

public class StationDTO {

    private int xCoordinate;
    private int yCoordinate;
    //private int maximumCapacity; //TBD
    private String name;
    private List<Bike> bikeList = new ArrayList<>();

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

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
