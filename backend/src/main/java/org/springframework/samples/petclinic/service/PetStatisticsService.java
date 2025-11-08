package org.springframework.samples.petclinic.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.samples.petclinic.repository.ServiceRequestRepository;
import org.springframework.samples.petclinic.rest.dto.PetTypeCountDTO;
import org.springframework.samples.petclinic.rest.dto.TopServiceDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for retrieving pet statistics.
 * Implements caching for performance optimization.
 */
@Service
@Validated
@Transactional(readOnly = true)
public class PetStatisticsService {

    private final ServiceRequestRepository serviceRequestRepository;

    public PetStatisticsService(ServiceRequestRepository serviceRequestRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
    }

    /**
     * Get pet count grouped by pet type.
     * Results are cached for 5 minutes to ensure sub-500ms response times.
     *
     * @return List of pet counts by type, ordered by count descending
     */
    @Cacheable(value = "petCountByType", key = "'all'")
    public List<PetTypeCountDTO> getPetCountByType() {
        List<PetTypeCountDTO> results = serviceRequestRepository.findPetCountByType();
        return results != null ? results : new ArrayList<>();
    }

    /**
     * Get top requested service for each pet type.
     * Results are cached for 5 minutes to ensure sub-500ms response times.
     * Handles edge cases like ties by selecting the first alphabetically.
     *
     * @return List of top services by pet type
     */
    @Cacheable(value = "topServiceByPetType", key = "'all'")
    public List<TopServiceDTO> getTopServiceByPetType() {
        List<TopServiceDTO> allServices = serviceRequestRepository.findTopServicesByPetType();
        
        if (allServices == null || allServices.isEmpty()) {
            return new ArrayList<>();
        }

        // Group by pet type and select the top service (first one due to ORDER BY)
        Map<String, TopServiceDTO> topServicesByType = new HashMap<>();
        
        for (TopServiceDTO service : allServices) {
            String petType = service.getPetType();
            
            // Only keep the first service for each pet type (highest count due to ORDER BY)
            if (!topServicesByType.containsKey(petType)) {
                topServicesByType.put(petType, service);
            }
        }

        return new ArrayList<>(topServicesByType.values());
    }
}
