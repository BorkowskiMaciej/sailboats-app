package com.example.sailboatsapp.application.user;

import com.example.sailboatsapp.domain.user.UserFacade;
import com.example.sailboatsapp.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public ModelAndView showRegistrationForm() {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("user", new User());
        return mav;
    }

    @PostMapping
    public String registerUser(@Valid User user, BindingResult bindingResult) {
        if (userFacade.checkIfUserExists(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Konto z taką nazwą użytkownika już istnieje.");
            return "register";
        }
        if (Boolean.TRUE.equals(user.getIsCompany())) {
            if (user.getCompanyName() == null || user.getCompanyName().trim().isEmpty()) {
                bindingResult.rejectValue("companyName", "error.companyName", "Nazwa firmy jest wymagana.");
            }
            if (user.getTin() == null || user.getTin().trim().isEmpty()) {
                bindingResult.rejectValue("tin", "error.tin", "Numer NIP jest wymagany.");
            }
            if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
                bindingResult.rejectValue("address", "error.address", "Adres firmy jest wymagany.");
            }
        }
        if (!bindingResult.hasErrors()) {
            userFacade.addUser(user);
            return "redirect:/register?success";
        }

        return "register";
    }
}