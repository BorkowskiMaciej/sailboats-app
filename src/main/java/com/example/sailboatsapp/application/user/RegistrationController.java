package com.example.sailboatsapp.application.user;

import com.example.sailboatsapp.domain.user.UserFacade;
import com.example.sailboatsapp.domain.user.model.AppUser;
import com.example.sailboatsapp.security.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
@Slf4j
public class RegistrationController {

    private final UserFacade userFacade;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @GetMapping
    public ModelAndView showRegistrationForm() {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("appUser", new AppUser());
        return mav;
    }

    @PostMapping
    public String registerUser(@Valid AppUser appUser, BindingResult bindingResult) {
        if (userFacade.checkIfUserExists(appUser.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Konto z taką nazwą użytkownika już istnieje.");
            return "register";
        }
        if (Boolean.TRUE.equals(appUser.getIsCompany())) {
            if (appUser.getCompanyName() == null || appUser.getCompanyName().trim().isEmpty()) {
                bindingResult.rejectValue("companyName", "error.companyName", "Nazwa firmy jest wymagana.");
            }
            if (appUser.getTin() == null || appUser.getTin().trim().isEmpty()) {
                bindingResult.rejectValue("tin", "error.tin", "Numer NIP jest wymagany.");
            }
            if (appUser.getAddress() == null || appUser.getAddress().trim().isEmpty()) {
                bindingResult.rejectValue("address", "error.address", "Adres firmy jest wymagany.");
            }
        }
        if (!bindingResult.hasErrors()) {
            String encodedPassword = passwordEncoder.encode(appUser.getPassword());
            appUser.setPassword(encodedPassword);
            appUser.setIsConfirmed(false);
            String confirmationCode = UUID.randomUUID().toString();
            appUser.setConfirmationCode(confirmationCode);
            emailService.sendConfirmationEmail(appUser.getEmail(), confirmationCode);
            userFacade.addUser(appUser);
            return "redirect:/confirm";
        }

        return "register";
    }

}