package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Station;
import com.backend.se_project_backend.repository.BikeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class BikeServiceImpl implements BikeService {

    private final BikeRepository bikeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BikeServiceImpl(BikeRepository bikeRepository, ModelMapper modelMapper) {
        this.bikeRepository = bikeRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public Optional<Bike> bikeById(long bikeId) {
        return this.bikeRepository.findById(bikeId);
    }

    @Override
    public void create(Bike bike) {
        this.bikeRepository.save(bike);
    }

    @Override
    public void delete(long bikeId) {
        this.bikeRepository.deleteById(bikeId);
    }

    @Override
    public boolean calculateRating(long bikeId, Double currentRating) {
        Optional<Bike> bikeById = bikeById(bikeId);
        if (bikeById.isPresent()) {
            Double previousRating = bikeById.get().getRating();
            if (previousRating == 0)
                bikeById.get().setRating(currentRating);
            else if (currentRating == 1) {
                bikeById.get().setUsable(false);
            }
            else {
                bikeById.get().setRating((previousRating + currentRating) / 2);
            }

            if (bikeById.get().getRating() < 2.5) {
                bikeById.get().setUsable(false);
            }
            return true;
        }
        else return false;
    }
}
