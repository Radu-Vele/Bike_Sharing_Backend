package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.repository.RideRepository;
import com.backend.se_project_backend.dto.RideDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;

    private final StationService stationService;

    private final UserService userService;

    private final RecommenderService recommenderService;

    @Override
    public void create(Ride ride) {
        this.rideRepository.save(ride);
    }

    @Override
    public void delete(String rideId) {
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

//        if (stationService.removeBike(newRide.getStartStationId(), newRide.getBikeId())) {
//            rideRepository.save(newRide);
//            userService.editStartRide(newRide, ride.getUsername());
//            System.out.println(path);
//            return true;
//        }
        return false;
    }

    @Override
    public boolean endRide(String rideId) {
        Ride ride = findRideById(rideId);
        ride.setCompleted(true);
        ride.endTime();

/*        if (stationService.addBike(ride.getEndStationId(), ride.getBikeId())) {
            rideRepository.save(ride);
            userService.editEndRide(ride, ride.getUsername());
            return true;
        }*/

        return false;
    }

    @Override
    public List<Ride> findRidesByUser(String username) throws Exception {
        if (userService.userByUsernameExists(username)) {
            User user = userService.findUserByUsername(username);
            return user.getRideList();
        }
        return null;
    }

    @Override
    public Ride findRideById(String rideId) {
        return this.rideRepository.findById(rideId).orElseThrow();
    }
}
