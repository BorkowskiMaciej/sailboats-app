package com.example.sailboatsapp.domain.user;

import com.example.sailboatsapp.domain.user.model.AppUser;
import com.example.sailboatsapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<AppUser> findByConfirmationCode(String confirmationCode) {
        return userRepository.findByConfirmationCode(confirmationCode);
    }

    public Optional<AppUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<AppUser> findByResetPasswordCode(String resetCode) {
        return userRepository.findByResetPasswordCode(resetCode);
    }

    public boolean checkIfUserExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public void register(AppUser user) {
        userRepository.save(user);
    }

    public void setPasswordResetCode(String username, String resetPasswordCode) {
        userRepository.setResetPasswordCode(username, resetPasswordCode);
    }

    public void updatePassword(String username, String newPassword) {
        userRepository.updatePassword(username, newPassword);
    }

    public void confirmUser(String username) {
        userRepository.confirmUser(username);
    }

    public void clearConfirmationCode(String username) {
        userRepository.clearConfirmationCode(username);
    }
    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        return userRepository.findByUsername(username).orElseThrow().getId();
    }


}
