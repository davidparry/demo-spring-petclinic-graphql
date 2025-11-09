package org.springframework.samples.petclinic.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.PetClinicTestDbConfiguration;
import org.springframework.samples.petclinic.graphql.GraphQlTokenProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for PetStatisticsController
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(PetClinicTestDbConfiguration.class)
@Transactional
class PetStatisticsControllerTests extends GraphQlTokenProvider {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetPetCountByTypeWithAuthentication() throws Exception {
        String token = createUserToken();

        mockMvc.perform(get("/api/statistics/pets/by-type")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    void testGetPetCountByTypeWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/statistics/pets/by-type")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetTopServicesByPetTypeWithAuthentication() throws Exception {
        String token = createUserToken();

        mockMvc.perform(get("/api/statistics/services/top-by-pet-type")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    void testGetTopServicesByPetTypeWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/statistics/services/top-by-pet-type")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetPetCountByTypeWithManagerRole() throws Exception {
        String token = createManagerToken();

        mockMvc.perform(get("/api/statistics/pets/by-type")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetTopServicesByPetTypeWithManagerRole() throws Exception {
        String token = createManagerToken();

        mockMvc.perform(get("/api/statistics/services/top-by-pet-type")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
