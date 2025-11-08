package org.springframework.samples.petclinic.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * REST controller for pet statistics endpoints.
 * Provides endpoints for retrieving pet distribution and service request statistics.
 */
@RestController
@RequestMapping("/api/statistics/pets")
@Tag(name = "Pet Statistics", description = "Pet statistics endpoints for retrieving pet distribution and service request data")
public class PetStatisticsController {

    private final PetStatisticsService petStatisticsService;

    public PetStatisticsController(PetStatisticsService petStatisticsService) {
        this.petStatisticsService = petStatisticsService;
    }

    /**
     * Get pet distribution by type.
     * Returns the count of pets grouped by their type.
     *
     * @return ResponseEntity containing statistics response with pet counts by type
     */
    @GetMapping("/by-type")
    @Operation(
        summary = "Get pet distribution by type",
        description = "Returns the count of pets grouped by their type, ordered by count descending"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved pet distribution",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = StatisticsResponseDTO.class)
            )
        )
    })
    public ResponseEntity<StatisticsResponseDTO<PetTypeCountDTO>> getPetsByType() {
        List<PetTypeCountDTO> petCounts = petStatisticsService.getPetCountByType();
        StatisticsResponseDTO<PetTypeCountDTO> response = new StatisticsResponseDTO<>(petCounts);
        return ResponseEntity.ok(response);
    }

    /**
     * Get top requested services by pet type.
     * Returns the most requested specialty service for each pet type.
     *
     * @return ResponseEntity containing statistics response with top services by pet type
     */
    @GetMapping("/top-services")
    @Operation(
        summary = "Get top requested services by pet type",
        description = "Returns the most requested specialty service for each pet type. In case of ties, the first service alphabetically is returned."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved top services",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = StatisticsResponseDTO.class)
            )
        )
    })
    public ResponseEntity<StatisticsResponseDTO<TopServiceDTO>> getTopServices() {
        List<TopServiceDTO> topServices = petStatisticsService.getTopServiceByPetType();
        StatisticsResponseDTO<TopServiceDTO> response = new StatisticsResponseDTO<>(topServices);
        return ResponseEntity.ok(response);
    }
}
