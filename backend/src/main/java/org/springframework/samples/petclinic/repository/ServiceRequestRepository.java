package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.ServiceRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for ServiceRequest with custom statistical queries.
 */
@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Integer> {

    /**
     * Count service requests grouped by pet type.
     * Returns array of [petTypeName, count]
     */
    @Query("SELECT p.type.name as petType, COUNT(sr) as count " +
           "FROM ServiceRequest sr " +
           "JOIN sr.pet p " +
           "GROUP BY p.type.name " +
           "ORDER BY p.type.name")
    List<Object[]> countRequestsByPetType();

    /**
     * Find top requested services by pet type.
     * Returns array of [petTypeName, serviceName, requestCount]
     * Ordered by pet type and count descending to get top service per type.
     */
    @Query("SELECT p.type.name as petType, ss.name as serviceName, COUNT(sr) as requestCount " +
           "FROM ServiceRequest sr " +
           "JOIN sr.pet p " +
           "JOIN sr.specialtyService ss " +
           "GROUP BY p.type.name, ss.name " +
           "ORDER BY p.type.name, COUNT(sr) DESC, ss.name")
    List<Object[]> findTopServicesByPetType();
}
