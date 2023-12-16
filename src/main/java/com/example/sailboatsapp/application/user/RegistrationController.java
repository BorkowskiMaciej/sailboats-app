package com.example.sailboatsapp.application.user;

import com.example.sailboatsapp.domain.user.UserFacade;
import com.example.sailboatsapp.domain.user.model.AppUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserFacade userFacade;
    private final PasswordEncoder passwordEncoder;

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
            userFacade.addUser(appUser);
            return "redirect:/register?success";
        }

        return "register";
    }
}