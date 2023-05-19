package com.backend.se_project_backend.service;

import com.backend.se_project_backend.dto.*;
import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Station;
import com.backend.se_project_backend.repository.BikeRepository;
import com.backend.se_project_backend.repository.StationRepository;
import com.backend.se_project_backend.utils.StringConstants;
import com.backend.se_project_backend.utils.exceptions.DocumentNotFoundException;
import com.backend.se_project_backend.utils.exceptions.IllegalOperationException;
import com.backend.se_project_backend.utils.exceptions.UniqueDBFieldException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;

    private final BikeRepository bikeRepository;

    private final BikeService bikeService;

    private final ModelMapper modelMapper;

    private final UserService userService;

    @Value("${csv.stations}")
    String stations_csv_filename;

    private void validateStationBikePairWithDB(Optional<Station> stationByName, Optional<Bike> bikeByExternalId ) throws Exception{

        if(stationByName.isEmpty()) {
            throw new DocumentNotFoundException("There is no station with this name in the database.");
        }
        if(bikeByExternalId.isEmpty()) {
            throw new DocumentNotFoundException("There is no bike with this external Id in the database.");
        }
    }

    @Override
    public void addBike(String stationName, long bikeExternalId) throws Exception {
        Optional<Station> stationByName = this.stationRepository.findByName(stationName);
        Optional<Bike> bikeByExternalId = this.bikeRepository.findByExternalId(bikeExternalId);

        validateStationBikePairWithDB(stationByName, bikeByExternalId);

        if(getFreeSlotsByStationName(stationByName.get().getName()) == 0) {
            throw new IllegalOperationException("Cannot add a bike to a full station");
        }
        if (bikeByExternalId.get().isAvailable()) {
            throw new IllegalOperationException("Cannot add an available bike to a station. Must first be removed from its host station.");
        }

        bikeByExternalId.get().setAvailable(true);
        stationByName.get().getBikeList().add(bikeByExternalId.get());
        stationRepository.save(stationByName.get());
        bikeRepository.save(bikeByExternalId.get());
    }

    @Override
    public void removeBike(String stationName, long bikeExternalId) throws Exception {
        Optional<Station> stationByName = this.stationRepository.findByName(stationName);
        Optional<Bike> bikeByExternalId = this.bikeRepository.findByExternalId(bikeExternalId);

        validateStationBikePairWithDB( stationByName, bikeByExternalId);

        if (!bikeByExternalId.get().isAvailable()) {
            throw new IllegalOperationException("The bike with the given id is currently in use");
        }

        if(!stationByName.get().getBikeList().remove(bikeByExternalId.get())) {
            throw new IllegalOperationException("The bike is not present in the station");
        }

        bikeByExternalId.get().setAvailable(false);

        stationRepository.save(stationByName.get());
        bikeRepository.save(bikeByExternalId.get());
    }

    @Override
    public void create(StationDTO stationDTO) throws UniqueDBFieldException {
        Station station = this.modelMapper.map(stationDTO, Station.class);

        if(this.stationRepository.findByName(stationDTO.getName()).isPresent()) {
            throw new UniqueDBFieldException("There already exists a station having that name");
        }
        if(this.stationRepository.findByCoordinates(stationDTO.getLatitude(), stationDTO.getLongitude()).isPresent()) {
            //this uniqueness is not enforced on the collection
            throw new UniqueDBFieldException("There already exists a station having these coordinates");
        }

        this.stationRepository.save(station);
    }

    @Override
    public void delete(String stationName) throws Exception {
        Optional<Station> station = this.stationRepository.findByName(stationName);
        if(station.isPresent()) {
            //remove all bikes from the station
            for(Bike bike : station.get().getBikeList()) {
                removeBike(station.get().getName(), bike.getExternalId());
            }

            this.stationRepository.deleteById(station.get().getId());
        }
        else {
            throw new DocumentNotFoundException("Cannot perform deletion. The station with the given name is not in the database.");
        }
    }

    private ArrayList<StationGetDTO> mapStationsToDTO(List<Station> stations) {
        ArrayList<StationGetDTO> stationDTOs = new ArrayList<>();

        for (Station station: stations) {
            StationGetDTO stationGetDTO = new StationGetDTO();
            this.modelMapper.map(station, stationGetDTO);

            ArrayList<BikeGetDTO> bikeGetDTOs = new ArrayList<>();
            for (Bike bike : station.getBikeList()) {
                BikeGetDTO bikeGetDTO = this.modelMapper.map(bike, BikeGetDTO.class);
                bikeGetDTOs.add(bikeGetDTO);
            }
            stationGetDTO.setBikeList(bikeGetDTOs);

            stationDTOs.add(stationGetDTO);
        }

        return stationDTOs;
    }

    @Override
    public ArrayList<StationGetDTO> getStations() {
        List<Station> allStations = this.stationRepository.findAll();
        return mapStationsToDTO(allStations);
    }

    @Override
    public long getFreeSlotsByStationName(String stationName) throws DocumentNotFoundException {
        Optional<Station> stationByName = this.stationRepository.findByName(stationName);
        if (stationByName.isPresent()) {
            List<Bike> listOfBikes = stationByName.get().getBikeList();
            return stationByName.get().getMaximumCapacity() - listOfBikes.size();
        }
        else {
            throw new DocumentNotFoundException("There is no station having the given name in the database.");
        }
    }

    public List<Bike> getBikesByStationName(String stationName) throws DocumentNotFoundException {
        Optional<Station> stationByName = this.stationRepository.findByName(stationName);
        if (stationByName.isEmpty()) {
            throw new DocumentNotFoundException("There is no station having the given name in the database.");
        }
       return stationByName.get().getBikeList();
    }

    @Override
    public ArrayList<BikeGetDTO> getUsableBikesByStationName(String stationName) throws DocumentNotFoundException {
        List<Bike> bikesFromStation = getBikesByStationName(stationName);
        ArrayList<BikeGetDTO> bikeGetDTOs = new ArrayList<>();
        for (Bike bike:
             bikesFromStation) {
            if(bike.isUsable()) {
                bikeGetDTOs.add(this.modelMapper.map(bike, BikeGetDTO.class));
            }
        }
        return bikeGetDTOs;
    }

    @Override
    public ArrayList<BikeGetDTO> getNonUsableBikesByStationName(String stationName) throws DocumentNotFoundException {
        List<Bike> bikesFromStation = getBikesByStationName(stationName);
        ArrayList<BikeGetDTO> bikeGetDTOs = new ArrayList<>();
        for (Bike bike:
                bikesFromStation) {
            if(!bike.isUsable()) {
                bikeGetDTOs.add(this.modelMapper.map(bike, BikeGetDTO.class));
            }
        }
        return bikeGetDTOs;
    }

    private List<BikeGetDTO> getAllBikesByStationName(String stationName) throws DocumentNotFoundException {
        List<Bike> bikesFromStation = getBikesByStationName(stationName);
        ArrayList<BikeGetDTO> bikeGetDTOs = new ArrayList<>();
        for (Bike bike:bikesFromStation) {
                bikeGetDTOs.add(this.modelMapper.map(bike, BikeGetDTO.class));
        }
        return bikeGetDTOs;
    }

    @Override
    public ArrayList<StationGetDTO> getUsableStartStations() throws DocumentNotFoundException {
        ArrayList<Station> listOfStations = (ArrayList<Station>) this.stationRepository.findAll();
        ArrayList<Station> usableStartStations = new ArrayList<>();
        for (Station station :
             listOfStations) {
            if(!getUsableBikesByStationName(station.getName()).isEmpty()) {
               usableStartStations.add(station);
            }
        }

        return mapStationsToDTO(usableStartStations);

    }

    @Override
    public ArrayList<StationGetDTO> getFreeEndStations() throws Exception{
        ArrayList<Station> listOfStations = (ArrayList<Station>) this.stationRepository.findAll();
        ArrayList<Station> usableEndStations = new ArrayList<>();
        for (Station station : listOfStations) {
            if(getFreeSlotsByStationName(station.getName()) != 0) {
                usableEndStations.add(station);
            }
        }

        //TODO: Check if I need to receive the selected station for the beginning of the ride and remove it from the returned list

        return mapStationsToDTO(usableEndStations);
    }

    @Override
    public Station getStationByName(String name) throws Exception {
        Optional<Station> foundStation = this.stationRepository.findByName(name);
        if(foundStation.isEmpty()) {
            throw new DocumentNotFoundException("Cannot find a station with the given name");
        }
        return foundStation.get();
    }

    @Override
    public void editStation(Station station) {
        this.stationRepository.save(station);
    }

    @Override
    public void editStationNameCapacity(StationEditDTO stationEditDTO) throws Exception {
        Optional<Station> currStation = this.stationRepository.findByName(stationEditDTO.getCurrName());

        if(currStation.isEmpty()) {
            throw new DocumentNotFoundException(StringConstants.STATION_NOT_FOUND);
        }

        if(stationEditDTO.getNewCapacity() != 0) {
            int currNrOfBikes = currStation.get().getBikeList().size();
            if(currNrOfBikes > stationEditDTO.getNewCapacity()) {
                while(currNrOfBikes > stationEditDTO.getNewCapacity()) { //remove excessive bikes
                    Bike toRemove = currStation.get().getBikeList().get(0);
                    // remove in db
                    this.removeBike(currStation.get().getName(), toRemove.getExternalId());
                    // remove in the object used in the function
                    currStation.get().getBikeList().remove(toRemove);
                    currNrOfBikes--;
                }
            }
            currStation.get().setMaximumCapacity(stationEditDTO.getNewCapacity());
        }

        if(!stationEditDTO.getNewName().isBlank()) {
            currStation.get().setName(stationEditDTO.getNewName());
        }

        this.stationRepository.save(currStation.get());
    }

    private void halfFillStation(StationDTO stationDTO) throws Exception {
        for(int i = 0; i < stationDTO.getMaximumCapacity() / 2; i++) {
            BikeDTO bikeDTO = new BikeDTO();
            bikeDTO.setAvailable(false);
            bikeDTO.setUsable(true);
            bikeDTO.setRating(0.0);
            long externalId = this.bikeService.create(bikeDTO);
            addBike(stationDTO.getName(), externalId);
        }
    }

    @Override
    public void importStationsBikesInit() throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(stations_csv_filename)));
        List<StationDTO> stationsFromFile = bufferedReader.lines()
                .map(s -> {
                    String[] words = s.split(",");
                    Double latitude = Double.parseDouble(words[0]);
                    Double longitude = Double.parseDouble(words[1]);
                    long maximumCapacity = Long.parseLong(words[3]);
                    String name = words[2];
                    return new StationDTO(latitude, longitude, maximumCapacity, name);
                })
                .toList();

        //empty up databases with bikes and stations
        this.stationRepository.deleteAll();
        this.bikeRepository.deleteAll();

        for (StationDTO stationDTO : stationsFromFile) {
            create(stationDTO);
            halfFillStation(stationDTO);
        }
    }

    @Override
    public void reflectNewRatingInStation(String username, long editedBikeExternalId) throws Exception{
        //reflect the changed rating in the list of bikes from the end station document
        Optional<Bike> editedBike = this.bikeService.bikeByExternalId(editedBikeExternalId);
        if(editedBike.isEmpty()) {
            throw new DocumentNotFoundException("There is no bike having the given external ID");
        }
        String endStationName = userService.findUserByUsername(username).getCurrentRide().getEndStationName();
        Station endStation = getStationByName(endStationName);
        for (Bike bike: endStation.getBikeList()) {
            if(bike.getExternalId() == editedBikeExternalId) {
                bike.setRating(editedBike.get().getRating());
            }
        }
        editStation(endStation);
    }

    private void writeStationToCSV(StationDTO stationDTO) throws Exception{
        FileWriter fw = new FileWriter(stations_csv_filename, true);
        fw.write("\n");
        fw.write(stationDTO.getLatitude().toString());
        fw.write(",");
        fw.write(stationDTO.getLongitude().toString());
        fw.write(",");
        fw.write(stationDTO.getName());
        fw.write(",");
        fw.write(Long.toString(stationDTO.getMaximumCapacity()));
        fw.close();
    }

    @Override
    public void createWithOptions(StationOptionsDTO stationDTO) throws Exception {
        this.create(stationDTO);
        if(stationDTO.isFillHalf()) {
            halfFillStation(stationDTO);
        }
        if(stationDTO.isSaveToCSV()) {
            writeStationToCSV(stationDTO);
        }
    }

    @Override
    public List<BikeGetDTO> fetchBikeData(BikeFiltersDTO bikeFiltersDTO) throws Exception {
        List<BikeGetDTO> bikeList = new ArrayList<>();
        List<Bike> foundBikes = new ArrayList<>();

        if(bikeFiltersDTO.getExternalId().equals("") && bikeFiltersDTO.getHostStation().equals("") && !bikeFiltersDTO.getOnlyUsable() && !bikeFiltersDTO.getOnlyNotUsable()) { //no filter
            foundBikes = bikeRepository.findAll();
        }

        else if(!bikeFiltersDTO.getExternalId().equals("") && bikeFiltersDTO.getHostStation().equals("")) { //id filter specified and station not specified
            Optional<Bike> foundBike = bikeService.bikeByExternalId(Integer.parseInt(bikeFiltersDTO.getExternalId()));
            if(foundBike.isPresent()) {
                checkUsableFilterAndAdd(foundBikes, foundBike, bikeFiltersDTO.getOnlyUsable(), foundBike.get().isUsable(), bikeFiltersDTO.getOnlyNotUsable(), !foundBike.get().isUsable(), bikeFiltersDTO);
            }
        }

        else if(!bikeFiltersDTO.getExternalId().equals("") && !bikeFiltersDTO.getHostStation().equals("")) { //id and station given
            Optional<Bike> foundBike = bikeService.bikeByExternalId(Integer.parseInt(bikeFiltersDTO.getExternalId()));
            if(foundBike.isPresent()) {
                Station hostStation = getStationByName(bikeFiltersDTO.getHostStation());

                if (hostStation.getBikeList().contains(foundBike.get())) { // the station contains the bike of the given ID
                    checkUsableFilterAndAdd(foundBikes, foundBike, bikeFiltersDTO.getOnlyNotUsable(), !foundBike.get().isUsable(), bikeFiltersDTO.getOnlyUsable(), foundBike.get().isUsable(), bikeFiltersDTO);
                }
            }
        }

        else if (!bikeFiltersDTO.getHostStation().equals("")) {
            if (bikeFiltersDTO.getOnlyUsable()) {
                bikeList = getUsableBikesByStationName(bikeFiltersDTO.getHostStation());
                return bikeList;
            }
            else if (bikeFiltersDTO.getOnlyNotUsable()) {
                bikeList = getNonUsableBikesByStationName(bikeFiltersDTO.getHostStation());
                return bikeList;
            }
            else {
                bikeList = getAllBikesByStationName(bikeFiltersDTO.getHostStation());
                return bikeList;
            }
        }
        else {
            if (bikeFiltersDTO.getOnlyUsable()) {
                foundBikes = bikeService.findAllUsable();
            }
            else if (bikeFiltersDTO.getOnlyNotUsable()) {
                foundBikes = bikeService.findAllNonUsable();
            }
        }

        for(Bike bike : foundBikes) {
            BikeGetDTO bikeGetDTO = new BikeGetDTO();
            modelMapper.map(bike, bikeGetDTO);
            bikeList.add(bikeGetDTO);
        }

        return bikeList;
    }

    public Station findStationOfBike(Bike bike) throws DocumentNotFoundException {
        List<Station> stationsList = this.stationRepository.findAll();
        // find the bike through all stations
        for(Station station : stationsList) {
            if (station.getBikeList().contains(bike)) {
                return station;
            }
        }
        return null;
    }


    @Override
    public void removeBikeByExternalId(String bikeId) throws Exception {
        Optional<Bike> foundBike = bikeService.bikeByExternalId(Long.parseLong(bikeId));
        if(foundBike.isEmpty()) {
            throw new DocumentNotFoundException(StringConstants.BIKE_NOT_FOUND);
        }
        Station hostStation = findStationOfBike(foundBike.get());
        if(hostStation != null) {
            removeBike(hostStation.getName(), Long.parseLong(bikeId));
        }
        // No action if the bike is outside the station
    }

    @Override
    public void repairBike(String externalId) throws DocumentNotFoundException {
        Optional<Bike> foundBike = bikeService.bikeByExternalId(Long.parseLong(externalId));
        if(foundBike.isEmpty()) {
            throw new DocumentNotFoundException(StringConstants.BIKE_NOT_FOUND);
        }
        Station station = findStationOfBike(foundBike.get());
        if (station != null) {
            for (Bike bike : station.getBikeList()) {
                if(bike.getExternalId() == Long.parseLong(externalId)) {
                    bike.setRating(0.0);
                    bike.setUsable(true);
                }
            }
            this.stationRepository.save(station); // update station
        }
        foundBike.get().setRating(0.0);
        foundBike.get().setUsable(true);
        this.bikeRepository.save(foundBike.get());
    }

    private void checkUsableFilterAndAdd(List<Bike> foundBikes, Optional<Bike> foundBike, boolean onlyNotUsable, boolean b, boolean onlyUsable, boolean usable, BikeFiltersDTO bikeFiltersDTO) {
        if(onlyNotUsable) {
            if (b) {
                foundBikes.add(foundBike.get());
            }
        }
        else if (onlyUsable) {
            if (usable) {
                foundBikes.add(foundBike.get());
            }
        }
        else {
            foundBikes.add(foundBike.get());
        }
    }

}
