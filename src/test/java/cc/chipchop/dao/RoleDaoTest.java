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
public class RoleDaoTest {

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
    private RoleDao roleDao;

    @BeforeEach
    void setUp(){
        jdbcTemplate.update("INSERT INTO roles (name) VALUES(?)", "USER");
        jdbcTemplate.update("INSERT INTO roles (name) VALUES(?)", "ADMIN");
    }

    @AfterEach
    void tearDown(){
        jdbcTemplate.execute("TRUNCATE users RESTART IDENTITY");
    }


    @Test
    public void givenFindAll_whenDaoLooksForRecords_thenReturnAllRecords(){
        var roles = roleDao.findAll();
        assertFalse(roles.isEmpty());
        assertNotNull(roles);
        assertEquals(2, roleDao.findAll().size());

        assertTrue(roles.contains(Role.USER));
        assertTrue(roles.contains(Role.ADMIN));
    }

    @Test
    void givenFindAll_whenTableIsEmpty_thenReturnEmptyList(){
        jdbcTemplate.update("DELETE FROM roles");

        var roles = roleDao.findAll();

        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }
}
