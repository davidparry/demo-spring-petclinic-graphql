package org.springframework.samples.petclinic.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * JPA Entity representing a service request for a pet.
 * Tracks which specialty services have been requested for which pets.
 */
@Entity
@Table(name = "service_requests")
public class ServiceRequest extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @NotNull(message = "Pet is required")
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_service_id", nullable = false)
    @NotNull(message = "Specialty service is required")
    private SpecialtyServiceEntity specialtyService;

    @Column(name = "request_date", nullable = false)
    @NotNull(message = "Request date is required")
    private LocalDate requestDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private RequestStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = RequestStatus.PENDING;
        }
    }

    // Getters and Setters

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public SpecialtyServiceEntity getSpecialtyService() {
        return specialtyService;
    }

    public void setSpecialtyService(SpecialtyServiceEntity specialtyService) {
        this.specialtyService = specialtyService;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Enum representing the status of a service request
     */
    public enum RequestStatus {
        PENDING,
        APPROVED,
        COMPLETED,
        CANCELLED
    }
}
