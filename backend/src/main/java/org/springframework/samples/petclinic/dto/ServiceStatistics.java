package org.springframework.samples.petclinic.dto;

/**
 * DTO for service statistics
 */
public class ServiceStatistics {
    private String serviceName;
    private Long requestCount;

    public ServiceStatistics(String serviceName, Long requestCount) {
        this.serviceName = serviceName;
        this.requestCount = requestCount;
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
