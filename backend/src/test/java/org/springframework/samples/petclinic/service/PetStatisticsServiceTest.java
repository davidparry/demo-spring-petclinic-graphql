package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.repository.ServiceRequestRepository;
import org.springframework.samples.petclinic.rest.dto.PetTypeCountDTO;
import org.springframework.samples.petclinic.rest.dto.TopServiceDTO;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests for PetStatisticsService.
 */
@ExtendWith(MockitoExtension.class)
class PetStatisticsServiceTest {

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @InjectMocks
    private PetStatisticsService petStatisticsService;

    @Test
    void getPetCountByType_shouldReturnPetCounts() {
        // Given
        List<PetTypeCountDTO> mockData = Arrays.asList(
            new PetTypeCountDTO("dog", 10L),
            new PetTypeCountDTO("cat", 8L),
            new PetTypeCountDTO("bird", 3L)
        );
        when(serviceRequestRepository.findPetCountByType()).thenReturn(mockData);

        // When
        List<PetTypeCountDTO> result = petStatisticsService.getPetCountByType();

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getPetType()).isEqualTo("dog");
        assertThat(result.get(0).getCount()).isEqualTo(10L);
    }

    @Test
    void getPetCountByType_shouldReturnEmptyListWhenNoData() {
        // Given
        when(serviceRequestRepository.findPetCountByType()).thenReturn(null);

        // When
        List<PetTypeCountDTO> result = petStatisticsService.getPetCountByType();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void getTopServiceByPetType_shouldReturnTopServices() {
        // Given
        List<TopServiceDTO> mockData = Arrays.asList(
            new TopServiceDTO("dog", "Dental Cleaning", 15L),
            new TopServiceDTO("dog", "Surgery Consultation", 10L),
            new TopServiceDTO("cat", "Radiology Scan", 12L),
            new TopServiceDTO("cat", "Dental Cleaning", 8L)
        );
        when(serviceRequestRepository.findTopServicesByPetType()).thenReturn(mockData);

        // When
        List<TopServiceDTO> result = petStatisticsService.getTopServiceByPetType();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(TopServiceDTO::getPetType).containsExactlyInAnyOrder("dog", "cat");
        
        TopServiceDTO dogService = result.stream()
            .filter(s -> s.getPetType().equals("dog"))
            .findFirst()
            .orElseThrow();
        assertThat(dogService.getServiceName()).isEqualTo("Dental Cleaning");
        assertThat(dogService.getRequestCount()).isEqualTo(15L);
    }

    @Test
    void getTopServiceByPetType_shouldReturnEmptyListWhenNoData() {
        // Given
        when(serviceRequestRepository.findTopServicesByPetType()).thenReturn(null);

        // When
        List<TopServiceDTO> result = petStatisticsService.getTopServiceByPetType();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void getTopServiceByPetType_shouldHandleTiesBySelectingFirst() {
        // Given - two services with same count for same pet type
        List<TopServiceDTO> mockData = Arrays.asList(
            new TopServiceDTO("dog", "Dental Cleaning", 10L),
            new TopServiceDTO("dog", "Surgery Consultation", 10L)
        );
        when(serviceRequestRepository.findTopServicesByPetType()).thenReturn(mockData);

        // When
        List<TopServiceDTO> result = petStatisticsService.getTopServiceByPetType();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getServiceName()).isEqualTo("Dental Cleaning");
    }
}
