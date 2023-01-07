package com.backend.se_project_backend.controller;

import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.service.RideService;
import com.backend.se_project_backend.utils.dto.RideDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin
public class RideController {

    private final RideService rideService;
    private final ModelMapper modelMapper;

    @PostMapping("/init-ride")
    @CrossOrigin
    public ResponseEntity<?> initRide(@RequestBody RideDTO rideDTO) {
        if (this.rideService.startRide(rideDTO) != null)
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/end-ride")
    @CrossOrigin
    public ResponseEntity<?> endRide(@RequestParam Ride ride) {
        if (this.rideService.endRide(ride) != null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



}
