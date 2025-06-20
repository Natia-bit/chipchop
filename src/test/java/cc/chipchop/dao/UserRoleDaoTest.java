package cc.chipchop.dao;

import cc.chipchop.entity.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class UserRoleDaoTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        "postgres:17")
        .withDatabaseName("testbd")
        .withUsername("testuser")
        .withPassword("testpassword")
        .withInitScript("schema.sql");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRoleDao userRoleDao;

    @BeforeEach
    void setUp(){
        jdbcTemplate.update(
            "INSERT INTO users(email, password) VALUES(?,?)",
            "one@example.com","secretpass");
        jdbcTemplate.update(
            "INSERT INTO users(email, password) VALUES(?,?)",
            "two@example.com", "verysecretpass");
        jdbcTemplate.update(
            "INSERT INTO users(email, password) VALUES(?,?)",
            "three@example.com", "supersecretpass");

        jdbcTemplate.update("INSERT INTO user_roles(user_id, role) VALUES(?,?)", 1,"USER");
        jdbcTemplate.update("INSERT INTO user_roles(user_id, role) VALUES(?,?)", 2,"ADMIN");
        jdbcTemplate.update("INSERT INTO user_roles(user_id, role) VALUES(?,?)", 3,"USER");
    }

    @Test
    public void givenFindAll_whenDaoLooksForRecords_thenReturnAllRecords(){
        var userRoles = userRoleDao.findAll();
        assertFalse(userRoles.isEmpty());
        assertNotNull(userRoles);
        assertEquals(3, userRoleDao.findAll().size());

        assertTrue(userRoles.contains(Role.USER));
//
//        assertTrue(roles.contains(Role.USER));
//        assertTrue(roles.contains(Role.ADMIN));
    }

    @Test
    void givenFindAll_whenTableIsEmpty_thenReturnEmptyList(){
        jdbcTemplate.update("DELETE FROM roles");

        var roles = userRoleDao.findAll();

        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }
}
