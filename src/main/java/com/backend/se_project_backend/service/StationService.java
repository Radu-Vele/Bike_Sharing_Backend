package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Station;

import java.util.ArrayList;

public interface StationService {

    boolean addBike(String stationId, String bikeId);

    boolean removeBike(String stationId, String bikeId);

    void create(Station station);

    void delete(String stationId);

    String getStationNameById(String stationId);

    ArrayList<Station> getStations();

    long getFreeSlotsByStationId(String stationId);

    ArrayList<Bike> getUsableBikesByStationId(String stationId);

    ArrayList<Station> getUsableStartStations();

    ArrayList<Station> getFreeEndStations();

}
