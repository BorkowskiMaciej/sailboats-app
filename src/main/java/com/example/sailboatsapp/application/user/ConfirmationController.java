package com.example.sailboatsapp.application.user;

import com.example.sailboatsapp.domain.user.UserFacade;
import com.example.sailboatsapp.domain.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/confirm")
@Slf4j
public class ConfirmationController {

    private final UserFacade userFacade;

    @GetMapping
    public String showConfirmationForm() {
        return "confirm";
    }
    @PostMapping
    public String confirmRegistration(@RequestParam("confirmationCode") String confirmationCode, Model model) {
        log.info("Confirmation code: {}", confirmationCode);
        Optional<AppUser> user = userFacade.findByConfirmationCode(confirmationCode);
        if (user.isPresent()) {
            userFacade.confirmUser(user.get().getUsername());
            model.addAttribute("message", "Konto zostało pomyślnie aktywowane.");
            return "confirmSuccess";
        } else {
            model.addAttribute("message", "Nieprawidłowy kod potwierdzający.");
            return "confirm";
        }
    }

}
