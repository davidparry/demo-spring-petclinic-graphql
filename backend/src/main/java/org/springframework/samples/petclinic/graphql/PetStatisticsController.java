package org.springframework.samples.petclinic.graphql;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.samples.petclinic.model.PetStatisticsService;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * GraphQL handler functions for Pet Statistics queries
 *
 * @author Agent
 */
@Controller
public class PetStatisticsController {

    private final PetStatisticsService petStatisticsService;

    public PetStatisticsController(PetStatisticsService petStatisticsService) {
        this.petStatisticsService = petStatisticsService;
    }

    /**
     * GraphQL query to get pet count by type
     * @return List of pet type counts
     */
    @QueryMapping
    public List<PetTypeCountDTO> petStatisticsByType() {
        return petStatisticsService.getPetCountByType();
    }

    /**
     * GraphQL query to get top requested specialty services per pet type
     * @return List of top services by pet type
     */
    @QueryMapping
    public List<TopServiceDTO> topServicesByPetType() {
        return petStatisticsService.getTopServiceByPetType();
    }
}
