package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.ServiceRequest;

import java.util.List;

/**
 * Repository for ServiceRequest entity with custom statistical queries
 */
public interface ServiceRequestRepository extends CrudRepository<ServiceRequest, Integer> {

    /**
     * Get top services by pet type with request counts
     * Returns: pet_type, service_name, request_count
     */
    @Query(value = "SELECT pt.name as pet_type, s.name as service_name, COUNT(*) as request_count " +
           "FROM service_requests sr " +
           "JOIN pets p ON sr.pet_id = p.id " +
           "JOIN types pt ON p.type_id = pt.id " +
           "JOIN specialties s ON sr.specialty_id = s.id " +
           "GROUP BY pt.name, s.name " +
           "ORDER BY pt.name, request_count DESC",
           nativeQuery = true)
    List<Object[]> getTopServicesByPetType();
}
