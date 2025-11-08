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

import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.PetStatisticsService;
import org.springframework.samples.petclinic.rest.dto.PetTypeCountDTO;
import org.springframework.samples.petclinic.rest.dto.StatisticsResponseDTO;
import org.springframework.samples.petclinic.rest.dto.TopServiceDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for pet statistics endpoints.
 * Provides API endpoints for retrieving pet distribution and service statistics.
 *
 * @author Agent
 */
@RestController
@RequestMapping("/api/statistics/pets")
public class PetStatisticsController {

    private final PetStatisticsService petStatisticsService;

    public PetStatisticsController(PetStatisticsService petStatisticsService) {
        this.petStatisticsService = petStatisticsService;
    }

    /**
     * Get pet count grouped by pet type.
     *
     * @return ResponseEntity containing list of pet type counts
     */
    @GetMapping("/by-type")
    public ResponseEntity<StatisticsResponseDTO<List<PetTypeCountDTO>>> getPetsByType() {
        List<PetTypeCountDTO> data = petStatisticsService.getPetCountByType();
        StatisticsResponseDTO<List<PetTypeCountDTO>> response =
            new StatisticsResponseDTO<>(data, data.isEmpty() ? "No pet data available" : null);
        return ResponseEntity.ok(response);
    }

    /**
     * Get top 3 most requested services for each pet type.
     *
     * @return ResponseEntity containing list of top services by pet type
     */
    @GetMapping("/top-services")
    public ResponseEntity<StatisticsResponseDTO<List<TopServiceDTO>>> getTopServices() {
        List<TopServiceDTO> data = petStatisticsService.getTopServiceByPetType();
        StatisticsResponseDTO<List<TopServiceDTO>> response =
            new StatisticsResponseDTO<>(data, data.isEmpty() ? "No service data available" : null);
        return ResponseEntity.ok(response);
    }
}
