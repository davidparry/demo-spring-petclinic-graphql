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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.samples.petclinic.PetClinicTestDbConfiguration;
import org.springframework.samples.petclinic.model.ServiceRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test class for ServiceRequestRepository.
 *
 * @author Agent
 */
@SpringBootTest
@Transactional
@Import(PetClinicTestDbConfiguration.class)
class ServiceRequestRepositoryTests {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Test
    void testFindAll() {
        // When
        Collection<ServiceRequest> serviceRequests = serviceRequestRepository.findAll();

        // Then
        assertThat(serviceRequests).isNotEmpty();
    }

    @Test
    void testFindTopServicesByPetType() {
        // When
        List<Object[]> results = serviceRequestRepository.findTopServicesByPetType();

        // Then
        assertThat(results).isNotEmpty();
        
        // Verify structure of results
        Object[] firstRow = results.get(0);
        assertThat(firstRow).hasSize(3);
        assertThat(firstRow[0]).isInstanceOf(String.class); // pet type
        assertThat(firstRow[1]).isInstanceOf(String.class); // service name
        assertThat(firstRow[2]).isNotNull(); // count
    }

    @Test
    void testFindTopServicesByPetType_OrderedByPetTypeAndCount() {
        // When
        List<Object[]> results = serviceRequestRepository.findTopServicesByPetType();

        // Then
        assertThat(results).isNotEmpty();
        
        // Verify ordering - should be ordered by pet type first
        String previousPetType = null;
        Long previousCount = null;
        
        for (Object[] row : results) {
            String currentPetType = (String) row[0];
            Long currentCount = ((Number) row[2]).longValue();
            
            if (previousPetType != null) {
                if (currentPetType.equals(previousPetType)) {
                    // Same pet type - count should be descending
                    assertThat(currentCount).isLessThanOrEqualTo(previousCount);
                }
            }
            
            previousPetType = currentPetType;
            previousCount = currentCount;
        }
    }
}
