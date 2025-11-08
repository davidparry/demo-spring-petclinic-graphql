package org.springframework.samples.petclinic.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.graphql.PetTypeCountDTO;
import org.springframework.samples.petclinic.graphql.TopServiceDTO;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for retrieving pet statistics
 * 
 * @author Qodo Agent
 */
@Service
@Transactional(readOnly = true)
public class PetStatisticsService {
    
    private static final Logger log = LoggerFactory.getLogger(PetStatisticsService.class);
    
    private final EntityManager entityManager;
    private final PetTypeRepository petTypeRepository;
    private final SpecialtyRepository specialtyRepository;
    
    public PetStatisticsService(EntityManager entityManager, 
                                PetTypeRepository petTypeRepository,
                                SpecialtyRepository specialtyRepository) {
        this.entityManager = entityManager;
        this.petTypeRepository = petTypeRepository;
        this.specialtyRepository = specialtyRepository;
    }
    
    /**
     * Get pet count grouped by pet type, ordered alphabetically by type name
     * 
     * @return List of PetTypeCountDTO with pet type and count
     */
    public List<PetTypeCountDTO> getPetCountByType() {
        log.debug("Getting pet count by type");
        
        String jpql = """
            SELECT p.type, COUNT(p)
            FROM Pet p
            GROUP BY p.type
            ORDER BY p.type.name
            """;
        
        Query query = entityManager.createQuery(jpql);
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        
        List<PetTypeCountDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            PetType petType = (PetType) row[0];
            Long count = (Long) row[1];
            dtos.add(new PetTypeCountDTO(petType, count));
        }
        
        log.debug("Found {} pet types with counts", dtos.size());
        return dtos;
    }
    
    /**
     * Get top requested specialty service per pet type
     * Uses a window function to get the specialty with the most visits for each pet type
     * 
     * @return List of TopServiceDTO with pet type, specialty, and request count
     */
    public List<TopServiceDTO> getTopServiceByPetType() {
        log.debug("Getting top service by pet type");
        
        String sql = """
            SELECT t.id as type_id, s.id as specialty_id, COUNT(*) as request_count
            FROM visits v
            JOIN pets p ON v.pet_id = p.id
            JOIN types t ON p.type_id = t.id
            JOIN vets vt ON v.vet_id = vt.id
            JOIN vet_specialties vs ON vt.id = vs.vet_id
            JOIN specialties s ON vs.specialty_id = s.id
            GROUP BY t.id, s.id
            ORDER BY t.id, request_count DESC
            """;
        
        Query query = entityManager.createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        
        // Group by pet type and take only the top specialty for each
        List<TopServiceDTO> dtos = new ArrayList<>();
        Integer lastTypeId = null;
        
        for (Object[] row : results) {
            Integer typeId = (Integer) row[0];
            Integer specialtyId = (Integer) row[1];
            Long requestCount = ((Number) row[2]).longValue();
            
            // Only take the first (top) specialty for each pet type
            if (lastTypeId == null || !lastTypeId.equals(typeId)) {
                PetType petType = petTypeRepository.findById(typeId)
                    .orElseThrow(() -> new IllegalStateException("PetType not found: " + typeId));
                Specialty specialty = specialtyRepository.findById(specialtyId)
                    .orElseThrow(() -> new IllegalStateException("Specialty not found: " + specialtyId));
                
                dtos.add(new TopServiceDTO(petType, specialty, requestCount));
                lastTypeId = typeId;
            }
        }
        
        log.debug("Found {} top services by pet type", dtos.size());
        return dtos;
    }
}
