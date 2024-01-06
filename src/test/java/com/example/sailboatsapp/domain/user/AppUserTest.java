package com.example.sailboatsapp.domain.user;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AppUserTest {

    @Test
    void testGettersAndSetters() {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("johndoe");
        user.setPassword("password");
        user.setEmail("john@example.com");
        user.setName("John");
        user.setSurname("Doe");
        user.setPhoneNumber("123456789");
        user.setIsCompany(true);
        user.setCompanyName("Doe Inc.");
        user.setTin("1234567890");
        user.setAddress("123 Main St");
        user.setIsConfirmed(true);
        user.setConfirmationCode("confirm123");
        user.setResetPasswordCode("reset123");

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        user.setRoles(roles);

        assertEquals(1L, user.getId());
        assertEquals("johndoe", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("John", user.getName());
        assertEquals("Doe", user.getSurname());
        assertEquals("123456789", user.getPhoneNumber());
        assertTrue(user.getIsCompany());
        assertEquals("Doe Inc.", user.getCompanyName());
        assertEquals("1234567890", user.getTin());
        assertEquals("123 Main St", user.getAddress());
        assertTrue(user.getIsConfirmed());
        assertEquals("confirm123", user.getConfirmationCode());
        assertEquals("reset123", user.getResetPasswordCode());
        assertEquals(roles, user.getRoles());
    }

    @Test
    void testBuilder() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");

        AppUser user = AppUser.builder()
                .id(1L)
                .username("johndoe")
                .password("password")
                .email("john@example.com")
                .name("John")
                .surname("Doe")
                .phoneNumber("123456789")
                .isCompany(true)
                .companyName("Doe Inc.")
                .tin("1234567890")
                .address("123 Main St")
                .isConfirmed(true)
                .confirmationCode("confirm123")
                .resetPasswordCode("reset123")
                .roles(roles)
                .build();

        assertEquals(1L, user.getId());
        assertEquals("johndoe", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("John", user.getName());
        assertEquals("Doe", user.getSurname());
        assertEquals("123456789", user.getPhoneNumber());
        assertTrue(user.getIsCompany());
        assertEquals("Doe Inc.", user.getCompanyName());
        assertEquals("1234567890", user.getTin());
        assertEquals("123 Main St", user.getAddress());
        assertTrue(user.getIsConfirmed());
        assertEquals("confirm123", user.getConfirmationCode());
        assertEquals("reset123", user.getResetPasswordCode());
        assertEquals(roles, user.getRoles());
    }
}
