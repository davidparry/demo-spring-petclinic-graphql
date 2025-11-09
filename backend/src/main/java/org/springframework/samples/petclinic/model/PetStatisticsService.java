package org.springframework.samples.petclinic.model;

import org.springframework.samples.petclinic.dto.ServiceStatistics;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.ServiceRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for retrieving pet statistics
 */
@Service
@Validated
@Transactional(readOnly = true)
public class PetStatisticsService {
    
    private final PetRepository petRepository;
    private final ServiceRequestRepository serviceRequestRepository;

    public PetStatisticsService(PetRepository petRepository, ServiceRequestRepository serviceRequestRepository) {
        this.petRepository = petRepository;
        this.serviceRequestRepository = serviceRequestRepository;
    }

    /**
     * Get count of pets by type
     * @return Map of pet type name to count
     */
    public Map<String, Long> getPetCountByType() {
        List<Object[]> results = petRepository.countPetsByType();
        Map<String, Long> statistics = new LinkedHashMap<>();
        
        for (Object[] result : results) {
            String typeName = (String) result[0];
            Long count = (Long) result[1];
            statistics.put(typeName, count);
        }
        
        return statistics;
    }

    /**
     * Get top requested services by pet type
     * @return Map of pet type to list of service statistics
     */
    public Map<String, List<ServiceStatistics>> getTopServiceByPetType() {
        List<Object[]> results = serviceRequestRepository.getTopServicesByPetType();
        Map<String, List<ServiceStatistics>> statistics = new LinkedHashMap<>();
        
        for (Object[] result : results) {
            String petType = (String) result[0];
            String serviceName = (String) result[1];
            Long requestCount = ((Number) result[2]).longValue();
            
            statistics.computeIfAbsent(petType, k -> new ArrayList<>())
                     .add(new ServiceStatistics(serviceName, requestCount));
        }
        
        return statistics;
    }
}
