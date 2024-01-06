package com.example.sailboatsapp.application;

import com.example.sailboatsapp.domain.user.AppUser;
import com.example.sailboatsapp.domain.user.UserService;
import com.example.sailboatsapp.security.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private LoginService loginService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("testUser", "password", Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void showUserListTest() throws Exception {
        when(userService.findAll()).thenReturn(List.of(new AppUser()));

        mockMvc.perform(get("/account/all").with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"));

        verify(userService).findAll();
    }

    @Test
    void updateAccountTest() throws Exception {
        mockMvc.perform(post("/account/update")
                        .with(user(testUser))
                        .with(csrf())
                        .param("name", "UpdatedName")
                        .param("surname", "UpdatedSurname")
                        .param("phoneNumber", "123456789")
                        .param("isCompany", "true")
                        .param("companyName", "UpdatedCompanyName")
                        .param("tin", "1234567890")
                        .param("address", "UpdatedAddress"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account"));

        ArgumentCaptor<AppUser> argumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(userService).updateUser(argumentCaptor.capture());
        assertEquals("UpdatedName", argumentCaptor.getValue().getName());
    }

    @Test
    void changePasswordTest() throws Exception {
        Principal principal = () -> "testUser";

        mockMvc.perform(post("/account/change-password")
                        .principal(principal)
                        .with(csrf())
                        .param("password", "newPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account"));

        verify(loginService).resetPassword(eq("testUser"), eq("newPassword"));
    }

    @Test
    void deleteUserTest() throws Exception {
        Principal principal = () -> "testUser";

        mockMvc.perform(post("/account/delete")
                        .principal(principal)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/logout"));

        verify(userService).deleteUser("testUser");
    }

    @Test
    void deleteUserByIdTest() throws Exception {
        mockMvc.perform(post("/account/delete/1")
                        .with(user(testUser))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/all"));

        verify(userService).deleteUser(1L);
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void changePasswordViewTest() throws Exception {
        mockMvc.perform(get("/account/change-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/changePassword"));
    }
}
