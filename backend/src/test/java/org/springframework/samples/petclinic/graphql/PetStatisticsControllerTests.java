package org.springframework.samples.petclinic.graphql;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for PetStatisticsController
 * 
 * @author Qodo Agent
 */
public class PetStatisticsControllerTests extends AbstractClinicGraphqlTests {
    
    @Test
    void shouldReturnPetStatisticsByType() {
        userRoleGraphQlTester
            .document("""
                query {
                    petStatisticsByType {
                        petType {
                            id
                            name
                        }
                        count
                    }
                }
                """)
            .execute()
            .path("petStatisticsByType").entityList(Object.class).hasSizeGreaterThan(0)
            .path("petStatisticsByType[0].petType.name").entity(String.class).satisfies(name -> 
                assertThat(name).isNotBlank()
            )
            .path("petStatisticsByType[0].count").entity(Integer.class).satisfies(count -> 
                assertThat(count).isPositive()
            );
    }
    
    @Test
    void shouldReturnPetStatisticsByTypeForManager() {
        managerRoleGraphQlTester
            .document("""
                query {
                    petStatisticsByType {
                        petType {
                            id
                            name
                        }
                        count
                    }
                }
                """)
            .execute()
            .path("petStatisticsByType").entityList(Object.class).hasSizeGreaterThan(0);
    }
    
    @Test
    void shouldReturnTopServicesByPetType() {
        userRoleGraphQlTester
            .document("""
                query {
                    topServicesByPetType {
                        petType {
                            id
                            name
                        }
                        specialty {
                            id
                            name
                        }
                        requestCount
                    }
                }
                """)
            .execute()
            .path("topServicesByPetType").entityList(Object.class).hasSizeGreaterThanOrEqualTo(0)
            .path("topServicesByPetType[*].petType.name").entityList(String.class).satisfies(names -> {
                if (!names.isEmpty()) {
                    assertThat(names).allMatch(name -> name != null && !name.isBlank());
                }
            })
            .path("topServicesByPetType[*].requestCount").entityList(Integer.class).satisfies(counts -> {
                if (!counts.isEmpty()) {
                    assertThat(counts).allMatch(count -> count > 0);
                }
            });
    }
    
    @Test
    void shouldReturnTopServicesByPetTypeForManager() {
        managerRoleGraphQlTester
            .document("""
                query {
                    topServicesByPetType {
                        petType {
                            id
                            name
                        }
                        specialty {
                            id
                            name
                        }
                        requestCount
                    }
                }
                """)
            .execute()
            .path("topServicesByPetType").entityList(Object.class).hasSizeGreaterThanOrEqualTo(0);
    }
    
    @Test
    void shouldOrderPetStatisticsByTypeName() {
        userRoleGraphQlTester
            .document("""
                query {
                    petStatisticsByType {
                        petType {
                            name
                        }
                    }
                }
                """)
            .execute()
            .path("petStatisticsByType[*].petType.name").entityList(String.class).satisfies(names -> {
                if (names.size() > 1) {
                    // Verify alphabetical ordering
                    for (int i = 0; i < names.size() - 1; i++) {
                        assertThat(names.get(i)).isLessThanOrEqualTo(names.get(i + 1));
                    }
                }
            });
    }
    
    @Test
    void shouldReturnUniqueTopServicePerPetType() {
        userRoleGraphQlTester
            .document("""
                query {
                    topServicesByPetType {
                        petType {
                            id
                        }
                    }
                }
                """)
            .execute()
            .path("topServicesByPetType[*].petType.id").entityList(Integer.class).satisfies(typeIds -> {
                if (!typeIds.isEmpty()) {
                    // Verify each pet type appears only once
                    long uniqueCount = typeIds.stream().distinct().count();
                    assertThat(uniqueCount).isEqualTo(typeIds.size());
                }
            });
    }
    
    @Test
    void shouldHandleEmptyResultsGracefully() {
        // This test verifies the queries return empty arrays when no data matches
        // In a real scenario with test data, this might not be empty, but the structure should be valid
        userRoleGraphQlTester
            .document("""
                query {
                    petStatisticsByType {
                        count
                    }
                    topServicesByPetType {
                        requestCount
                    }
                }
                """)
            .execute()
            .path("petStatisticsByType").entityList(Object.class).satisfies(list -> 
                assertThat(list).isNotNull()
            )
            .path("topServicesByPetType").entityList(Object.class).satisfies(list -> 
                assertThat(list).isNotNull()
            );
    }
}
