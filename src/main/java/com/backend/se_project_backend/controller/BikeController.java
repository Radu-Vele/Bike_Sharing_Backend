package com.backend.se_project_backend.controller;

import com.backend.se_project_backend.service.BikeService;
import com.backend.se_project_backend.dto.BikeDTO;
import com.backend.se_project_backend.dto.BikeRatingDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@CrossOrigin
public class BikeController {

    private final BikeService bikeService;

    @PostMapping("/create-bike")
    @CrossOrigin
    public ResponseEntity<?> createBike(@Valid @RequestBody BikeDTO bikeDTO) {
        String returnInfo = "New bike of external id: " + this.bikeService.create(bikeDTO);
        return new ResponseEntity<String>(returnInfo, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-bike")
    @CrossOrigin
    public ResponseEntity<?> deleteBike(@RequestParam String bikeId) {
        this.bikeService.delete(bikeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping ("/calculate-rating")
    @CrossOrigin
    public ResponseEntity<?> updateBikeRating(@Valid @RequestBody BikeRatingDTO bikeRatingDTO) throws Exception {
        this.bikeService.calculateRating(bikeRatingDTO.getExternalId(), bikeRatingDTO.getGivenRating());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
