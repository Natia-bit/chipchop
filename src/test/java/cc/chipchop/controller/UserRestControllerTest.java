package cc.chipchop.controller;

import cc.chipchop.entity.User;
import cc.chipchop.rest.ChipchopRestController;
import cc.chipchop.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChipchopRestController.class)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .findAndRegisterModules();


    @Test
    public void givenGetAllUsers_whenGetAllUsers_thenSucceedWith200() throws Exception {
        when(userService.findAll()).thenReturn(List.of(
            new User(1, "one@test.com", "password"),
            new User(2, "two@test.com", "passdrd"),
            new User(3, "tree@test.com", "pass")));

        mockMvc.perform(get("/api/users"))
            .andDo(print())
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


    @Test
    public void givenFindUserById_whenFindExistingUser_thenSucceedWith200() throws Exception {
        when(userService.findById(1))
            .thenReturn(Optional.of(new User(1, "bla@testing.com", "supersecret")));

        mockMvc.perform(get("/api/users/{id}", 1))
            .andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.id").value(1),
                jsonPath("$.email").value("bla@testing.com"),
                jsonPath("$.password").value("supersecret")
            )
            .andDo(print())
            .andReturn();

        verify(userService, times(1)).findById(1);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void givenFindUserById_whenFindingInvalidUser_thenReturnNotFound404() throws Exception {
        when(userService.findById(2)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/users/{id}", 2))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(2);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void givenFindUserById_whenDatabaseIsDown_thenReturn500() throws Exception {
        when(userService.findById(3)).thenThrow(new RuntimeException());

        assertThrows(ServletException.class, () ->
            mockMvc.perform(get("/api/users/{id}", 3)).andReturn()
        );

        verify(userService, times(1)).findById(3);
    }


    @Test
    public void givenCreateUser_whenCreatingUser_thenSucceedWith200() throws Exception {
        User dude = new User(1, "dude@test.com", "java");

        mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dude)))
            .andExpect(status().isOk());

        verify(userService, times(1)).insert(argThat(user ->
                user.email().equals("dude@test.com") && user.password().equals("java")));
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void givenCreateUser_whenUsingExistingEmail_thenReturnConflict409() throws Exception {
        User dude = new User(1, "dude@test.com", "java");

        doThrow(new ResponseStatusException(HttpStatus.CONFLICT))
            .when(userService).insert(any(User.class));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dude)))
            .andExpect(status().isConflict());

        verify(userService, times(1)).insert(any(User.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void givenInsertNewUser_whenDatabaseIsDown_thenReturn500() throws Exception {
        User dude = new User(1, "dude@test.com", "java");
        doThrow(new RuntimeException()).when(userService).insert(any(User.class));

        ServletException ex = assertThrows(ServletException.class, () ->
            mockMvc.perform(post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dude)))
                .andReturn()
        );

        assertInstanceOf(RuntimeException.class, ex.getCause());
        verify(userService, times(1)).insert(any(User.class));
        verifyNoMoreInteractions(userService);
    }

}
