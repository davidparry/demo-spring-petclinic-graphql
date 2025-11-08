package org.springframework.samples.petclinic.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a service request made by an owner for their pet.
 * Tracks which specialty services are requested for which pets.
 */
@Entity
@Table(name = "service_requests")
public class ServiceRequest extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "specialty_service_id", nullable = false)
    private SpecialtyServiceEntity specialtyService;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @Column(name = "status", length = 50)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        if (requestDate == null) {
            requestDate = LocalDateTime.now();
        }
        if (status == null) {
            status = RequestStatus.PENDING;
        }
    }

    /**
     * Enum for service request status
     */
    public enum RequestStatus {
        PENDING,
        COMPLETED,
        CANCELLED
    }
}
