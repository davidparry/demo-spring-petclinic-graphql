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
package org.springframework.samples.petclinic.rest.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * DTO for top services by pet type statistics.
 *
 * @author Agent
 */
public class TopServiceDTO {

    @NotEmpty
    private String petType;

    @NotNull
    private List<String> topServices;

    public TopServiceDTO() {
    }

    public TopServiceDTO(String petType, List<String> topServices) {
        this.petType = petType;
        this.topServices = topServices;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public List<String> getTopServices() {
        return topServices;
    }

    public void setTopServices(List<String> topServices) {
        this.topServices = topServices;
    }
}
