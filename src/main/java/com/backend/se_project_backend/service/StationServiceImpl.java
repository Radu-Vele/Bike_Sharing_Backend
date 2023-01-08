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
import java.util.Optional;

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
        if (stationById.isPresent() && bikeById.isPresent() && !bikeById.get().isAvailable()) { //verifică dacă bicicleta respectivă e folosită la mom respectiv
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

    public String getStationNameById(long stationId) {
        Optional<Station> stationById = this.stationRepository.findById(stationId);
        if (stationById.isPresent()) {
            return stationById.get().getName();
        }
        else return "";
    }


}
