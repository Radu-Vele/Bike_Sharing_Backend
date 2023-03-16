package com.backend.se_project_backend.service;

import com.backend.se_project_backend.dto.BikeGetDTO;
import com.backend.se_project_backend.dto.StationDTO;
import com.backend.se_project_backend.dto.StationGetDTO;
import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Station;
import com.backend.se_project_backend.repository.BikeRepository;
import com.backend.se_project_backend.repository.StationRepository;
import com.backend.se_project_backend.utils.exceptions.DocumentNotFoundException;
import com.backend.se_project_backend.utils.exceptions.IllegalOperationException;
import com.backend.se_project_backend.utils.exceptions.UniqueDBFieldException;
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
    public void delete(String stationName) throws DocumentNotFoundException {
        Optional<Station> station = this.stationRepository.findByName(stationName);
        if(station.isPresent()) {
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

    @Override
    public ArrayList<BikeGetDTO> getUsableBikesByStationName(String stationName) throws DocumentNotFoundException {
        Optional<Station> stationByName = this.stationRepository.findByName(stationName);

        if (stationByName.isEmpty()) {
            throw new DocumentNotFoundException("There is no station having the given name in the database.");
        }

        List<Bike> bikesFromStation = stationByName.get().getBikeList();
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
}
