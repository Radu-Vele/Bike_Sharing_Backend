package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Station;
import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.repository.BikeRepository;
import com.backend.se_project_backend.repository.StationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;
    private final BikeRepository bikeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public StationServiceImpl(StationRepository stationRepository, BikeRepository bikeRepository, ModelMapper modelMapper) {
        this.stationRepository = stationRepository;
        this.bikeRepository = bikeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean addBike(long stationId, long bikeId) {
        Optional<Station> stationById = this.stationRepository.findById(stationId);
        Optional<Bike> bikeById = this.bikeRepository.findById(bikeId);
        if (stationById.isPresent() && bikeById.isPresent() && !bikeById.get().isAvailable() && getFreeSlotsByStationId(stationId) != 0) { //verifică dacă bicicleta respectivă e
                                                                                                                                           // folosită la mom respectiv
                                                                                                                                           // și dacă mai sunt sloturi libere
            bikeById.get().setAvailable(true); //after leaving, o face available
            stationById.get().getBikeList().add(bikeById.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean removeBike(long stationId, long bikeId) {
        Optional<Station> stationById = this.stationRepository.findById(stationId);
        Optional<Bike> bikeById = this.bikeRepository.findById(bikeId);
        if (stationById.isPresent() && bikeById.isPresent() && bikeById.get().isAvailable()) { //verifică dacă bița e available
            bikeById.get().setAvailable(false); //after pick up, o face unavailable
            stationById.get().getBikeList().remove(bikeById.get());
            return true;
        }
        return false;
    }

    @Override
    public void create(Station station) {
        this.stationRepository.save(station);
    }

    @Override
    public void delete(long stationId) {
        Optional<Station> station = this.stationRepository.findById(stationId);
        station.ifPresent(value -> this.stationRepository.deleteById(value.getId()));
    }

    @Override
    public ArrayList<Station> getStations() {
        return (ArrayList<Station>) this.stationRepository.findAll();
    }

    @Override
    public long getFreeSlotsByStationId(long stationId) {
        Optional<Station> stationById = this.stationRepository.findById(stationId);
        if (stationById.isPresent()) {
            List<Bike> listOfBikes = stationById.get().getBikeList();
            return stationById.get().getMaximumCapacity() - listOfBikes.stream().filter(bike -> bike.isAvailable()).count();
        }
        else return -1; //dacă nu există stationId cerut
    }

    @Override
    public ArrayList<Bike> getUsableBikesByStationId(long stationId) {
        Optional<Station> stationById = this.stationRepository.findById(stationId);
        if (stationById.isPresent()) {
            List<Bike> listOfBikes = stationById.get().getBikeList();
            return (ArrayList<Bike>) listOfBikes.stream().filter(bike -> bike.isUsable()).collect(Collectors.toList());
        }
        else return new ArrayList<>(); //dacă nu există stationId cerut
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
