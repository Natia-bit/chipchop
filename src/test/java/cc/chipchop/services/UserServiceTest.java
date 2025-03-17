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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void serUp() {
        when(userDao.findById(1)).thenReturn(Optional.empty());
        when(userDao.findById(2)).thenReturn(Optional.of(new User(2, "zeus@test.com", "thunderandlightning")));
        when(userDao.findById(3)).thenReturn(Optional.of(new User(3, "hera@test.com", "whereareyouzeus")));
        when(userDao.findById(4)).thenReturn(Optional.of(new User(4, "athena@test.com", "knowlageispower")));
        when(userDao.findByEmail("hadis@test.com")).thenReturn(Optional.of(new User(5, "hadis@test.com", "cerberus")));
    }

    @AfterEach
    public void tearDown(){
        Mockito.reset(userDao);
    }

    @Test
    public void givenFindAll_whenDaoReturnsMultipleRecords_ThenReturnMultipleRecords(){
        List<User> expected = List.of(
            new User(1, "apollo@test.com", "sunisthebest"),
            new User(2, "artemis@test.com", "afkgoneforhunting"),
            new User(3, "hephaestus@test.com", "aphrodite")
        );

        when(userDao.findAll()).thenReturn(expected);
        var actual = userService.findAll();

        assertEquals(expected, actual);
        verify(userDao, times(1)).findAll();
    }

    @Test
    public void givenFindByID_whenDaoReturnsUserId_thenReturnUserId(){
        var result = userService.findById(2);
        assertTrue(result.isPresent());

        verify(userDao, times(1)).findById(2);
    }

    @Test
    public void givenFindInvalidId_whenDaoReturnsInvalidId_thenReturnNotFound(){
        assertThrows(ResponseStatusException.class, () -> userService.findById(1));
        verify(userDao, times(1)).findById(1);
    }

    @Test
    public void givenFindByEmail_whenDaoReturnsFindByEmail_thenReturnUser(){
        var result = userService.findByEmail("hadis@test.com");
        assertTrue(result.isPresent());

        verify(userDao, times(1)).findByEmail("hadis@test.com");
    }

    @Test
    public void givenFindByInvalidEmail_whenDaoReturnsFindByEmail_thenReturnNotFound(){
        when(userDao.findByEmail("hermes@test.com")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.findByEmail("hermes@test.com"));
        verify(userDao, times(1)).findByEmail("hermes@test.com");
    }

    @Test
    public void givenInsert_whenDaoInsertsNewUser_thenReturnNewUser(){
        User user = new User(10, "poseidon@test.com", "sevenseas");
        when(userDao.findByEmail("poseidon@test.com")).thenReturn(Optional.empty());

        userService.insert(user);
        verify(userDao, times(1)).insert(user);
    }

    @Test
    public void givenInsert_whenDaoInsertsInvalidUser_thenReturnResponseStatusException(){
        User user = new User(1, "hadis@test.com", "underworld");

        assertThrows(ResponseStatusException.class, () -> userService.insert(user));
        verify(userDao, times(1)).findByEmail("hadis@test.com");
    }

    @Test
    public void givenUpdate_whenUpdatingUser_thenReturnUpdatedUser(){
        var updatedUser = new User(3, "hera@test.com", "updatedpassword");

        userService.update(3, updatedUser);
        verify(userDao, times(1)).update(3, updatedUser);
    }

    @Test
    public void givenDelete_whenDeletingExistingUser_thenReturnTrue(){
        assertTrue(userService.delete(3));
        verify(userDao, times(1)).delete(3);
    }
}
