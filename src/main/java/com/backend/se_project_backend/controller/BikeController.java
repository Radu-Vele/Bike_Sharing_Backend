package com.backend.se_project_backend.controller;

import com.backend.se_project_backend.config.jwt.JwtUtility;
import com.backend.se_project_backend.dto.BikeFiltersDTO;
import com.backend.se_project_backend.service.BikeService;
import com.backend.se_project_backend.dto.BikeDTO;
import com.backend.se_project_backend.dto.BikeRatingDTO;
import com.backend.se_project_backend.service.StationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@CrossOrigin
public class BikeController {

    private final BikeService bikeService;

    private final JwtUtility jwtUtility;

    private final StationService stationService;

    @PostMapping("/create-bike")
    @CrossOrigin
    public ResponseEntity<?> createBike(@Valid @RequestBody BikeDTO bikeDTO) {
        String returnInfo = "New bike of external id: " + this.bikeService.create(bikeDTO);
        return new ResponseEntity<String>(returnInfo, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-bike")
    @CrossOrigin
    public ResponseEntity<?> deleteBike(@RequestParam String bikeId) throws Exception {
        //find its station and delete
        this.stationService.removeBikeByExternalId(bikeId);
        this.bikeService.delete(bikeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping ("/calculate-rating")
    @CrossOrigin
    public ResponseEntity<?> updateBikeRating(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @Valid @RequestBody BikeRatingDTO bikeRatingDTO) throws Exception {
        String username = jwtUtility.getUsernameFromToken(auth.substring(7));
        this.bikeService.calculateRating(bikeRatingDTO.getExternalId(), bikeRatingDTO.getGivenRating(), username);
        this.stationService.reflectNewRatingInStation(username, bikeRatingDTO.getExternalId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping ("/repair-bike")
    @CrossOrigin
    public ResponseEntity<?> updateBikeRating(@RequestParam String externalId) throws Exception {
        this.stationService.repairBike(externalId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping ("/make-bike-unusable")
    @CrossOrigin
    public ResponseEntity<?> makeBikeUnusable(@RequestParam String externalId) throws Exception {
        this.stationService.makeBikeUnusable(externalId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/fetch-bikes-filtered")
    @CrossOrigin
    public ResponseEntity<?> fetchBikeData(@RequestBody BikeFiltersDTO bikeFiltersDTO) throws Exception {
        return ResponseEntity.ok(stationService.fetchBikeData(bikeFiltersDTO));
    }
}
