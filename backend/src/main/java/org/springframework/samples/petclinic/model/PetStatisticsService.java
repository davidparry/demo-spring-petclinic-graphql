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
package org.springframework.samples.petclinic.model;

import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.ServiceRequestRepository;
import org.springframework.samples.petclinic.rest.dto.PetTypeCountDTO;
import org.springframework.samples.petclinic.rest.dto.TopServiceDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for pet statistics operations.
 * Provides methods to calculate pet distribution by type and top services per pet type.
 *
 * @author Agent
 */
@Service
@Validated
@Transactional(readOnly = true)
public class PetStatisticsService {

    private final PetRepository petRepository;
    private final ServiceRequestRepository serviceRequestRepository;

    public PetStatisticsService(PetRepository petRepository,
                                ServiceRequestRepository serviceRequestRepository) {
        this.petRepository = petRepository;
        this.serviceRequestRepository = serviceRequestRepository;
    }

    /**
     * Calculate the count of pets grouped by pet type.
     *
     * @return List of PetTypeCountDTO containing pet type and count
     */
    public List<PetTypeCountDTO> getPetCountByType() {
        return petRepository.findAll().stream()
            .collect(Collectors.groupingBy(
                pet -> pet.getType().getName(),
                Collectors.counting()
            ))
            .entrySet().stream()
            .map(entry -> new PetTypeCountDTO(entry.getKey(), entry.getValue()))
            .sorted(Comparator.comparing(PetTypeCountDTO::getPetType))
            .collect(Collectors.toList());
    }

    /**
     * Calculate the top 3 most requested services for each pet type.
     *
     * @return List of TopServiceDTO containing pet type and list of top services
     */
    public List<TopServiceDTO> getTopServiceByPetType() {
        List<Object[]> results = serviceRequestRepository.findTopServicesByPetType();
        Map<String, List<String>> topServicesMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            String petType = (String) row[0];
            String serviceName = (String) row[1];

            topServicesMap.computeIfAbsent(petType, k -> new ArrayList<>());

            // Keep only top 3 services per pet type
            if (topServicesMap.get(petType).size() < 3) {
                topServicesMap.get(petType).add(serviceName);
            }
        }

        return topServicesMap.entrySet().stream()
            .map(entry -> new TopServiceDTO(entry.getKey(), entry.getValue()))
            .sorted(Comparator.comparing(TopServiceDTO::getPetType))
            .collect(Collectors.toList());
    }
}
