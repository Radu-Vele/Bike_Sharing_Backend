package com.backend.se_project_backend.controller;

import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.service.BikeService;
import com.backend.se_project_backend.utils.dto.BikeDTO;
import com.backend.se_project_backend.utils.dto.BikeRatingDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin
public class BikeController {

    private final BikeService bikeService;
    private final ModelMapper modelMapper;

    @PostMapping("/create-bike")
    @CrossOrigin
    public ResponseEntity<?> createBike(@RequestBody BikeDTO bikeDTO) {
        Bike bike = this.modelMapper.map(bikeDTO, Bike.class);
        this.bikeService.create(bike);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-bike")
    @CrossOrigin
    public ResponseEntity<?> deleteBike(@RequestParam String bikeId) {
        this.bikeService.delete(bikeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping ("/calculate-rating")
    @CrossOrigin
    public ResponseEntity<?> getBikeRating(@RequestBody BikeRatingDTO bikeRatingDTO) {
        boolean status = this.bikeService.calculateRating(bikeRatingDTO.getBikeId(), bikeRatingDTO.getCurrentRating());

        if (status)
            return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
