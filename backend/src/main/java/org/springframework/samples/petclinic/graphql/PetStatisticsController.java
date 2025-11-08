package org.springframework.samples.petclinic.graphql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.samples.petclinic.model.PetStatisticsService;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * GraphQL controller for pet statistics queries
 * 
 * @author Qodo Agent
 */
@Controller
public class PetStatisticsController {
    
    private static final Logger log = LoggerFactory.getLogger(PetStatisticsController.class);
    
    private final PetStatisticsService petStatisticsService;
    
    public PetStatisticsController(PetStatisticsService petStatisticsService) {
        this.petStatisticsService = petStatisticsService;
    }
    
    /**
     * GraphQL query to get pet count by type
     * 
     * @return List of pet type counts ordered alphabetically by type name
     */
    @QueryMapping
    public List<PetTypeCountDTO> petStatisticsByType() {
        log.debug("GraphQL query: petStatisticsByType");
        return petStatisticsService.getPetCountByType();
    }
    
    /**
     * GraphQL query to get top requested specialty service per pet type
     * 
     * @return List of top services by pet type
     */
    @QueryMapping
    public List<TopServiceDTO> topServicesByPetType() {
        log.debug("GraphQL query: topServicesByPetType");
        return petStatisticsService.getTopServiceByPetType();
    }
}
