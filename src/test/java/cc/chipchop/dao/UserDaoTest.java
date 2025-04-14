package cc.chipchop.dao;

import cc.chipchop.entity.User;
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

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class UserDaoTest {

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
    private UserDao userDao;

    @BeforeEach
    void setUp(){
        jdbcTemplate.update("INSERT INTO users(email, password) VALUES(?,?)", "one@example.com", "secretpass");
        jdbcTemplate.update("INSERT INTO users(email, password) VALUES(?,?)", "two@example.com", "verysecretpass");
        jdbcTemplate.update("INSERT INTO users(email, password) VALUES(?,?)", "three@example.com", "supersecretpass");
    }

    @AfterEach
    void tearDown(){
        jdbcTemplate.execute("TRUNCATE users RESTART IDENTITY");
    }

    @Test
    public void givenFindAll_whenDaoLooksForRecords_thenReturnAllRecords() {
        var users = userDao.findAll();
        assertFalse(users.isEmpty());
        assertEquals(3, userDao.findAll().size());
    }

    @Test
    public void givenFindById_whenDaoLooksForId_thenReturnUser(){
        var user = userDao.findById(1);
        assertTrue(user.isPresent());
        assertEquals("one@example.com", user.get().email());
    }

    @Test
    public void givenFindById_whenDaoLooksForInvalidId_thenReturnEmpty(){
        var user = userDao.findById(10);
        assertTrue(user.isEmpty());
    }

    @Test
    public void givenFindByEmail_whenDaoLooksForEmail_thenReturnUser(){
        var user = userDao.findByEmail("two@example.com");
        assertTrue(user.isPresent());
        assertEquals(2, user.get().id());
    }

    @Test
    public void givenFindByEmail_whenDaoLooksForInvalidEmail_thenReturnEmpty(){
        assertTrue(userDao.findByEmail("fail@example.com").isEmpty());
        assertThrows(NoSuchElementException.class, () -> userDao.findByEmail("fail@example.com").get().email());
    }

    @Test
    public void givenInsert_whenDaoAddsNewUser_thenReturnNewUser(){
        var userFour = new User(4, "four@example.com", "password");
        userDao.insert(userFour);

        assertTrue(userDao.findById(4).isPresent());
        assertNotNull(userDao.findByEmail("four@example.com"));
        assertEquals("four@example.com", userDao.findById(4).get().email());
    }

    @Test
    public void givenInsert_whenDaoAddsNewUserWithSameEmail_thenThrowException(){
        var user = new User(5, "one@example.com", "doesnotmatter" );
        assertThrows(org.springframework.dao.DuplicateKeyException.class, () -> userDao.insert(user));
    }

    @Test
    public void givenUpdate_whenDaoUpdatesUser_thenReturnUpdatedUser(){
       var user = new User(3, "threeUpdated@example.com", "newpassword");
       userDao.update(3, user);

       assertEquals("newpassword", userDao.findById(3).get().password());
       assertEquals("threeUpdated@example.com", userDao.findById(3).get().email());
       assertNotEquals("three@example.com", userDao.findById(3).get().email());
    }

    @Test
    public void givenUpdate_whenDaoUpdatesInvalidUser_thenReturnZero(){
        var user = new User(6, "six@example.com", "coolpassword");
        userDao.update(6, user);

        assertEquals(0, userDao.update(6, user));
        assertFalse(userDao.findByEmail("six@example.com").isPresent());
        assertFalse(userDao.findById(6).isPresent());
    }

    @Test
    public void givenDelete_whenDaoDeletesUser_thenReturnOne(){
        userDao.delete(1);

        assertFalse(userDao.findById(1).isPresent());
        assertEquals(2, userDao.findAll().size());
    }

    @Test
    public void givenDelete_whenDaoDeletesInvalidUser_thenReturnZero(){
        userDao.delete(10);

        assertEquals(0, userDao.delete(10));
        assertFalse(userDao.findById(10).isPresent());
    }
    @Test
    public void testRowMapper(){
        var user = userDao.findById(1);
        var expected = new User(1, "one@example.com", "secretpass");

        assertEquals(Optional.of(expected), user);
    }
}



