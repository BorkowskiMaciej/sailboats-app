package com.example.sailboatsapp.security;

import com.example.sailboatsapp.domain.user.UserService;
import com.example.sailboatsapp.domain.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserService userService;

    public void register(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsConfirmed(false);
        String confirmationCode = UUID.randomUUID().toString();
        user.setConfirmationCode(confirmationCode);
        emailService.sendConfirmationCode(user.getEmail(), confirmationCode);
        userService.register(user);
    }

    public void confirm(String username) {
        userService.confirmUser(username);
        userService.clearConfirmationCode(username);
    }

    public void requestPasswordReset(AppUser user) {
        String resetCode = UUID.randomUUID().toString();
        userService.setPasswordResetCode(user.getUsername(), resetCode);
        emailService.sendResetPasswordCode(user.getEmail(), resetCode);
    }

    public void resetPassword(String username, String password) {
        userService.updatePassword(username, passwordEncoder.encode(password));
        userService.setPasswordResetCode(username, null);
    }

}
