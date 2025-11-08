package org.springframework.samples.petclinic.graphql;

import org.springframework.samples.petclinic.model.PetType;

/**
 * DTO for pet count by type statistics
 *
 * @author Agent
 */
public class PetTypeCountDTO {

    private final PetType petType;
    private final Long count;

    public PetTypeCountDTO(PetType petType, Long count) {
        this.petType = petType;
        this.count = count;
    }

    public PetType getPetType() {
        return petType;
    }

    public Long getCount() {
        return count;
    }
}
