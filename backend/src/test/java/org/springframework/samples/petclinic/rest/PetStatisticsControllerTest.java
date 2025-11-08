package org.springframework.samples.petclinic.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.samples.petclinic.rest.dto.PetTypeCountDTO;
import org.springframework.samples.petclinic.rest.dto.TopServiceDTO;
import org.springframework.samples.petclinic.service.PetStatisticsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for PetStatisticsController.
 */
@WebMvcTest(PetStatisticsController.class)
class PetStatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetStatisticsService petStatisticsService;

    @Test
    void getPetsByType_shouldReturnPetCounts() throws Exception {
        // Given
        List<PetTypeCountDTO> mockData = Arrays.asList(
            new PetTypeCountDTO("dog", 10L),
            new PetTypeCountDTO("cat", 8L)
        );
        when(petStatisticsService.getPetCountByType()).thenReturn(mockData);

        // When & Then
        mockMvc.perform(get("/api/statistics/pets/by-type"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.count").value(2))
            .andExpect(jsonPath("$.data[0].petType").value("dog"))
            .andExpect(jsonPath("$.data[0].count").value(10))
            .andExpect(jsonPath("$.data[1].petType").value("cat"))
            .andExpect(jsonPath("$.data[1].count").value(8));
    }

    @Test
    void getPetsByType_shouldReturnEmptyArrayWhenNoData() throws Exception {
        // Given
        when(petStatisticsService.getPetCountByType()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/statistics/pets/by-type"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.count").value(0))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void getTopServices_shouldReturnTopServices() throws Exception {
        // Given
        List<TopServiceDTO> mockData = Arrays.asList(
            new TopServiceDTO("dog", "Dental Cleaning", 15L),
            new TopServiceDTO("cat", "Radiology Scan", 12L)
        );
        when(petStatisticsService.getTopServiceByPetType()).thenReturn(mockData);

        // When & Then
        mockMvc.perform(get("/api/statistics/pets/top-services"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.count").value(2))
            .andExpect(jsonPath("$.data[0].petType").value("dog"))
            .andExpect(jsonPath("$.data[0].serviceName").value("Dental Cleaning"))
            .andExpect(jsonPath("$.data[0].requestCount").value(15))
            .andExpect(jsonPath("$.data[1].petType").value("cat"))
            .andExpect(jsonPath("$.data[1].serviceName").value("Radiology Scan"))
            .andExpect(jsonPath("$.data[1].requestCount").value(12));
    }

    @Test
    void getTopServices_shouldReturnEmptyArrayWhenNoData() throws Exception {
        // Given
        when(petStatisticsService.getTopServiceByPetType()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/statistics/pets/top-services"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.count").value(0))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data").isEmpty());
    }
}
