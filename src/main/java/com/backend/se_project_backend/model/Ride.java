package com.backend.se_project_backend.model;
import javax.persistence.*;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@Entity
@Table(name = "rides")
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    //private Bike bike;
    private int startStationId;
    private int endStationId;
    private Date startTime; //TODO: is it the good type?
    private Date endTime;

}
