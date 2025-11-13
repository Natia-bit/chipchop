package cc.chipchop.services;

import cc.chipchop.dao.UserRoleDao;
import cc.chipchop.entity.Role;
import cc.chipchop.entity.UserRole;
import cc.chipchop.service.UserRoleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRoleServiceTest {

    @Mock
    private UserRoleDao userRoleDao;

    @InjectMocks
    private UserRoleService userRoleService;

    @BeforeEach
    public void setUp(){
        var result = List.of(
            new UserRole(1, Role.USER),
            new UserRole(2, Role.ADMIN));
        when(userRoleDao.findAll()).thenReturn(result);
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(userRoleDao);
    }



    @Test
    void givenFindAll_whenRolesExist_thenReturnRolesFromDao(){
        List<UserRole> result = userRoleService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(userRole -> userRole.role() == Role.USER));
        assertTrue(result.stream().anyMatch(userRole -> userRole.role() == Role.ADMIN));

        verify(userRoleDao, times(1)).findAll();
        verifyNoMoreInteractions(userRoleDao);
    }

    @Test
    void givenFindAll_whenRolesExist_thenConfirmResultContainsRoles(){
        List<UserRole> result = userRoleService.findAll();

        var roles = result.stream().map(UserRole::role).toList();
        assertTrue(roles.contains(Role.USER));
        assertTrue(roles.contains(Role.ADMIN));

        verify(userRoleDao, times(1)).findAll();
        verifyNoMoreInteractions(userRoleDao);
    }

    @Test
    void givenFindAll_whenNoRolesExist_thenReturnEmptyList(){
        when(userRoleDao.findAll()).thenReturn(Collections.emptyList());

        List<UserRole> result = userRoleService.findAll();
        assertTrue(result.isEmpty());

        verify(userRoleDao, times(1)).findAll();
        verifyNoMoreInteractions(userRoleDao);
    }

}
