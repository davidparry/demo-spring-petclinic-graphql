package org.springframework.samples.petclinic.dto;

/**
 * DTO for pet type statistics
 */
public class PetTypeStatistics {
    private String petType;
    private Long count;

    public PetTypeStatistics(String petType, Long count) {
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
