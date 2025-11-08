package org.springframework.samples.petclinic.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for top service by pet type statistics.
 */
@Schema(description = "Top requested service by pet type")
public class TopServiceDTO {

    @Schema(description = "Pet type name", example = "dog")
    private String petType;

    @Schema(description = "Service name", example = "Dental Cleaning")
    private String serviceName;

    @Schema(description = "Number of requests for this service", example = "25")
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
