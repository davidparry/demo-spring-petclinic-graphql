package org.springframework.samples.petclinic.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for pet count by type statistics.
 */
@Schema(description = "Pet count by type")
public class PetTypeCountDTO {

    @Schema(description = "Pet type name", example = "cat")
    private String petType;

    @Schema(description = "Number of pets of this type", example = "15")
    private Long count;

    public PetTypeCountDTO() {
    }

    public PetTypeCountDTO(String petType, Long count) {
        this.petType = petType;
        this.count = count;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
