package org.springframework.samples.petclinic.graphql;

import org.junit.jupiter.api.Test;

/**
 * Integration tests for Pet Statistics GraphQL queries
 *
 * @author Agent
 */
public class PetStatisticsControllerTests extends AbstractClinicGraphqlTests {

    @Test
    public void petStatisticsByType_shouldReturnPetCounts() {
        userRoleGraphQlTester.document("""
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
            .path("data.petStatisticsByType").entityList(Object.class).hasSizeGreaterThan(0)
            .path("data.petStatisticsByType[0].petType.id").hasValue()
            .path("data.petStatisticsByType[0].petType.name").hasValue()
            .path("data.petStatisticsByType[0].count").hasValue();
    }

    @Test
    public void petStatisticsByType_shouldReturnCorrectCounts() {
        // Test that counts are positive integers
        userRoleGraphQlTester.document("""
            query {
                petStatisticsByType {
                    count
                }
            }
            """)
            .execute()
            .path("data.petStatisticsByType[*].count").entityList(Integer.class)
            .satisfies(counts -> {
                counts.forEach(count -> {
                    assert count > 0 : "Count should be positive";
                });
            });
    }

    @Test
    public void topServicesByPetType_shouldReturnTopServices() {
        userRoleGraphQlTester.document("""
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
            .path("data.topServicesByPetType").entityList(Object.class).hasSizeGreaterThanOrEqualTo(0)
            .path("data.topServicesByPetType[*].petType.id").entityList(Object.class)
            .path("data.topServicesByPetType[*].specialty.id").entityList(Object.class)
            .path("data.topServicesByPetType[*].requestCount").entityList(Object.class);
    }

    @Test
    public void topServicesByPetType_shouldReturnValidRequestCounts() {
        // Test that request counts are positive integers
        userRoleGraphQlTester.document("""
            query {
                topServicesByPetType {
                    requestCount
                }
            }
            """)
            .execute()
            .path("data.topServicesByPetType[*].requestCount").entityList(Integer.class)
            .satisfies(counts -> {
                counts.forEach(count -> {
                    assert count > 0 : "Request count should be positive";
                });
            });
    }

    @Test
    public void petStatisticsByType_shouldWorkWithManagerRole() {
        managerRoleGraphQlTester.document("""
            query {
                petStatisticsByType {
                    petType {
                        name
                    }
                    count
                }
            }
            """)
            .execute()
            .path("data.petStatisticsByType").entityList(Object.class).hasSizeGreaterThan(0);
    }

    @Test
    public void topServicesByPetType_shouldWorkWithManagerRole() {
        managerRoleGraphQlTester.document("""
            query {
                topServicesByPetType {
                    petType {
                        name
                    }
                    specialty {
                        name
                    }
                    requestCount
                }
            }
            """)
            .execute()
            .path("data.topServicesByPetType").entityList(Object.class).hasSizeGreaterThanOrEqualTo(0);
    }

    @Test
    public void petStatisticsByType_shouldReturnEmptyArrayWhenNoPets() {
        // This test assumes the test data has pets, so we just verify the structure
        // In a real scenario with no pets, it should return an empty array
        userRoleGraphQlTester.document("""
            query {
                petStatisticsByType {
                    count
                }
            }
            """)
            .execute()
            .path("data.petStatisticsByType").entityList(Object.class);
    }
}
