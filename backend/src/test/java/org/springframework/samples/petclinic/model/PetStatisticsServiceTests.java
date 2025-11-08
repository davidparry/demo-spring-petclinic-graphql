package org.springframework.samples.petclinic.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.graphql.PetTypeCountDTO;
import org.springframework.samples.petclinic.graphql.TopServiceDTO;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit tests for PetStatisticsService
 * 
 * @author Qodo Agent
 */
@ExtendWith(MockitoExtension.class)
public class PetStatisticsServiceTests {
    
    @Mock
    private EntityManager entityManager;
    
    @Mock
    private PetTypeRepository petTypeRepository;
    
    @Mock
    private SpecialtyRepository specialtyRepository;
    
    @Mock
    private Query query;
    
    private PetStatisticsService service;
    
    @BeforeEach
    void setUp() {
        service = new PetStatisticsService(entityManager, petTypeRepository, specialtyRepository);
    }
    
    @Test
    void shouldReturnPetCountByType() {
        // Given
        PetType catType = new PetType();
        catType.setId(1);
        catType.setName("cat");
        
        PetType dogType = new PetType();
        dogType.setId(2);
        dogType.setName("dog");
        
        List<Object[]> mockResults = Arrays.asList(
            new Object[]{catType, 5L},
            new Object[]{dogType, 10L}
        );
        
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(mockResults);
        
        // When
        List<PetTypeCountDTO> results = service.getPetCountByType();
        
        // Then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).petType().getName()).isEqualTo("cat");
        assertThat(results.get(0).count()).isEqualTo(5L);
        assertThat(results.get(1).petType().getName()).isEqualTo("dog");
        assertThat(results.get(1).count()).isEqualTo(10L);
    }
    
    @Test
    void shouldReturnEmptyListWhenNoPets() {
        // Given
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());
        
        // When
        List<PetTypeCountDTO> results = service.getPetCountByType();
        
        // Then
        assertThat(results).isEmpty();
    }
    
    @Test
    void shouldReturnTopServiceByPetType() {
        // Given
        PetType catType = new PetType();
        catType.setId(1);
        catType.setName("cat");
        
        Specialty surgerySpecialty = new Specialty();
        surgerySpecialty.setId(1);
        surgerySpecialty.setName("surgery");
        
        List<Object[]> mockResults = Arrays.asList(
            new Object[]{1, 1, 15L}  // type_id, specialty_id, count
        );
        
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(mockResults);
        when(petTypeRepository.findById(1)).thenReturn(Optional.of(catType));
        when(specialtyRepository.findById(1)).thenReturn(Optional.of(surgerySpecialty));
        
        // When
        List<TopServiceDTO> results = service.getTopServiceByPetType();
        
        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).petType().getName()).isEqualTo("cat");
        assertThat(results.get(0).specialty().getName()).isEqualTo("surgery");
        assertThat(results.get(0).requestCount()).isEqualTo(15L);
    }
    
    @Test
    void shouldReturnOnlyTopServicePerPetType() {
        // Given
        PetType catType = new PetType();
        catType.setId(1);
        catType.setName("cat");
        
        Specialty surgerySpecialty = new Specialty();
        surgerySpecialty.setId(1);
        surgerySpecialty.setName("surgery");
        
        Specialty radiologySpecialty = new Specialty();
        radiologySpecialty.setId(2);
        radiologySpecialty.setName("radiology");
        
        // Mock results with multiple specialties for same pet type (ordered by count DESC)
        List<Object[]> mockResults = Arrays.asList(
            new Object[]{1, 1, 15L},  // cat, surgery, 15 (top)
            new Object[]{1, 2, 10L}   // cat, radiology, 10 (should be filtered out)
        );
        
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(mockResults);
        when(petTypeRepository.findById(1)).thenReturn(Optional.of(catType));
        when(specialtyRepository.findById(1)).thenReturn(Optional.of(surgerySpecialty));
        
        // When
        List<TopServiceDTO> results = service.getTopServiceByPetType();
        
        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).specialty().getName()).isEqualTo("surgery");
        assertThat(results.get(0).requestCount()).isEqualTo(15L);
    }
    
    @Test
    void shouldReturnEmptyListWhenNoVisits() {
        // Given
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());
        
        // When
        List<TopServiceDTO> results = service.getTopServiceByPetType();
        
        // Then
        assertThat(results).isEmpty();
    }
    
    @Test
    void shouldHandleMultiplePetTypesWithTopServices() {
        // Given
        PetType catType = new PetType();
        catType.setId(1);
        catType.setName("cat");
        
        PetType dogType = new PetType();
        dogType.setId(2);
        dogType.setName("dog");
        
        Specialty surgerySpecialty = new Specialty();
        surgerySpecialty.setId(1);
        surgerySpecialty.setName("surgery");
        
        Specialty dentistrySpecialty = new Specialty();
        dentistrySpecialty.setId(2);
        dentistrySpecialty.setName("dentistry");
        
        List<Object[]> mockResults = Arrays.asList(
            new Object[]{1, 1, 15L},  // cat, surgery, 15
            new Object[]{2, 2, 20L}   // dog, dentistry, 20
        );
        
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(mockResults);
        when(petTypeRepository.findById(1)).thenReturn(Optional.of(catType));
        when(petTypeRepository.findById(2)).thenReturn(Optional.of(dogType));
        when(specialtyRepository.findById(1)).thenReturn(Optional.of(surgerySpecialty));
        when(specialtyRepository.findById(2)).thenReturn(Optional.of(dentistrySpecialty));
        
        // When
        List<TopServiceDTO> results = service.getTopServiceByPetType();
        
        // Then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).petType().getName()).isEqualTo("cat");
        assertThat(results.get(0).specialty().getName()).isEqualTo("surgery");
        assertThat(results.get(1).petType().getName()).isEqualTo("dog");
        assertThat(results.get(1).specialty().getName()).isEqualTo("dentistry");
    }
}
