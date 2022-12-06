package com.backend.se_project_backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private int bikeId;
    private int startStationId;
    private int endStationId;
    private Date startTime; //TODO: is it the good type?
    private Date endTime;

}
