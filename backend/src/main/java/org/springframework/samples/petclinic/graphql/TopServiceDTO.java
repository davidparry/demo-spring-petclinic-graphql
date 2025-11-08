package org.springframework.samples.petclinic.graphql;

import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;

/**
 * DTO for top requested specialty services per pet type
 *
 * @author Agent
 */
public class TopServiceDTO {

    private final PetType petType;
    private final Specialty specialty;
    private final Long requestCount;

    public TopServiceDTO(PetType petType, Specialty specialty, Long requestCount) {
        this.petType = petType;
        this.specialty = specialty;
        this.requestCount = requestCount;
    }

    public PetType getPetType() {
        return petType;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public Long getRequestCount() {
        return requestCount;
    }
}
