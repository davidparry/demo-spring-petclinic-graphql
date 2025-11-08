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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void testGetPetCountByType_WithData() {
        // Arrange
        List<Object[]> mockData = Arrays.asList(
            new Object[]{"cat", 5L},
            new Object[]{"dog", 10L}
        );
        when(serviceRequestRepository.countRequestsByPetType()).thenReturn(mockData);

        // Act
        List<PetTypeCountDTO> result = petStatisticsService.getPetCountByType();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("cat", result.get(0).getPetType());
        assertEquals(5L, result.get(0).getCount());
        assertEquals("dog", result.get(1).getPetType());
        assertEquals(10L, result.get(1).getCount());
        verify(serviceRequestRepository, times(1)).countRequestsByPetType();
    }

    @Test
    void testGetPetCountByType_EmptyData() {
        // Arrange
        when(serviceRequestRepository.countRequestsByPetType()).thenReturn(Collections.emptyList());

        // Act
        List<PetTypeCountDTO> result = petStatisticsService.getPetCountByType();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(serviceRequestRepository, times(1)).countRequestsByPetType();
    }

    @Test
    void testGetTopServiceByPetType_WithData() {
        // Arrange
        List<Object[]> mockData = Arrays.asList(
            new Object[]{"cat", "Dental Surgery", 3L},
            new Object[]{"cat", "General Checkup", 2L},  // Should be ignored (not top)
            new Object[]{"dog", "Orthopedic Surgery", 5L}
        );
        when(serviceRequestRepository.findTopServicesByPetType()).thenReturn(mockData);

        // Act
        List<TopServiceDTO> result = petStatisticsService.getTopServiceByPetType();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Verify cat's top service
        TopServiceDTO catService = result.get(0);
        assertEquals("cat", catService.getPetType());
        assertEquals("Dental Surgery", catService.getServiceName());
        assertEquals(3L, catService.getRequestCount());
        
        // Verify dog's top service
        TopServiceDTO dogService = result.get(1);
        assertEquals("dog", dogService.getPetType());
        assertEquals("Orthopedic Surgery", dogService.getServiceName());
        assertEquals(5L, dogService.getRequestCount());
        
        verify(serviceRequestRepository, times(1)).findTopServicesByPetType();
    }

    @Test
    void testGetTopServiceByPetType_EmptyData() {
        // Arrange
        when(serviceRequestRepository.findTopServicesByPetType()).thenReturn(Collections.emptyList());

        // Act
        List<TopServiceDTO> result = petStatisticsService.getTopServiceByPetType();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(serviceRequestRepository, times(1)).findTopServicesByPetType();
    }
}
