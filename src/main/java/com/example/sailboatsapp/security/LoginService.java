package com.example.sailboatsapp.security;

import com.example.sailboatsapp.domain.user.UserFacade;
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
    private final UserFacade userFacade;

    public void register(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsConfirmed(false);
        String confirmationCode = UUID.randomUUID().toString();
        user.setConfirmationCode(confirmationCode);
        emailService.sendConfirmationCode(user.getEmail(), confirmationCode);
        userFacade.register(user);
    }

    public void confirm(String username) {
        userFacade.confirmUser(username);

    }

    public void requestPasswordReset(AppUser user) {
        String resetCode = UUID.randomUUID().toString();
        userFacade.setPasswordResetCode(user.getUsername(), resetCode);
        emailService.sendResetPasswordCode(user.getEmail(), resetCode);
    }

    public void resetPassword(String username, String password) {
        userFacade.updatePassword(username, passwordEncoder.encode(password));
        userFacade.setPasswordResetCode(username, null);
    }

}
