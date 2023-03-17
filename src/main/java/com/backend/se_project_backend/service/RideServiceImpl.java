package com.backend.se_project_backend.service;

import com.backend.se_project_backend.dto.RideGetDTO;
import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.repository.RideRepository;
import com.backend.se_project_backend.dto.RideDTO;
import com.backend.se_project_backend.utils.exceptions.DocumentNotFoundException;
import com.backend.se_project_backend.utils.exceptions.IllegalOperationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;

    private final StationService stationService;

    private final UserService userService;

    //private final RecommenderService recommenderService;

    private final ModelMapper modelMapper;

    @Override
    public void create(Ride ride) {
        this.rideRepository.save(ride);
    }

    @Override
    public void delete(String rideId) {
        this.rideRepository.deleteById(rideId);
    }

    @Override
    public void startRide(String username, RideDTO ride) throws Exception {
        User user = this.userService.findUserByUsername(username);
        if(user.isInActiveRide()) {
            throw new IllegalOperationException("The user already has an active ride. Can not start a new one.");
        }
        Ride newRide = new Ride();
        newRide.startTime();

        stationService.removeBike(ride.getStartStationName(), ride.getBikeExternalId()); //also checks if the bike is in that station
        newRide.setStartStationName(ride.getStartStationName());

        if(stationService.getFreeSlotsByStationName(ride.getEndStationName()) == 0) {
            throw new IllegalOperationException("The chosen end station has no free slots.");
        }
        newRide.setEndStationName(ride.getEndStationName());
        newRide.setBikeExternalId(ride.getBikeExternalId());
        //TODO: add recommender String path = recommenderService.dijkstra(newRide.getStartStationId(), newRide.getEndStationId());
        newRide.setRecommendation("To be implemented...");
        rideRepository.save(newRide);
        userService.editStartRide(newRide, username);
    }

    @Override
    public void endRide(String username) throws Exception {
        Ride ride = userService.findUserByUsername(username).getCurrentRide();
        stationService.addBike(ride.getEndStationName(), ride.getBikeExternalId());
        ride.setCompleted(true);
        ride.endTime();
        userService.editEndRide(ride, username);
        rideRepository.save(ride);
    }

    @Override
    public List<RideGetDTO> findRidesByUser(String username) throws Exception {
        User user = userService.findUserByUsername(username);
        ArrayList<RideGetDTO> rideGetDTOArrayList = new ArrayList<>();
        for (Ride ride : user.getRideList()) {
            RideGetDTO rideGetDTO = this.modelMapper.map(ride, RideGetDTO.class);
            rideGetDTOArrayList.add(rideGetDTO);
        }
        return rideGetDTOArrayList;
    }

    @Override
    public Ride findRideById(String rideId) {
        return this.rideRepository.findById(rideId).orElseThrow();
    }
}
