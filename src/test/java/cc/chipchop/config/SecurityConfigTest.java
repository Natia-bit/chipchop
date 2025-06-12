package cc.chipchop.config;

import cc.chipchop.entity.User;
import cc.chipchop.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ContextConfiguration(classes = {SecurityConfig.class, UserDetailServiceImpl.class})
//@WebMvcTest(ChipchopRestController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final User mockUser = new User(1, "test@example.com", "password");

    @Test
    void givenNoCredentials_whenAccessProtectedEndpoint_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isUnauthorized());

        verifyNoMoreInteractions(userService);
    }

    @Test
    void givenInvalidCredentials_whenAccessProtectedEndpoint_thenUnauthorised() throws Exception {
        mockMvc.perform(get("/api/users")
                .with(httpBasic("invalid@example.com", "wrongpass")))
            .andExpect(status().isUnauthorized());

        verify(userService, times(1)).findByEmail("invalid@example.com");
        verifyNoMoreInteractions(userService);
    }

    @Test
    void givenValidCredentials_whenAccessProtectedEndpoint_thenOk() throws Exception {
        when(userService.findByEmail("test@example.com")).thenReturn(mockUser);
        when(userService.findAll()).thenReturn(List.of(mockUser));

        mockMvc.perform(get("/api/users")
                .with(httpBasic("test@example.com", "password")))
            .andExpect(status().isOk());

        verify(userService, times(1)).findAll();
        verify(userService, times(1)).findByEmail("test@example.com");
        verifyNoMoreInteractions(userService);
    }

    @Test
    void givenCsrfToken_whenPostRequest_thenCsrfIgnored() throws Exception {
        when(userService.findByEmail("test@example.com")).thenReturn(mockUser);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"new@example.com\",\"password\":\"pass\"}")
                .with(httpBasic("test@example.com", "password"))
                .with(csrf()))
            .andExpect(status().isCreated());

        verify(userService).insert(any(User.class));
        verify(userService).findByEmail("test@example.com");
        verifyNoMoreInteractions(userService);
    }

    @Test
    void givenValidCredentials_whenAccessUserById_thenOk() throws Exception {
        when(userService.findByEmail("test@example.com")).thenReturn(mockUser);
        when(userService.findById(1L)).thenReturn(Optional.of(mockUser));

        mockMvc.perform(get("/api/users/1")
                .with(httpBasic("test@example.com", "password")))
            .andExpect(status().isOk());

        verify(userService).findByEmail("test@example.com");
        verify(userService).findById(1L);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void givenValidCredentials_whenSessionCreated_thenNoSessionCookies() throws Exception {
        when(userService.findByEmail("test@example.com")).thenReturn(mockUser);
        when(userService.findAll()).thenReturn(List.of(mockUser));

        mockMvc.perform(get("/api/users")
                .with(httpBasic("test@example.com", "password")))
            .andExpect(status().isOk())
            .andExpect(header().doesNotExist("Set-Cookie"));

        verify(userService).findByEmail("test@example.com");
        verify(userService).findAll();
        verifyNoMoreInteractions(userService);
    }

    @Test
    void givenCaseSensitiveEmail_whenAccessEndpoint_thenUnauthorized() throws Exception {
        when(userService.findByEmail("Test@example.com"))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/users")
                .with(httpBasic("Test@example.com", "password")))
            .andExpect(status().isUnauthorized());

        verify(userService).findByEmail("Test@example.com");
        verifyNoMoreInteractions(userService);
    }
}
