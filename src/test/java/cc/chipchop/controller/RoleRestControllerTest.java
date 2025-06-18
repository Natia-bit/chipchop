package cc.chipchop.controller;

import cc.chipchop.entity.Role;
import cc.chipchop.exception.ControllerExceptionHandler;
import cc.chipchop.rest.RoleRestController;
import cc.chipchop.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.ConnectException;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {RoleRestController.class, ControllerExceptionHandler.class})
@WebMvcTest( value = RoleRestController.class,  excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class RoleRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleService roleService;

    @Test
    public void givenGetAllRoles_whenLookingAllRoles_thenSucceedWith200() throws Exception {
        List<Role> mockRoles = List.of(Role.USER, Role.ADMIN);
        when(roleService.findAll()).thenReturn(mockRoles);

        mockMvc.perform(get("/api/roles"))
            .andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(2),
                jsonPath("$[0]").value("USER"),
                jsonPath("$[1]").value("ADMIN")
            );

        verify(roleService, times(1)).findAll();
        verifyNoMoreInteractions(roleService);
    }

    @Test
    public void givenGetAllRoles_whenRolesAreEmpty_thenReturn200WithEmptyList() throws Exception {
        when(roleService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/roles"))
            .andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(0)
            );

        verify(roleService, times(1)).findAll();
        verifyNoMoreInteractions(roleService);
    }

    @Test
    public void givenGetAllRoles_WhenServiceThrowsException_thenReturn500() throws Exception {
        when(roleService.findAll()).thenThrow(new RuntimeException(new ConnectException()));

        mockMvc.perform(get("/api/roles"))
            .andExpect(status().isInternalServerError());

        verify(roleService, times(1)).findAll();
        verifyNoMoreInteractions(roleService);
    }

    @Test
    public void givenGetAllUsers_whenDatabaseIsDown_thenReturn500() throws Exception {
        when(roleService.findAll()).thenThrow(new RuntimeException(new ConnectException()));

        mockMvc.perform(get("/api/roles"))
            .andExpect(status().isInternalServerError());

        verify(roleService, times(1)).findAll();
        verifyNoMoreInteractions(roleService);
    }
}
