package cc.chipchop.dao;

import cc.chipchop.entity.Role;
import cc.chipchop.entity.UserRole;
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

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
        jdbcTemplate.update(
            "INSERT INTO users(email, password) VALUES(?,?)",
            "four@example.com", "password");
        jdbcTemplate.update(
            "INSERT INTO users(email, password) VALUES(?,?)",
            "five@example.com", "superpassword");

        jdbcTemplate.update("INSERT INTO user_roles(user_id, role) VALUES(?,?)", 1,"USER");
        jdbcTemplate.update("INSERT INTO user_roles(user_id, role) VALUES(?,?)", 5,"Admin");
        jdbcTemplate.update("INSERT INTO user_roles(user_id, role) VALUES(?,?)", 2,"ADMIN");
        jdbcTemplate.update("INSERT INTO user_roles(user_id, role) VALUES(?,?)", 4,"USER");
        jdbcTemplate.update("INSERT INTO user_roles(user_id, role) VALUES(?,?)", 3,"User");

    }

    @AfterEach
    public void tearDown(){
        jdbcTemplate.execute("TRUNCATE TABLE user_roles, users  RESTART IDENTITY CASCADE");
    }


    @Test
    public void givenFindAll_whenDaoLooksForRecords_thenReturnAllRecords(){
        var actual = userRoleDao.findAll();
        assertFalse(actual.isEmpty());
        assertEquals(5, actual.size());

        var expected = List.of(
            new UserRole(4L, Role.USER),
            new UserRole(1L, Role.USER),
            new UserRole(5L, Role.ADMIN),
            new UserRole(3L, Role.USER),
            new UserRole(2L, Role.ADMIN)
        );

        assertThat(actual)
            .containsExactlyInAnyOrderElementsOf(expected);

        assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    @Test
    void givenFindAll_whenTableIsEmpty_thenReturnEmptyList(){
        jdbcTemplate.update("DELETE FROM user_roles");
        var roles = userRoleDao.findAll();

        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }

    @Test
    void givenFindAll_whenDaoLooksForRecords_thenRolesInRecordsAreCapital() {
        var userRoles = userRoleDao.findAll();

        var result = userRoles.stream().map(
            userRole -> userRole.role().name()
        ).allMatch(roleName -> roleName.equals(roleName.toUpperCase()));

    assertTrue(result);
    }

    @Test
    void givenFindRoleByUserId_whenDaoLooksForId_thenReturnUserRole(){
        var userRole = userRoleDao.findRoleByUserId(1);
        assertTrue(userRole.isPresent());
        assertEquals(1, userRole.get().userId());
    }

    @Test
    void givenFindRoleByUserId_whenDaoLooksForInvalidId_thenReturnEmpty(){
        var userRole = userRoleDao.findRoleByUserId(404);
        assertTrue(userRole.isEmpty());
    }
}
