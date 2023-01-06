package com.backend.se_project_backend.utils.dto;

public class BikeDTO {

    private boolean available;
    private boolean usable;
    private Double rating;
    //private Station station; //home station


    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
