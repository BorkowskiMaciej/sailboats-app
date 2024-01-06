package com.example.sailboatsapp.application;

import com.example.sailboatsapp.domain.user.AppUser;
import com.example.sailboatsapp.domain.user.UserService;
import com.example.sailboatsapp.security.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private LoginService loginService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void registerUserTest_Success() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .param("username", "newUser")
                        .param("email", "newUser@example.com")
                        .param("password", "password")
                        .param("name", "New")
                        .param("surname", "User")
                        .param("phoneNumber", "123456789")
                        .param("role", "USER")
                        .param("isCompany", "false")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/confirm"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void confirmRegistrationTest_ValidCode() throws Exception {
        String validCode = "validCode";
        when(userService.findByConfirmationCode(validCode)).thenReturn(Optional.of(new AppUser()));

        mockMvc.perform(post("/auth/confirm")
                        .param("confirmationCode", validCode))
                .andExpect(status().isOk())
                .andExpect(view().name("authentication/login"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void confirmRegistrationTest_InvalidCode() throws Exception {
        String invalidCode = "invalidCode";
        when(userService.findByConfirmationCode(invalidCode)).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/confirm")
                        .param("confirmationCode", invalidCode))
                .andExpect(status().isOk())
                .andExpect(view().name("authentication/confirm"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void registerUserTest_Failure_UserExists() throws Exception {
        when(userService.checkIfUsernameExists("existingUser")).thenReturn(true);

        mockMvc.perform(post("/auth/register")
                        .param("username", "existingUser")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("authentication/register"))
                .andExpect(model().attributeHasFieldErrors("appUser", "username"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void sendResetPasswordCodeTest_Success() throws Exception {
        when(userService.findByEmail("existing@example.com")).thenReturn(Optional.of(new AppUser()));

        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", "existing@example.com")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("authentication/enterResetCode"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void sendResetPasswordCodeTest_Failure_NoUser() throws Exception {
        when(userService.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", "nonexistent@example.com")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("authentication/resetPasswordRequest"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void resetPasswordTest_Success() throws Exception {
        String resetCode = "validResetCode";
        when(userService.findByResetPasswordCode(resetCode)).thenReturn(Optional.of(new AppUser()));

        mockMvc.perform(post("/auth/reset-password")
                        .param("resetCode", resetCode)
                        .param("password", "newPassword")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("authentication/login"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void resetPasswordTest_Failure_InvalidCode() throws Exception {
        String resetCode = "invalidResetCode";
        when(userService.findByResetPasswordCode(resetCode)).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/reset-password")
                        .param("resetCode", resetCode)
                        .param("password", "newPassword")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("authentication/enterResetCode"))
                .andExpect(model().attributeExists("error"));
    }


}
