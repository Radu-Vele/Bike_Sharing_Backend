package com.backend.se_project_backend.controller;


import com.backend.se_project_backend.dto.BikeDTO;
import com.backend.se_project_backend.dto.BikeRatingDTO;
import com.backend.se_project_backend.service.BikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO: Make sure to figure out how to run the test
@WebMvcTest(BikeController.class)
@AutoConfigureMockMvc(addFilters = false) //prevents spring security need for a token
@ExtendWith(MockitoExtension.class)
public class BikeControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BikeService bikeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateBike() throws Exception {
        // Given
        BikeDTO bikeDTO = new BikeDTO();
        bikeDTO.setUsable(true);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/create-bike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bikeDTO)))
                .andExpect(status().isBadRequest());

        // Then
        verify(bikeService, times(1)).create(bikeDTO);
    }

    @Test
    public void testDeleteBike() throws Exception {
    }

    @Test
    public void testGetBikeRating() throws Exception {
        // Given
        BikeRatingDTO bikeRatingDTO = new BikeRatingDTO();
        bikeRatingDTO.setExternalId(1234);
        bikeRatingDTO.setGivenRating(4.5);

        // When
        mockMvc.perform(put("/calculate-rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bikeRatingDTO)))
                .andExpect(status().isOk());

        // Then
    }

}