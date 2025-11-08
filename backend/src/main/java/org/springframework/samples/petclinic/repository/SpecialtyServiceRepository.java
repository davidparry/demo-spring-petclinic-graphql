package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.model.SpecialtyServiceEntity;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for SpecialtyServiceEntity.
 * Provides CRUD operations for specialty services.
 */
@Repository
public interface SpecialtyServiceRepository extends JpaRepository<SpecialtyServiceEntity, Integer> {
}
