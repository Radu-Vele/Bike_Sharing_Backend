package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Account;
import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.repository.RideRepository;
import com.backend.se_project_backend.utils.dto.RideDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;

    private final ModelMapper modelMapper;

    private final StationService stationService;
    private final UserService userService;

    @Autowired
    public RideServiceImpl(RideRepository rideRepository, ModelMapper modelMapper, StationService stationService, UserService userService) {
        this.rideRepository = rideRepository;
        this.modelMapper = modelMapper;
        this.stationService = stationService;
        this.userService = userService;
    }

    @Override
    public void create(Ride ride) {
        this.rideRepository.save(ride);
    }

    @Override
    public void delete(long rideId) {
        this.rideRepository.deleteById(rideId);
    }

    @Override
    public Ride startRide(RideDTO ride) {
        Ride newRide = new Ride();
        newRide.startTime();
        newRide.setStartStationId(ride.getStartStationId());
        newRide.setEndStationId(ride.getEndStationId());
        newRide.setBikeId(ride.getBikeId());
        newRide.setUsername(ride.getUsername());

        User user = userService.findUserByUsername(ride.getUsername());
        user.setCurrentRide(newRide);
        user.setActiveRide(true);
        if (stationService.removeBike(newRide.getStartStationId(), newRide.getBikeId()))
            return rideRepository.save(newRide);
        return null;
    }

    @Override
    public Ride endRide(Ride ride) {
        ride.setCompleted(true);
        ride.endTime();
        User user = userService.findUserByUsername(ride.getUsername());
        user.setActiveRide(false);
        Account account = userService.findAccountByUsername(ride.getUsername());
        account.addRide(ride);
        if (stationService.addBike(ride.getEndStationId(), ride.getBikeId()))
            return rideRepository.save(ride);
        return null;
    }



}
