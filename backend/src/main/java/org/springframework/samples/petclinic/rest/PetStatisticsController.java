package org.springframework.samples.petclinic.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.rest.dto.PetTypeCountDTO;
import org.springframework.samples.petclinic.rest.dto.StatisticsResponseDTO;
import org.springframework.samples.petclinic.rest.dto.TopServiceDTO;
import org.springframework.samples.petclinic.service.PetStatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for pet statistics endpoints.
 * Provides endpoints for retrieving pet distribution by type and top services by pet type.
 */
@RestController
@RequestMapping("/api/statistics/pets")
public class PetStatisticsController {

    private static final Logger log = LoggerFactory.getLogger(PetStatisticsController.class);

    private final PetStatisticsService petStatisticsService;

    public PetStatisticsController(PetStatisticsService petStatisticsService) {
        this.petStatisticsService = petStatisticsService;
    }

    /**
     * Get count of service requests grouped by pet type.
     * 
     * @return ResponseEntity with StatisticsResponseDTO containing list of PetTypeCountDTO
     */
    @GetMapping("/by-type")
    public ResponseEntity<StatisticsResponseDTO<List<PetTypeCountDTO>>> getPetsByType() {
        log.info("GET /api/statistics/pets/by-type - Fetching pet count by type");
        
        try {
            List<PetTypeCountDTO> data = petStatisticsService.getPetCountByType();
            
            if (data.isEmpty()) {
                return ResponseEntity.ok(
                    StatisticsResponseDTO.success(data, "No service request data available")
                );
            }
            
            return ResponseEntity.ok(StatisticsResponseDTO.success(data));
            
        } catch (Exception e) {
            log.error("Error fetching pet count by type", e);
            return ResponseEntity.ok(
                StatisticsResponseDTO.error("Error retrieving statistics: " + e.getMessage())
            );
        }
    }

    /**
     * Get the most requested service for each pet type.
     * 
     * @return ResponseEntity with StatisticsResponseDTO containing list of TopServiceDTO
     */
    @GetMapping("/top-services")
    public ResponseEntity<StatisticsResponseDTO<List<TopServiceDTO>>> getTopServices() {
        log.info("GET /api/statistics/pets/top-services - Fetching top services by pet type");
        
        try {
            List<TopServiceDTO> data = petStatisticsService.getTopServiceByPetType();
            
            if (data.isEmpty()) {
                return ResponseEntity.ok(
                    StatisticsResponseDTO.success(data, "No service request data available")
                );
            }
            
            return ResponseEntity.ok(StatisticsResponseDTO.success(data));
            
        } catch (Exception e) {
            log.error("Error fetching top services by pet type", e);
            return ResponseEntity.ok(
                StatisticsResponseDTO.error("Error retrieving statistics: " + e.getMessage())
            );
        }
    }
}
