package com.backend.se_project_backend.controller;

import com.backend.se_project_backend.dto.*;
import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Station;
import com.backend.se_project_backend.service.StationService;
import com.backend.se_project_backend.utils.exceptions.DocumentNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
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

    @PostMapping ("/create-station-options")
    @CrossOrigin
    public ResponseEntity<?> createStation(@Valid @RequestBody StationOptionsDTO stationDTO) throws Exception{
        this.stationService.createWithOptions(stationDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/edit-station")
    @CrossOrigin
    public ResponseEntity<?> editStation(@Valid @RequestBody StationEditDTO stationEditDTO) throws Exception {
        this.stationService.editStationNameCapacity(stationEditDTO);
        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<?> pickUpBikeFromStation(@Valid @RequestBody StationBikePairDTO stationBikePairDTO) throws Exception {
        this.stationService.removeBike(stationBikePairDTO.getStationName(), stationBikePairDTO.getBikeExternalId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping ("/get-stations")
    @CrossOrigin
    public ArrayList<StationGetDTO> getListOfStations() {
        return this.stationService.getStations();
    }

    @GetMapping ("/get-free-slots")
    @CrossOrigin
    public long getFreeSlots(@RequestParam String stationName) throws DocumentNotFoundException {
        return this.stationService.getFreeSlotsByStationName(stationName);
    }

    @GetMapping ("/get-usable-bikes")
    @CrossOrigin
    public ArrayList<BikeGetDTO> getUsableBikes(@RequestParam String stationName) throws DocumentNotFoundException {
        return this.stationService.getUsableBikesByStationName(stationName);
    }

    @GetMapping ("/get-start-stations")
    @CrossOrigin
    public ArrayList<StationGetDTO> getListOfUsableStations() throws Exception { return this.stationService.getUsableStartStations(); }

    @GetMapping ("/get-end-stations")
    @CrossOrigin
    public ArrayList<StationGetDTO> getListOfFreeStations() throws Exception { return this.stationService.getFreeEndStations(); }

    /**
     * Import stations found in csv file and populate each one to half its capacity
     * - the DB collections for bikes and stations are emptied
     */
    @PostMapping("/init-stations-bikes-csv")
    @CrossOrigin
    public ResponseEntity<?> importStationsBikesInit() throws Exception {
        stationService.importStationsBikesInit();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}