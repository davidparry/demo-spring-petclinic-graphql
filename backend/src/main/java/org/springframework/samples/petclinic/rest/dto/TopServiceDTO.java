package org.springframework.samples.petclinic.rest.dto;

/**
 * DTO for top service by pet type statistics.
 */
public class TopServiceDTO {
    private String petType;
    private String serviceName;
    private Long requestCount;

    public TopServiceDTO() {
    }

    public TopServiceDTO(String petType, String serviceName, Long requestCount) {
        this.petType = petType;
        this.serviceName = serviceName;
        this.requestCount = requestCount;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }
}
