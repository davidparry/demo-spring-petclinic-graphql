package org.springframework.samples.petclinic.graphql;

import org.springframework.samples.petclinic.model.PetType;

/**
 * DTO for pet count by type statistics
 * 
 * @param petType The pet type
 * @param count The number of pets of this type
 */
public record PetTypeCountDTO(
    PetType petType,
    Long count
) {
}
