package org.springframework.samples.petclinic.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.rest.dto.PetTypeCountDTO;
import org.springframework.samples.petclinic.rest.dto.TopServiceDTO;
import org.springframework.samples.petclinic.service.PetStatisticsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(PetStatisticsController.class)
class PetStatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetStatisticsService petStatisticsService;

    @Test
    void testGetPetsByType_WithData() throws Exception {
        // Arrange
        List<PetTypeCountDTO> mockData = Arrays.asList(
            new PetTypeCountDTO("cat", 5L),
            new PetTypeCountDTO("dog", 10L)
        );
        when(petStatisticsService.getPetCountByType()).thenReturn(mockData);

        // Act & Assert
        mockMvc.perform(get("/api/statistics/pets/by-type"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[0].petType").value("cat"))
            .andExpect(jsonPath("$.data[0].count").value(5))
            .andExpect(jsonPath("$.data[1].petType").value("dog"))
            .andExpect(jsonPath("$.data[1].count").value(10));
    }

    @Test
    void testGetPetsByType_EmptyData() throws Exception {
        // Arrange
        when(petStatisticsService.getPetCountByType()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/statistics/pets/by-type"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data").isEmpty())
            .andExpect(jsonPath("$.message").value("No service request data available"));
    }

    @Test
    void testGetTopServices_WithData() throws Exception {
        // Arrange
        List<TopServiceDTO> mockData = Arrays.asList(
            new TopServiceDTO("cat", "Dental Surgery", 3L),
            new TopServiceDTO("dog", "Orthopedic Surgery", 5L)
        );
        when(petStatisticsService.getTopServiceByPetType()).thenReturn(mockData);

        // Act & Assert
        mockMvc.perform(get("/api/statistics/pets/top-services"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[0].petType").value("cat"))
            .andExpect(jsonPath("$.data[0].serviceName").value("Dental Surgery"))
            .andExpect(jsonPath("$.data[0].requestCount").value(3))
            .andExpect(jsonPath("$.data[1].petType").value("dog"))
            .andExpect(jsonPath("$.data[1].serviceName").value("Orthopedic Surgery"))
            .andExpect(jsonPath("$.data[1].requestCount").value(5));
    }

    @Test
    void testGetTopServices_EmptyData() throws Exception {
        // Arrange
        when(petStatisticsService.getTopServiceByPetType()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/statistics/pets/top-services"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data").isEmpty())
            .andExpect(jsonPath("$.message").value("No service request data available"));
    }
}
