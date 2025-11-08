package org.springframework.samples.petclinic.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.samples.petclinic.graphql.PetTypeCountDTO;
import org.springframework.samples.petclinic.graphql.TopServiceDTO;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for retrieving pet statistics
 *
 * @author Agent
 */
@Service
@Validated
@Transactional(readOnly = true)
public class PetStatisticsService {

    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;
    private final SpecialtyRepository specialtyRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public PetStatisticsService(PetRepository petRepository, 
                                PetTypeRepository petTypeRepository,
                                SpecialtyRepository specialtyRepository) {
        this.petRepository = petRepository;
        this.petTypeRepository = petTypeRepository;
        this.specialtyRepository = specialtyRepository;
    }

    /**
     * Get pet count by type
     * @return List of pet type counts
     */
    public List<PetTypeCountDTO> getPetCountByType() {
        String jpql = "SELECT p.type, COUNT(p) FROM Pet p GROUP BY p.type ORDER BY p.type.name";
        
        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createQuery(jpql).getResultList();
        
        List<PetTypeCountDTO> dtos = new ArrayList<>();
        for (Object[] result : results) {
            PetType petType = (PetType) result[0];
            Long count = (Long) result[1];
            dtos.add(new PetTypeCountDTO(petType, count));
        }
        
        return dtos;
    }

    /**
     * Get top requested specialty services per pet type
     * Service requests are visits linked to vets with specialties
     * @return List of top services by pet type
     */
    public List<TopServiceDTO> getTopServiceByPetType() {
        // Native SQL for complex aggregation with window function to get top specialty per pet type
        String sql = """
            WITH ranked_services AS (
                SELECT 
                    t.id as type_id,
                    s.id as specialty_id,
                    COUNT(*) as request_count,
                    ROW_NUMBER() OVER (PARTITION BY t.id ORDER BY COUNT(*) DESC) as rank
                FROM visits v
                JOIN pets p ON v.pet_id = p.id
                JOIN types t ON p.type_id = t.id
                JOIN vets vt ON v.vet_id = vt.id
                JOIN vet_specialties vs ON vt.id = vs.vet_id
                JOIN specialties s ON vs.specialty_id = s.id
                GROUP BY t.id, s.id
            )
            SELECT type_id, specialty_id, request_count
            FROM ranked_services
            WHERE rank = 1
            ORDER BY type_id
            """;
        
        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(sql).getResultList();
        
        List<TopServiceDTO> dtos = new ArrayList<>();
        for (Object[] result : results) {
            Integer typeId = (Integer) result[0];
            Integer specialtyId = (Integer) result[1];
            Long requestCount = ((Number) result[2]).longValue();
            
            // Fetch the entities
            PetType petType = petTypeRepository.findById(typeId).orElse(null);
            Specialty specialty = specialtyRepository.findById(specialtyId).orElse(null);
            
            if (petType != null && specialty != null) {
                dtos.add(new TopServiceDTO(petType, specialty, requestCount));
            }
        }
        
        return dtos;
    }
}
