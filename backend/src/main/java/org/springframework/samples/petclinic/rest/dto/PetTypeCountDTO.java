package org.springframework.samples.petclinic.rest.dto;

/**
 * DTO for pet count by type statistics.
 */
public class PetTypeCountDTO {
    private String petType;
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
