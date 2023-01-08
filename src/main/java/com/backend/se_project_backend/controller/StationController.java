package com.backend.se_project_backend.controller;

import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Station;
import com.backend.se_project_backend.service.StationService;
import com.backend.se_project_backend.utils.dto.StationBikePairDTO;
import com.backend.se_project_backend.utils.dto.StationDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@AllArgsConstructor
@CrossOrigin
public class StationController {

    private final StationService stationService;
    private final ModelMapper modelMapper;

    @PostMapping ("/create-station")
    @CrossOrigin
    public ResponseEntity<?> createStation(@RequestBody StationDTO stationDTO) {
        Station station = this.modelMapper.map(stationDTO, Station.class);
        this.stationService.create(station);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping ("/delete-station")
    @CrossOrigin
    public ResponseEntity<?> deleteStation(@RequestParam long stationId) {
        this.stationService.delete(stationId);
        return new ResponseEntity<>(HttpStatus.OK); //or whatever this should be
    }

    @PutMapping ("/add-bike")
    @CrossOrigin //plus id pentru station
    public ResponseEntity<?> leaveBikeInStation(@RequestBody StationBikePairDTO stationBikePairDTO) {
        boolean status = this.stationService.addBike(stationBikePairDTO.getStationId(), stationBikePairDTO.getBikeId());

        if (status)
            return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping ("/remove-bike")
    @CrossOrigin
    public ResponseEntity<?> pickUpBikeFromStation(@RequestBody StationBikePairDTO stationBikePairDTO) {
        boolean status = this.stationService.removeBike(stationBikePairDTO.getStationId(), stationBikePairDTO.getBikeId());
        if (status)
            return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping ("/get-stations")
    @CrossOrigin
    public ArrayList<Station> getListOfStations() {
        return this.stationService.getStations();
    }

    @GetMapping ("/get-free-slots")
    @CrossOrigin
    public long getFreeSlots(@RequestParam long stationId) {
        return this.stationService.getFreeSlotsByStationId(stationId);
    }

    @GetMapping ("/get-usable-bikes")
    @CrossOrigin
    public ArrayList<Bike> getUsableBikes(@RequestParam long stationId) {
        return this.stationService.getUsableBikesByStationId(stationId);
    }

    @GetMapping ("/get-usable-stations")
    @CrossOrigin
    public ArrayList<Station> getListOfUsableStations() { return this.stationService.getUsableStartStations(); }

    @GetMapping ("/get-free-stations")
    @CrossOrigin
    public ArrayList<Station> getListOfFreeStations() { return this.stationService.getFreeEndStations(); }


}
