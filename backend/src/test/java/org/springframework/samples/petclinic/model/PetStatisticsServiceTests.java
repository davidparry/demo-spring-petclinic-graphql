package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.dto.ServiceStatistics;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.ServiceRequestRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests for PetStatisticsService
 */
@ExtendWith(MockitoExtension.class)
class PetStatisticsServiceTests {

    @Mock
    private PetRepository petRepository;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @InjectMocks
    private PetStatisticsService petStatisticsService;

    @Test
    void testGetPetCountByType() {
        // Given
        List<Object[]> mockResults = Arrays.asList(
            new Object[]{"cat", 5L},
            new Object[]{"dog", 8L},
            new Object[]{"bird", 3L}
        );
        when(petRepository.countPetsByType()).thenReturn(mockResults);

        // When
        Map<String, Long> result = petStatisticsService.getPetCountByType();

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get("cat")).isEqualTo(5L);
        assertThat(result.get("dog")).isEqualTo(8L);
        assertThat(result.get("bird")).isEqualTo(3L);
    }

    @Test
    void testGetPetCountByTypeEmpty() {
        // Given
        when(petRepository.countPetsByType()).thenReturn(Arrays.asList());

        // When
        Map<String, Long> result = petStatisticsService.getPetCountByType();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testGetTopServiceByPetType() {
        // Given
        List<Object[]> mockResults = Arrays.asList(
            new Object[]{"cat", "radiology", 10L},
            new Object[]{"cat", "surgery", 5L},
            new Object[]{"dog", "dentistry", 8L},
            new Object[]{"dog", "radiology", 3L}
        );
        when(serviceRequestRepository.getTopServicesByPetType()).thenReturn(mockResults);

        // When
        Map<String, List<ServiceStatistics>> result = petStatisticsService.getTopServiceByPetType();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get("cat")).hasSize(2);
        assertThat(result.get("cat").get(0).getServiceName()).isEqualTo("radiology");
        assertThat(result.get("cat").get(0).getRequestCount()).isEqualTo(10L);
        assertThat(result.get("dog")).hasSize(2);
        assertThat(result.get("dog").get(0).getServiceName()).isEqualTo("dentistry");
        assertThat(result.get("dog").get(0).getRequestCount()).isEqualTo(8L);
    }

    @Test
    void testGetTopServiceByPetTypeEmpty() {
        // Given
        when(serviceRequestRepository.getTopServicesByPetType()).thenReturn(Arrays.asList());

        // When
        Map<String, List<ServiceStatistics>> result = petStatisticsService.getTopServiceByPetType();

        // Then
        assertThat(result).isEmpty();
    }
}
