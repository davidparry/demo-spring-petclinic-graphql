/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.samples.petclinic.PetClinicTestDbConfiguration;
import org.springframework.samples.petclinic.model.PetStatisticsService;
import org.springframework.samples.petclinic.rest.dto.PetTypeCountDTO;
import org.springframework.samples.petclinic.rest.dto.TopServiceDTO;
import org.springframework.samples.petclinic.security.SecurityConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for PetStatisticsController REST endpoints.
 *
 * @author Agent
 */
@WebMvcTest(PetStatisticsController.class)
@Import({SecurityConfig.class, PetClinicTestDbConfiguration.class})
class PetStatisticsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetStatisticsService petStatisticsService;

    @Test
    void testGetPetsByType_WithData() throws Exception {
        // Given
        List<PetTypeCountDTO> mockData = Arrays.asList(
            new PetTypeCountDTO("cat", 5L),
            new PetTypeCountDTO("dog", 7L),
            new PetTypeCountDTO("bird", 2L)
        );
        when(petStatisticsService.getPetCountByType()).thenReturn(mockData);

        // When & Then
        mockMvc.perform(get("/api/statistics/pets/by-type"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(3))
            .andExpect(jsonPath("$.data[0].petType").value("cat"))
            .andExpect(jsonPath("$.data[0].count").value(5))
            .andExpect(jsonPath("$.data[1].petType").value("dog"))
            .andExpect(jsonPath("$.data[1].count").value(7))
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testGetPetsByType_EmptyData() throws Exception {
        // Given
        when(petStatisticsService.getPetCountByType()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/statistics/pets/by-type"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(0))
            .andExpect(jsonPath("$.message").value("No pet data available"))
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testGetTopServices_WithData() throws Exception {
        // Given
        List<TopServiceDTO> mockData = Arrays.asList(
            new TopServiceDTO("cat", Arrays.asList("Teeth Cleaning", "Vaccination")),
            new TopServiceDTO("dog", Arrays.asList("Vaccination", "Surgery", "Teeth Cleaning"))
        );
        when(petStatisticsService.getTopServiceByPetType()).thenReturn(mockData);

        // When & Then
        mockMvc.perform(get("/api/statistics/pets/top-services"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.data[0].petType").value("cat"))
            .andExpect(jsonPath("$.data[0].topServices").isArray())
            .andExpect(jsonPath("$.data[0].topServices.length()").value(2))
            .andExpect(jsonPath("$.data[1].petType").value("dog"))
            .andExpect(jsonPath("$.data[1].topServices.length()").value(3))
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testGetTopServices_EmptyData() throws Exception {
        // Given
        when(petStatisticsService.getTopServiceByPetType()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/statistics/pets/top-services"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(0))
            .andExpect(jsonPath("$.message").value("No service data available"))
            .andExpect(jsonPath("$.timestamp").exists());
    }
}
