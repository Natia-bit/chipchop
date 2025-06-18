package cc.chipchop.services;

import cc.chipchop.dao.RoleDao;
import cc.chipchop.entity.Role;
import cc.chipchop.service.RoleService;
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
public class RoleServiceTest {

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private RoleService roleService;

    @Test
    void givenFindAll_whenRolesExist_thenReturnRolesFromDao(){
        List<Role> mockRoles = List.of(Role.USER, Role.ADMIN);
        when(roleDao.findAll()).thenReturn(mockRoles);

        List<Role> result = roleService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(Role.USER));
        assertTrue(result.contains(Role.ADMIN));

        verify(roleDao, times(1)).findAll();
        verifyNoMoreInteractions(roleDao);
    }

    @Test
    void givenFindAll_whenNoRolesExist_thenReturnEmptyList(){
        when(roleDao.findAll()).thenReturn(Collections.emptyList());

        List<Role> result = roleService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(roleDao, times(1)).findAll();
        verifyNoMoreInteractions(roleDao);
    }

}
