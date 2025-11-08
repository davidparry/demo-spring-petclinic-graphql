package org.springframework.samples.petclinic.graphql;

import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;

/**
 * DTO for top requested specialty service per pet type
 * 
 * @param petType The pet type
 * @param specialty The most requested specialty for this pet type
 * @param requestCount The number of visits for this specialty and pet type combination
 */
public record TopServiceDTO(
    PetType petType,
    Specialty specialty,
    Long requestCount
) {
}
