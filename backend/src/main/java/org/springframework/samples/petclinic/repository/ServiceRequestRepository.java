/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.ServiceRequest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for <code>ServiceRequest</code> domain objects
 * Includes custom queries for statistical analysis.
 *
 * @author Agent
 */
public interface ServiceRequestRepository extends Repository<ServiceRequest, Integer> {

    /**
     * Retrieve a <code>ServiceRequest</code> from the data store by id.
     *
     * @param id the id to search for
     * @return the <code>ServiceRequest</code> if found
     */
    Optional<ServiceRequest> findById(Integer id);

    /**
     * Save a <code>ServiceRequest</code> to the data store.
     *
     * @param serviceRequest the <code>ServiceRequest</code> to save
     */
    void save(ServiceRequest serviceRequest);

    /**
     * Retrieve all <code>ServiceRequest</code>s from the data store.
     *
     * @return a <code>Collection</code> of <code>ServiceRequest</code>s
     */
    Collection<ServiceRequest> findAll();

    /**
     * Find top services by pet type using native SQL for complex aggregation.
     * Returns pet type name, service name, and request count ordered by pet type and count.
     *
     * @return List of Object arrays containing [petType, serviceName, requestCount]
     */
    @Query(value = "SELECT pt.name as pet_type, ss.name as service_name, COUNT(*) as request_count " +
           "FROM service_requests sr " +
           "JOIN pets p ON sr.pet_id = p.id " +
           "JOIN types pt ON p.type_id = pt.id " +
           "JOIN specialty_services ss ON sr.specialty_service_id = ss.id " +
           "GROUP BY pt.name, ss.name " +
           "ORDER BY pt.name, request_count DESC",
           nativeQuery = true)
    List<Object[]> findTopServicesByPetType();
}
