package com.example.sailboatsapp.domain.user;

import com.example.sailboatsapp.domain.user.model.AppUser;
import com.example.sailboatsapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserRepository userRepository;

    public void addUser(AppUser appUser) {
        userRepository.save(appUser);
    }

    public boolean checkIfUserExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<AppUser> findByConfirmationCode(String confirmationCode) {
        return userRepository.findByConfirmationCode(confirmationCode);
    }

    public void confirmUser(String username) {
        userRepository.confirmUser(username);
    }

    public void setPasswordResetCode(String username, String resetPasswordCode) {
        userRepository.setResetPasswordCode(username, resetPasswordCode);
    }

    public Optional<AppUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<AppUser> findByResetPasswordCode(String resetCode) {
        return userRepository.findByResetPasswordCode(resetCode);
    }

    public void updatePassword(String username, String newPassword) {
        userRepository.updatePassword(username, newPassword);
    }

}
