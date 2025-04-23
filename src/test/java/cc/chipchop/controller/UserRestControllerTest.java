package cc.chipchop.controller;

import cc.chipchop.entity.User;
import cc.chipchop.rest.ChipchopRestController;
import cc.chipchop.service.UserService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChipchopRestController.class)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;


    @Test
    public void givenGetAllUsers_whenGetAllUsers_thenSucceedWith200() throws Exception {
        when(userService.findAll()).thenReturn(List.of(
            new User(1, "one@test.com", "password"),
            new User(2, "two@test.com", "passdrd"),
            new User(3, "tree@test.com", "pass")));

        mockMvc.perform(get("/api/users"))
            .andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.size()").value(3),
                jsonPath("$[0].id").value(1),
                jsonPath("$[0].email").value("one@test.com"),
                jsonPath("$[0].password").value("password")
            );

        verify(userService, times(1)).findAll();
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void givenGetAllUsers_whenUserListIsEmpty_thenReturnEmptyList() throws Exception {
        when(userService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users"))
            .andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(0)
            );

        verify(userService, times(1)).findAll();
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void givenGetAllUsers_whenDatabaseIsDown_thenReturn500() throws Exception {
        when(userService.findAll()).thenThrow(new RuntimeException());

        assertThrows(ServletException.class, () ->
            mockMvc.perform(get("/api/users")).andReturn()
        );

        verify(userService, times(1)).findAll();
    }
}
