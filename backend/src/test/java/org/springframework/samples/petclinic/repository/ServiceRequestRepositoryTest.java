package org.springframework.samples.petclinic.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.rest.dto.PetTypeCountDTO;
import org.springframework.samples.petclinic.rest.dto.TopServiceDTO;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository tests for ServiceRequestRepository.
 * Tests custom JPQL queries for statistics.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ServiceRequestRepositoryTest {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Test
    void findPetCountByType_shouldReturnPetCounts() {
        // When
        List<PetTypeCountDTO> results = serviceRequestRepository.findPetCountByType();

        // Then
        assertThat(results).isNotNull();
        // Should have at least some pet types from the sample data
        assertThat(results).isNotEmpty();
        
        // Verify structure
        if (!results.isEmpty()) {
            PetTypeCountDTO first = results.get(0);
            assertThat(first.getPetType()).isNotNull();
            assertThat(first.getCount()).isGreaterThan(0);
        }
    }

    @Test
    void findTopServicesByPetType_shouldReturnServices() {
        // When
        List<TopServiceDTO> results = serviceRequestRepository.findTopServicesByPetType();

        // Then
        assertThat(results).isNotNull();
        
        // Verify structure if data exists
        if (!results.isEmpty()) {
            TopServiceDTO first = results.get(0);
            assertThat(first.getPetType()).isNotNull();
            assertThat(first.getServiceName()).isNotNull();
            assertThat(first.getRequestCount()).isGreaterThan(0);
        }
    }
}
