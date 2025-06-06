package cc.chipchop.config;

import cc.chipchop.rest.ChipchopRestController;
import cc.chipchop.service.UserDetailServiceImpl;
import cc.chipchop.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChipchopRestController.class)
@Import({SecurityConfig.class, UserDetailServiceImpl.class})
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

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
    @WithMockUser(username = "test@example.com")
    void givenValidCredentials_whenAccessProtectedEndpoint_thenOk() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk());

        verify(userService, times(1)).findAll();
        verifyNoMoreInteractions(userService);
    }
}
