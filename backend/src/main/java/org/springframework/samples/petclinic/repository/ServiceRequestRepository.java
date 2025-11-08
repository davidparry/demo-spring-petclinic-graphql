package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.ServiceRequest;
import org.springframework.samples.petclinic.rest.dto.PetTypeCountDTO;
import org.springframework.samples.petclinic.rest.dto.TopServiceDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for ServiceRequest.
 * Provides CRUD operations and custom statistical queries.
 */
@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Integer> {

    /**
     * Find pet count grouped by pet type.
     * Returns the distribution of pets across different types.
     *
     * @return List of PetTypeCountDTO containing pet type name and count
     */
    @Query("SELECT new org.springframework.samples.petclinic.rest.dto.PetTypeCountDTO(" +
           "p.type.name, COUNT(p)) " +
           "FROM Pet p " +
           "GROUP BY p.type.id, p.type.name " +
           "ORDER BY COUNT(p) DESC, p.type.name")
    List<PetTypeCountDTO> findPetCountByType();

    /**
     * Find top requested services grouped by pet type.
     * Returns all services with their request counts per pet type,
     * ordered to facilitate finding the top service per type.
     *
     * @return List of TopServiceDTO containing pet type, service name, and request count
     */
    @Query("SELECT new org.springframework.samples.petclinic.rest.dto.TopServiceDTO(" +
           "p.type.name, ss.name, COUNT(sr)) " +
           "FROM ServiceRequest sr " +
           "JOIN sr.pet p " +
           "JOIN sr.specialtyService ss " +
           "GROUP BY p.type.id, p.type.name, ss.id, ss.name " +
           "ORDER BY p.type.name, COUNT(sr) DESC, ss.name")
    List<TopServiceDTO> findTopServicesByPetType();
}
