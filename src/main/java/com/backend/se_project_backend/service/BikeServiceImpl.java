package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Bike;
import com.backend.se_project_backend.repository.BikeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
    public void create(Bike bike) {
        this.bikeRepository.save(bike);
    }

    @Override
    public void delete(long bikeId) {
        this.bikeRepository.deleteById(bikeId);
    }
}
