package com.backend.se_project_backend.service;

import com.backend.se_project_backend.dto.BikeDTO;
import com.backend.se_project_backend.dto.BikeFiltersDTO;
import com.backend.se_project_backend.dto.BikeRatingDTO;
import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.model.Station;
import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.repository.BikeRepository;
import com.backend.se_project_backend.utils.exceptions.DocumentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BikeServiceImpl implements BikeService {

    private final BikeRepository bikeRepository;

    private final ModelMapper modelMapper;

    private final SequenceGeneratorService sequenceGeneratorService;

    private final MongoTemplate mongoTemplate;

    @Override public Optional<Bike> bikeByExternalId(long externalId) {
        return this.bikeRepository.findByExternalId(externalId);
    }

    @Override
    public long create(BikeDTO bikeDTO) {
        Bike bike = this.modelMapper.map(bikeDTO, Bike.class);
        bike.setExternalId(sequenceGeneratorService.generateSequence(Bike.SEQUENCE_NAME));
        return this.bikeRepository.save(bike).getExternalId();
    }

    @Override
    public void delete(String bikeId) {
        Query query = new Query(Criteria.where("externalId").is(Long.parseLong(bikeId)));
        mongoTemplate.remove(query, Bike.class, "bikes");
    }

    @Override
    public void calculateRating(long externalId, Double currentRating, String username) throws Exception {
        Optional<Bike> bikeById = bikeByExternalId(externalId);
        if (bikeById.isPresent()) {
            Double previousRating = bikeById.get().getRating();
            if (previousRating == 0) {
                bikeById.get().setRating(currentRating); //allow bikes to be initialized with 0 and be usable
            }
            else {
                bikeById.get().setRating((previousRating + currentRating) / 2);
            }

            bikeRepository.save(bikeById.get());
        }
        else {
            throw new DocumentNotFoundException("There is no bike with the given external ID in the database.");
        }
    }

    @Override
    public List<Bike> findAllUsable() {
        return bikeRepository.findAllUsable();
    }

    @Override
    public List<Bike> findAllNonUsable() {
        return bikeRepository.findAllNonUsable();
    }
}
