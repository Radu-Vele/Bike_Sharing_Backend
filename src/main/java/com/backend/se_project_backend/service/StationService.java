package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Station;
import com.backend.se_project_backend.utils.dto.BikeDTO;

public interface StationService {

    boolean addBike(long stationId, long bikeId);

    boolean removeBike(long stationId, long bikeId);

    void create(Station station);

    void delete(long stationId);

    String getStationNameById(long stationId);

}
