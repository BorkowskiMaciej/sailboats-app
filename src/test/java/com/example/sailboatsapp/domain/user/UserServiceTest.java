package com.example.sailboatsapp.domain.user;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        UserDetails mockUserDetails = new User("username", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);
    }

    @Test
    void findByConfirmationCodeTest() {
        Optional<AppUser> userOptional = Optional.of(new AppUser());
        when(userRepository.findByConfirmationCode("code")).thenReturn(userOptional);

        assertEquals(userOptional, userService.findByConfirmationCode("code"));
    }

    @Test
    void findByEmailTest() {
        Optional<AppUser> userOptional = Optional.of(new AppUser());
        when(userRepository.findByEmail("email")).thenReturn(userOptional);

        assertEquals(userOptional, userService.findByEmail("email"));
    }

    @Test
    void findByResetPasswordCodeTest() {
        Optional<AppUser> userOptional = Optional.of(new AppUser());
        when(userRepository.findByResetPasswordCode("code")).thenReturn(userOptional);

        assertEquals(userOptional, userService.findByResetPasswordCode("code"));
    }

    @Test
    void checkIfUsernameExistsTest() {
        when(userRepository.existsByUsername("username")).thenReturn(true);

        assertTrue(userService.checkIfUsernameExists("username"));
    }

    @Test
    void checkIfEmailExistsTest() {
        when(userRepository.existsByEmail("email")).thenReturn(true);

        assertTrue(userService.checkIfEmailExists("email"));
    }

    @Test
    void registerTest() {
        AppUser user = new AppUser();
        userService.register(user);

        verify(userRepository).save(user);
    }

    @Test
    void setPasswordResetCodeTest() {
        userService.setPasswordResetCode("username", "resetCode");

        verify(userRepository).setResetPasswordCode("username", "resetCode");
    }

    @Test
    void updatePasswordTest() {
        userService.updatePassword("username", "newPassword");

        verify(userRepository).updatePassword("username", "newPassword");
    }

    @Test
    void findByIdTest() {
        AppUser user = new AppUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertEquals(user, userService.findById(1L));
    }

    @Test
    void findByUsernameTest() {
        AppUser user = new AppUser();
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        assertEquals(user, userService.findByUsername("username"));
    }

    @Test
    void deleteUserByUsernameTest() {
        userService.deleteUser("username");

        verify(userRepository).deleteByUsername("username");
    }

    @Test
    void deleteUserByIdTest() {
        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void findAllTest() {
        List<AppUser> users = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users);

        assertEquals(users, userService.findAll());
    }
}
