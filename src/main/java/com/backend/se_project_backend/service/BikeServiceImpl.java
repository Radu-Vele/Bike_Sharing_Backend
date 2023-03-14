package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Station;
import com.backend.se_project_backend.repository.BikeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BikeServiceImpl implements BikeService {

    private final BikeRepository bikeRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Override
    public Optional<Bike> bikeById(String bikeId) {
        return this.bikeRepository.findById(bikeId);
    }

    @Override
    public void create(Bike bike) {
        bike.setExternalId(sequenceGeneratorService.generateSequence(Bike.SEQUENCE_NAME));
        this.bikeRepository.save(bike);
    }

    @Override
    public void delete(String bikeId) {
        this.bikeRepository.deleteById(bikeId);
    }

    @Override
    public boolean calculateRating(String bikeId, Double currentRating) {
        Optional<Bike> bikeById = bikeById(bikeId);
        if (bikeById.isPresent()) {
            Double previousRating = bikeById.get().getRating();
            if (previousRating == 0) {
                bikeById.get().setRating(currentRating); //allow bikes to be initialized with 0 and be usable
            }
            else {
                if (currentRating == 1) {
                    bikeById.get().setUsable(false);
                }
                else {
                    bikeById.get().setRating((previousRating + currentRating) / 2);
                }

                if (bikeById.get().getRating() < 2.5) {
                    bikeById.get().setUsable(false);
                }
            }

            bikeRepository.save(bikeById.get());

            return true;
        }
        else return false;
    }
}
