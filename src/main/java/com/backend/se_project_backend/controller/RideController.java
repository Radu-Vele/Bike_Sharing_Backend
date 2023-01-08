package com.backend.se_project_backend.controller;

import com.backend.se_project_backend.model.Account;
import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.service.RideService;
import com.backend.se_project_backend.utils.dto.RideDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
public class RideController {

    private final RideService rideService;
    private final ModelMapper modelMapper;

    @PostMapping("/init-ride")
    @CrossOrigin
    public ResponseEntity<?> initRide(@RequestBody RideDTO rideDTO) {
        boolean status = this.rideService.startRide(rideDTO);
        if (status)
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/end-ride")
    @CrossOrigin
    public ResponseEntity<?> endRide(@RequestParam long rideId) {
        boolean status = this.rideService.endRide(rideId);
        if (status)
            return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/ride-list")
    @CrossOrigin
    public List<Ride> showRidesByUser(@RequestParam String username) {
        return this.rideService.findRidesByUser(username);
    }





}
