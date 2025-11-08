package org.springframework.samples.petclinic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.samples.petclinic.repository.ServiceRequestRepository;
import org.springframework.samples.petclinic.rest.dto.PetTypeCountDTO;
import org.springframework.samples.petclinic.rest.dto.TopServiceDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for retrieving pet statistics.
 * Implements caching for performance optimization.
 */
@Service
@Transactional(readOnly = true)
public class PetStatisticsService {

    private static final Logger log = LoggerFactory.getLogger(PetStatisticsService.class);

    private final ServiceRequestRepository serviceRequestRepository;

    public PetStatisticsService(ServiceRequestRepository serviceRequestRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
    }

    /**
     * Get count of service requests grouped by pet type.
     * Results are cached to improve performance.
     *
     * @return List of PetTypeCountDTO with pet type and count
     */
    @Cacheable(value = "petCountByType", unless = "#result.isEmpty()")
    public List<PetTypeCountDTO> getPetCountByType() {
        log.info("Fetching pet count by type from database");
        
        List<Object[]> results = serviceRequestRepository.countRequestsByPetType();
        
        return results.stream()
            .map(row -> new PetTypeCountDTO(
                (String) row[0],  // petType
                (Long) row[1]     // count
            ))
            .collect(Collectors.toList());
    }

    /**
     * Get the most requested service for each pet type.
     * Results are cached to improve performance.
     * In case of ties, returns the first service alphabetically.
     *
     * @return List of TopServiceDTO with pet type, service name, and request count
     */
    @Cacheable(value = "topServiceByPetType", unless = "#result.isEmpty()")
    public List<TopServiceDTO> getTopServiceByPetType() {
        log.info("Fetching top services by pet type from database");
        
        List<Object[]> results = serviceRequestRepository.findTopServicesByPetType();
        
        // Group by pet type and take the first (top) service for each type
        Map<String, TopServiceDTO> topServicesByType = new LinkedHashMap<>();
        
        for (Object[] row : results) {
            String petType = (String) row[0];
            String serviceName = (String) row[1];
            Long requestCount = (Long) row[2];
            
            // Only add if this pet type hasn't been added yet (first = top due to ORDER BY)
            if (!topServicesByType.containsKey(petType)) {
                topServicesByType.put(petType, new TopServiceDTO(petType, serviceName, requestCount));
            }
        }
        
        return new ArrayList<>(topServicesByType.values());
    }
}
