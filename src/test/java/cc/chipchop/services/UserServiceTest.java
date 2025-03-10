package cc.chipchop.services;

import cc.chipchop.dao.UserDao;
import cc.chipchop.entity.User;
import cc.chipchop.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void serUp()
    {
        when(userDao.findById(1)).thenReturn(Optional.empty());
        when(userDao.findById(2)).thenReturn(Optional.of(new User(2, "bob@test.com", "secretpass")));
        when(userDao.findById(3)).thenReturn(Optional.of(new User(3, "alice@test.com", "supersecretpass")));
        when(userDao.findById(4)).thenReturn(Optional.of(new User(4, "alex@test.com", "topsecretpass")));
    }
    @AfterEach
    public void tearDown(){
        Mockito.reset(userDao);
    }

    @Test
    public void givenFindAll_whenDaoReturnsMultipleRecords_ThenReturnMultipleRecords(){
        List<User> expected = new ArrayList<>();
        expected.add(new User(1, "tim@test.com", "123456"));
        expected.add(new User(2, "rob@test.com", "102030"));
        expected.add(new User(3, "maria@test.com", "654321"));

        when(userDao.findAll()).thenReturn(expected);

        var actual = userService.findAll();

        assertEquals(expected, actual);
        verify(userDao, times(1)).findAll();
    }

    @Test
    public void givenFindByID_whenDaoReturnsUserId_thenReturnUserId(){

    }

    @Test
    public void givenFindInvalidId_whenDaoReturnsInvalidId_thenReturnNotFound(){

    }

    @Test
    public void givenInsert_whenDaoInsertsNewUser_thenReturnNewUser(){

    }

    @Test
    public void givenInsertWithExistingEmail_whenDaoInsertsExistingEmail_thenReturnSameUser(){}

    @Test
    public void givenUpdate_whenUpdatingUser_thenReturnUpdatedUser(){

    }

    @Test
    public void givenDelete_whenDeletingExistingUser_thenReturnTrue(){

    }
}
