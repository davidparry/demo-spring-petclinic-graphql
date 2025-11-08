package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.samples.petclinic.PetClinicTestDbConfiguration;
import org.springframework.samples.petclinic.graphql.PetTypeCountDTO;
import org.springframework.samples.petclinic.graphql.TopServiceDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for PetStatisticsService
 *
 * @author Agent
 */
@SpringBootTest
@Import(PetClinicTestDbConfiguration.class)
@Transactional
public class PetStatisticsServiceTests {

    @Autowired
    private PetStatisticsService petStatisticsService;

    @Test
    public void getPetCountByType_shouldReturnNonEmptyList() {
        List<PetTypeCountDTO> results = petStatisticsService.getPetCountByType();
        
        assertThat(results).isNotNull();
        assertThat(results).isNotEmpty();
    }

    @Test
    public void getPetCountByType_shouldReturnValidData() {
        List<PetTypeCountDTO> results = petStatisticsService.getPetCountByType();
        
        for (PetTypeCountDTO dto : results) {
            assertThat(dto.getPetType()).isNotNull();
            assertThat(dto.getPetType().getId()).isNotNull();
            assertThat(dto.getPetType().getName()).isNotBlank();
            assertThat(dto.getCount()).isGreaterThan(0L);
        }
    }

    @Test
    public void getPetCountByType_shouldReturnOrderedByTypeName() {
        List<PetTypeCountDTO> results = petStatisticsService.getPetCountByType();
        
        // Verify results are ordered by pet type name
        for (int i = 1; i < results.size(); i++) {
            String previousName = results.get(i - 1).getPetType().getName();
            String currentName = results.get(i).getPetType().getName();
            assertThat(currentName).isGreaterThanOrEqualTo(previousName);
        }
    }

    @Test
    public void getTopServiceByPetType_shouldReturnValidData() {
        List<TopServiceDTO> results = petStatisticsService.getTopServiceByPetType();
        
        assertThat(results).isNotNull();
        
        // If there are results, validate their structure
        for (TopServiceDTO dto : results) {
            assertThat(dto.getPetType()).isNotNull();
            assertThat(dto.getPetType().getId()).isNotNull();
            assertThat(dto.getPetType().getName()).isNotBlank();
            assertThat(dto.getSpecialty()).isNotNull();
            assertThat(dto.getSpecialty().getId()).isNotNull();
            assertThat(dto.getSpecialty().getName()).isNotBlank();
            assertThat(dto.getRequestCount()).isGreaterThan(0L);
        }
    }

    @Test
    public void getTopServiceByPetType_shouldReturnAtMostOnePerPetType() {
        List<TopServiceDTO> results = petStatisticsService.getTopServiceByPetType();
        
        // Count unique pet types in results
        long uniquePetTypes = results.stream()
            .map(dto -> dto.getPetType().getId())
            .distinct()
            .count();
        
        // Should have at most one result per pet type
        assertThat(results.size()).isEqualTo((int) uniquePetTypes);
    }

    @Test
    public void getTopServiceByPetType_shouldHandleNullGracefully() {
        // This test verifies the service handles edge cases
        // The actual implementation filters out nulls
        List<TopServiceDTO> results = petStatisticsService.getTopServiceByPetType();
        
        assertThat(results).isNotNull();
        assertThat(results).allMatch(dto -> 
            dto.getPetType() != null && dto.getSpecialty() != null
        );
    }
}
