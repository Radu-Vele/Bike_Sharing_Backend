package com.backend.se_project_backend.controller;

import com.backend.se_project_backend.config.jwt.JwtUtility;
import com.backend.se_project_backend.dto.RideGetDTO;
import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.service.RideService;
import com.backend.se_project_backend.dto.RideDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
public class RideController {

    private final RideService rideService;

    private final JwtUtility jwtUtility;

    @PostMapping("/init-ride")
    @CrossOrigin
    public ResponseEntity<?> initRide(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @Valid @RequestBody RideDTO rideDTO) throws Exception {
        String username = jwtUtility.getUsernameFromToken(auth.substring(7));
        this.rideService.startRide(username, rideDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/end-ride")
    @CrossOrigin
    public ResponseEntity<?> endRide(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) throws Exception {
        String username = jwtUtility.getUsernameFromToken(auth.substring(7));
        this.rideService.endRide(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/ride-list")
    @CrossOrigin
    public List<RideGetDTO> showRidesByUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) throws Exception {
        String username = jwtUtility.getUsernameFromToken(auth.substring(7));
        return this.rideService.findRidesByUser(username);
    }
}
