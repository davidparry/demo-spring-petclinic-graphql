package org.springframework.samples.petclinic.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.dto.ServiceStatistics;
import org.springframework.samples.petclinic.model.PetStatisticsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST controller for pet statistics endpoints
 */
@RestController
@RequestMapping("/api/statistics")
public class PetStatisticsController {

    private static final Logger log = LoggerFactory.getLogger(PetStatisticsController.class);

    private final PetStatisticsService petStatisticsService;

    public PetStatisticsController(PetStatisticsService petStatisticsService) {
        this.petStatisticsService = petStatisticsService;
    }

    /**
     * Get count of pets by type
     * @return Map of pet type to count
     */
    @GetMapping("/pets/by-type")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Long>> getPetCountByType() {
        log.info("Fetching pet count by type statistics");
        Map<String, Long> statistics = petStatisticsService.getPetCountByType();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get top requested services by pet type
     * @return Map of pet type to list of service statistics
     */
    @GetMapping("/services/top-by-pet-type")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, List<ServiceStatistics>>> getTopServicesByPetType() {
        log.info("Fetching top services by pet type statistics");
        Map<String, List<ServiceStatistics>> statistics = petStatisticsService.getTopServiceByPetType();
        return ResponseEntity.ok(statistics);
    }
}
