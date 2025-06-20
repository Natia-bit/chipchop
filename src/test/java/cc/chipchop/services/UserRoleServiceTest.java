package cc.chipchop.services;

import cc.chipchop.dao.UserRoleDao;
import cc.chipchop.entity.Role;
import cc.chipchop.entity.UserRole;
import cc.chipchop.service.UserRoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRoleServiceTest {

    @Mock
    private UserRoleDao userRoleDao;

    @InjectMocks
    private UserRoleService userRoleService;

    @Test
    void givenFindAll_whenRolesExist_thenReturnRolesFromDao(){
        List<Role> mockRoles = List.of(Role.USER, Role.ADMIN);
//        when(userRoleDao.findAll()).thenReturn(mockRoles);

        List<UserRole> result = userRoleService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(Role.USER));
        assertTrue(result.contains(Role.ADMIN));

        verify(userRoleDao, times(1)).findAll();
        verifyNoMoreInteractions(userRoleDao);
    }

    @Test
    void givenFindAll_whenNoRolesExist_thenReturnEmptyList(){
        when(userRoleDao.findAll()).thenReturn(Collections.emptyList());

//        List<Role> result = userRoleService.findAll();

//        assertNotNull(result);
//        assertTrue(result.isEmpty());

        verify(userRoleDao, times(1)).findAll();
        verifyNoMoreInteractions(userRoleDao);
    }

}
