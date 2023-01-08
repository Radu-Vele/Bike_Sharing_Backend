package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Account;
import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.repository.RideRepository;
import com.backend.se_project_backend.utils.dto.RideDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;

    private final ModelMapper modelMapper;

    private final StationService stationService;
    private final UserService userService;
    private final RecommenderService recommenderService;

    @Autowired
    public RideServiceImpl(RideRepository rideRepository, ModelMapper modelMapper, StationService stationService, UserService userService, RecommenderService recommenderService) {
        this.rideRepository = rideRepository;
        this.modelMapper = modelMapper;
        this.stationService = stationService;
        this.userService = userService;
        this.recommenderService = recommenderService;
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
    public boolean startRide(RideDTO ride) {
        Ride newRide = new Ride();
        newRide.startTime();
        newRide.setStartStationId(ride.getStartStationId());
        newRide.setEndStationId(ride.getEndStationId());
        newRide.setBikeId(ride.getBikeId());
        newRide.setUsername(ride.getUsername());
        String path = recommenderService.dijkstra(newRide.getStartStationId(), newRide.getEndStationId());
        newRide.setRecommendation(path);

        if (stationService.removeBike(newRide.getStartStationId(), newRide.getBikeId())) {
            rideRepository.save(newRide);
            userService.editStartRide(newRide, ride.getUsername());
            System.out.println(path);
            return true;
        }
        return false;
    }

    @Override
    public boolean endRide(long rideId) {
        Ride ride = findRideById(rideId);
        ride.setCompleted(true);
        ride.endTime();

        if (stationService.addBike(ride.getEndStationId(), ride.getBikeId())) {
            rideRepository.save(ride);
            userService.editEndRide(ride, ride.getUsername());
            return true;
        }

        return false;
    }

    @Override
    public List<Ride> findRidesByUser(String username) {
            if (userService.userByUsernameExists(username)) {
                Account account = userService.findAccountByUsername(username);
                return account.getRideList();
            }
            return null;
    }

    @Override
    public Ride findRideById(long rideId) {
        return this.rideRepository.findById(rideId).orElseThrow();
    }


}
