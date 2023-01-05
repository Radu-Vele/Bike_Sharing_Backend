package com.backend.se_project_backend.model;

import lombok.AllArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "bike")
public class Bike extends BaseEntity {

    private boolean available;
    private boolean usable;
    private Double rating;
    //private Station station; //home station

    public Bike() {

    }

    @Column
    public boolean isAvailable() { return this.available; }

    public void setAvailable(boolean available) { this.available = available; }

    @Column
    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    @Column
    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

//    @OneToOne
//    public Station getStation() {
//        return station;
//    }
//
//    public void setStation(Station station) {
//        this.station = station;
//    }
}
