package com.example.sailboatsapp.application.user;

import com.example.sailboatsapp.domain.user.UserFacade;
import com.example.sailboatsapp.domain.user.model.AppUser;
import com.example.sailboatsapp.security.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ResetPasswordController {

    private final UserFacade userFacade;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/request-reset-password")
    public String showResetPasswordRequestForm() {
        return "resetPasswordRequest";
    }
    @PostMapping("/request-reset-password")
    public String sendResetPasswordCode(@RequestParam("email") String email, Model model) {
        Optional<AppUser> userOptional = userFacade.findByEmail(email);
        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            String resetCode = UUID.randomUUID().toString();
            userFacade.setPasswordResetCode(user.getUsername(), resetCode);

            emailService.sendResetPasswordCodeEmail(user.getEmail(), resetCode);

            model.addAttribute("message", "Kod resetowania hasła został wysłany na Twój adres e-mail.");
            return "enterResetCode";
        } else {
            model.addAttribute("error", "Nie znaleziono konta z podanym adresem e-mail.");
            return "resetPasswordRequest";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("resetCode") String resetCode,
            @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword,
            Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Hasła nie są takie same.");
            return "enterResetCode";
        }

        Optional<AppUser> userOptional = userFacade.findByResetPasswordCode(resetCode);
        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            userFacade.updatePassword(user.getUsername(), passwordEncoder.encode(newPassword));
            model.addAttribute("message", "Twoje hasło zostało zresetowane.");
            return "login";
        } else {
            model.addAttribute("error", "Nieprawidłowy kod resetowania.");
            return "enterResetCode";
        }
    }

}
