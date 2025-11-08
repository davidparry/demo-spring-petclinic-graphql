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

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.SpecialtyServiceEntity;

import java.util.Collection;
import java.util.Optional;

/**
 * Repository class for <code>SpecialtyServiceEntity</code> domain objects
 *
 * @author Agent
 */
public interface SpecialtyServiceRepository extends Repository<SpecialtyServiceEntity, Integer> {

    /**
     * Retrieve a <code>SpecialtyServiceEntity</code> from the data store by id.
     *
     * @param id the id to search for
     * @return the <code>SpecialtyServiceEntity</code> if found
     */
    Optional<SpecialtyServiceEntity> findById(Integer id);

    /**
     * Save a <code>SpecialtyServiceEntity</code> to the data store.
     *
     * @param specialtyService the <code>SpecialtyServiceEntity</code> to save
     */
    void save(SpecialtyServiceEntity specialtyService);

    /**
     * Retrieve all <code>SpecialtyServiceEntity</code>s from the data store.
     *
     * @return a <code>Collection</code> of <code>SpecialtyServiceEntity</code>s
     */
    Collection<SpecialtyServiceEntity> findAll();
}
