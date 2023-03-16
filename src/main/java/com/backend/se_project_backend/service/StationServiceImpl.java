package com.backend.se_project_backend.service;

import com.backend.se_project_backend.dto.StationDTO;
import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Station;
import com.backend.se_project_backend.repository.BikeRepository;
import com.backend.se_project_backend.repository.StationRepository;
import com.backend.se_project_backend.utils.exceptions.DocumentNotFoundException;
import com.backend.se_project_backend.utils.exceptions.IllegalOperationException;
import com.backend.se_project_backend.utils.exceptions.UniqueDBFieldException;
import com.mongodb.MongoWriteException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;
    private final BikeRepository bikeRepository;
    private final ModelMapper modelMapper;

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

        validateStationBikePairWithDB( stationByName, bikeByExternalId);

        if(getFreeSlotsByStationId(stationByName.get().getId()) == 0) {
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
    public void delete(String stationName) throws DocumentNotFoundException {
        Optional<Station> station = this.stationRepository.findByName(stationName);
        if(station.isPresent()) {
            this.stationRepository.deleteById(station.get().getId());
        }
        else {
            throw new DocumentNotFoundException("Cannot perform deletion. The station with the given name is not in the database.");
        }
    }

    public String getStationNameById(String stationId) {
        Optional<Station> stationById = this.stationRepository.findById(stationId);
        if (stationById.isPresent()) {
            return stationById.get().getName();
        }
        else return "";
    }

    @Override
    public ArrayList<Station> getStations() {
        return (ArrayList<Station>) this.stationRepository.findAll();
    }

    @Override
    public long getFreeSlotsByStationId(String stationId) {
        Optional<Station> stationById = this.stationRepository.findById(stationId);
        if (stationById.isPresent()) {
            List<Bike> listOfBikes = stationById.get().getBikeList();
            return stationById.get().getMaximumCapacity() - listOfBikes.stream().filter(Bike::isAvailable).count();
        }
        else return -1;
    }

    @Override
    public ArrayList<Bike> getUsableBikesByStationId(String stationId) {
        Optional<Station> stationById = this.stationRepository.findById(stationId);
        if (stationById.isPresent()) {
            List<Bike> listOfBikes = stationById.get().getBikeList();
            return (ArrayList<Bike>) listOfBikes.stream().filter(Bike::isUsable).collect(Collectors.toList());
        }
        else return new ArrayList<>();
    }

    @Override
    public ArrayList<Station> getUsableStartStations() {
        ArrayList<Station> listOfStations = getStations();
        ArrayList<Station> toBeRemoved = new ArrayList<>();
        for (Station station : listOfStations) {
            if(getUsableBikesByStationId(station.getId()).isEmpty()) {
                toBeRemoved.add(station);
            }
        }
        if (listOfStations.isEmpty()) {
            return new ArrayList<>();
        }
        if (toBeRemoved.isEmpty()) {
            return listOfStations;
        }
        listOfStations.removeAll(toBeRemoved);
        return listOfStations;
    }

    @Override
    public ArrayList<Station> getFreeEndStations() {
        ArrayList<Station> listOfStations = getStations();
        ArrayList<Station> toBeRemoved = new ArrayList<>();
        for (Station station : listOfStations) {
            if(getFreeSlotsByStationId(station.getId()) == 0) {
                toBeRemoved.add(station);
            }
        }
        if (listOfStations.isEmpty()) {
            return new ArrayList<>();
        }
        if (toBeRemoved.isEmpty()) {
            return listOfStations;
        }
        listOfStations.removeAll(toBeRemoved);
        return listOfStations;
    }
}
