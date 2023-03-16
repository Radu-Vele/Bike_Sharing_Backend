package com.backend.se_project_backend.controller;

import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Station;
import com.backend.se_project_backend.service.StationService;
import com.backend.se_project_backend.dto.StationBikePairDTO;
import com.backend.se_project_backend.dto.StationDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@AllArgsConstructor
@CrossOrigin
public class StationController {

    private final StationService stationService;

    @PostMapping ("/create-station")
    @CrossOrigin
    public ResponseEntity<?> createStation(@Valid @RequestBody StationDTO stationDTO) throws Exception{
        this.stationService.create(stationDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping ("/delete-station")
    @CrossOrigin
    public ResponseEntity<?> deleteStation(@RequestParam String stationName) throws Exception{
        this.stationService.delete(stationName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping ("/add-bike")
    @CrossOrigin
    public ResponseEntity<?> addBikeToStation(@Valid @RequestBody StationBikePairDTO stationBikePairDTO) throws Exception{
        this.stationService.addBike(stationBikePairDTO.getStationName(), stationBikePairDTO.getBikeExternalId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping ("/remove-bike")
    @CrossOrigin
    public ResponseEntity<?> pickUpBikeFromStation(@RequestBody StationBikePairDTO stationBikePairDTO) throws Exception {
        this.stationService.removeBike(stationBikePairDTO.getStationName(), stationBikePairDTO.getBikeExternalId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping ("/get-stations")
    @CrossOrigin
    public ArrayList<Station> getListOfStations() {
        return this.stationService.getStations();
    }

    @GetMapping ("/get-free-slots")
    @CrossOrigin
    public long getFreeSlots(@RequestParam String stationId) {
        return this.stationService.getFreeSlotsByStationId(stationId);
    }

    @GetMapping ("/get-usable-bikes")
    @CrossOrigin
    public ArrayList<Bike> getUsableBikes(@RequestParam String stationId) {
        return this.stationService.getUsableBikesByStationId(stationId);
    }

    @GetMapping ("/get-usable-stations")
    @CrossOrigin
    public ArrayList<Station> getListOfUsableStations() { return this.stationService.getUsableStartStations(); }

    @GetMapping ("/get-free-stations")
    @CrossOrigin
    public ArrayList<Station> getListOfFreeStations() { return this.stationService.getFreeEndStations(); }
}