package com.backend.se_project_backend.service;

import com.backend.se_project_backend.dto.StationDTO;
import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Station;
import com.backend.se_project_backend.utils.exceptions.DocumentNotFoundException;
import com.backend.se_project_backend.utils.exceptions.UniqueDBFieldException;

import java.util.ArrayList;

public interface StationService {

    void addBike(String stationName, long bikeExternalId) throws Exception;

    void removeBike(String stationName, long bikeExternalId) throws Exception;

    void create(StationDTO stationDTO) throws UniqueDBFieldException;

    void delete(String stationName) throws DocumentNotFoundException;

    String getStationNameById(String stationId);

    ArrayList<Station> getStations();

    long getFreeSlotsByStationId(String stationId);

    ArrayList<Bike> getUsableBikesByStationId(String stationId);

    ArrayList<Station> getUsableStartStations();

    ArrayList<Station> getFreeEndStations();

}
