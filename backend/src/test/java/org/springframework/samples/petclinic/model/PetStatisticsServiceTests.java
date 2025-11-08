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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.ServiceRequestRepository;
import org.springframework.samples.petclinic.rest.dto.PetTypeCountDTO;
import org.springframework.samples.petclinic.rest.dto.TopServiceDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test class for PetStatisticsService.
 *
 * @author Agent
 */
@ExtendWith(MockitoExtension.class)
class PetStatisticsServiceTests {

    @Mock
    private PetRepository petRepository;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    private PetStatisticsService petStatisticsService;

    @BeforeEach
    void setUp() {
        petStatisticsService = new PetStatisticsService(petRepository, serviceRequestRepository);
    }

    @Test
    void testGetPetCountByType_WithPets() {
        // Given
        Pet cat1 = createPet("Leo", "cat");
        Pet cat2 = createPet("Samantha", "cat");
        Pet dog1 = createPet("Max", "dog");
        Pet dog2 = createPet("Lucky", "dog");
        Pet dog3 = createPet("Mulligan", "dog");

        when(petRepository.findAll()).thenReturn(Arrays.asList(cat1, cat2, dog1, dog2, dog3));

        // When
        List<PetTypeCountDTO> result = petStatisticsService.getPetCountByType();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPetType()).isEqualTo("cat");
        assertThat(result.get(0).getCount()).isEqualTo(2L);
        assertThat(result.get(1).getPetType()).isEqualTo("dog");
        assertThat(result.get(1).getCount()).isEqualTo(3L);
    }

    @Test
    void testGetPetCountByType_NoPets() {
        // Given
        when(petRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<PetTypeCountDTO> result = petStatisticsService.getPetCountByType();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testGetTopServiceByPetType_WithServices() {
        // Given
        List<Object[]> mockResults = Arrays.asList(
            new Object[]{"cat", "Teeth Cleaning", 5L},
            new Object[]{"cat", "Vaccination", 3L},
            new Object[]{"cat", "Surgery", 1L},
            new Object[]{"dog", "Vaccination", 10L},
            new Object[]{"dog", "Teeth Cleaning", 7L},
            new Object[]{"dog", "Surgery", 2L}
        );
        when(serviceRequestRepository.findTopServicesByPetType()).thenReturn(mockResults);

        // When
        List<TopServiceDTO> result = petStatisticsService.getTopServiceByPetType();

        // Then
        assertThat(result).hasSize(2);
        
        TopServiceDTO catServices = result.get(0);
        assertThat(catServices.getPetType()).isEqualTo("cat");
        assertThat(catServices.getTopServices()).hasSize(3);
        assertThat(catServices.getTopServices().get(0)).isEqualTo("Teeth Cleaning");
        
        TopServiceDTO dogServices = result.get(1);
        assertThat(dogServices.getPetType()).isEqualTo("dog");
        assertThat(dogServices.getTopServices()).hasSize(3);
        assertThat(dogServices.getTopServices().get(0)).isEqualTo("Vaccination");
    }

    @Test
    void testGetTopServiceByPetType_LimitToTop3() {
        // Given - 5 services for one pet type
        List<Object[]> mockResults = Arrays.asList(
            new Object[]{"cat", "Service1", 10L},
            new Object[]{"cat", "Service2", 9L},
            new Object[]{"cat", "Service3", 8L},
            new Object[]{"cat", "Service4", 7L},
            new Object[]{"cat", "Service5", 6L}
        );
        when(serviceRequestRepository.findTopServicesByPetType()).thenReturn(mockResults);

        // When
        List<TopServiceDTO> result = petStatisticsService.getTopServiceByPetType();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTopServices()).hasSize(3);
        assertThat(result.get(0).getTopServices()).containsExactly("Service1", "Service2", "Service3");
    }

    @Test
    void testGetTopServiceByPetType_NoServices() {
        // Given
        when(serviceRequestRepository.findTopServicesByPetType()).thenReturn(Collections.emptyList());

        // When
        List<TopServiceDTO> result = petStatisticsService.getTopServiceByPetType();

        // Then
        assertThat(result).isEmpty();
    }

    private Pet createPet(String name, String typeName) {
        Pet pet = new Pet();
        pet.setName(name);
        PetType type = new PetType();
        type.setName(typeName);
        pet.setType(type);
        return pet;
    }
}
