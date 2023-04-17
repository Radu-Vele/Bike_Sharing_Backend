package com.backend.se_project_backend.service;

import com.backend.se_project_backend.dto.*;
import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Station;
import com.backend.se_project_backend.utils.exceptions.DocumentNotFoundException;
import com.backend.se_project_backend.utils.exceptions.UniqueDBFieldException;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public interface StationService {

    void addBike(String stationName, long bikeExternalId) throws Exception;

    void removeBike(String stationName, long bikeExternalId) throws Exception;

    void create(StationDTO stationDTO) throws UniqueDBFieldException;

    void delete(String stationName) throws DocumentNotFoundException;

    ArrayList<StationGetDTO> getStations();

    long getFreeSlotsByStationName(String stationName) throws DocumentNotFoundException;

    ArrayList<BikeGetDTO> getUsableBikesByStationName(String stationName) throws DocumentNotFoundException;

    ArrayList<StationGetDTO> getUsableStartStations() throws Exception;

    ArrayList<StationGetDTO> getFreeEndStations() throws Exception;

    Station getStationByName(String name) throws Exception;

    void editStation(Station station);

    void editStationNameCapacity(StationEditDTO stationEditDTO) throws Exception;

    void importStationsBikesInit() throws Exception;

    void reflectNewRatingInStation(String username, long editedBikeExternalId) throws Exception;

    void createWithOptions(StationOptionsDTO stationDTO) throws Exception;
}
