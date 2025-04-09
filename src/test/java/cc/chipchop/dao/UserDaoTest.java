package cc.chipchop.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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
    private UserDao userDao;

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
        fail("Not yet implemented");
    }

    @Test
    public void givenInsert_whenDaoAddsNewUser_thenReturnNumberOfRowsChanged(){
        fail("Not yet implemented");
    }

    @Test
    public void givenInsert_whenDaoAddsNewUserWithSameEmail_thenReturnError(){
        fail("Not yet implemented");
    }

    @Test
    public void givenUpdate_whenDaoUpdatesUser_thenReturnOne(){
        fail("Not yet implemented");
    }

    @Test
    public void givenUpdate_whenDaoUpdatesInvalidUser_thenReturnError(){
        fail("Not yet implemented");
    }

    @Test
    public void givenDelete_whenDaoDeletesUser_thenReturnOne(){
        fail("Not yet implemented");
    }

    @Test
    public void givenDelete_whenDaoDeletesInvalidUser_thenReturnError(){
        fail("Not yet implemented");
    }
}



