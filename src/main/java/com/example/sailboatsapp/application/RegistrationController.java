package com.example.sailboatsapp.application;

import com.example.sailboatsapp.domain.user.UserFacade;
import com.example.sailboatsapp.domain.user.model.AppUser;
import com.example.sailboatsapp.security.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

    private final UserFacade userFacade;
    private final LoginService loginService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("loginError", "Błędna nazwa użytkownika lub hasło.");
        }
        return "login";
    }

    @GetMapping("/register")
    public ModelAndView showRegistrationForm() {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("appUser", new AppUser());
        return mav;
    }

    @GetMapping("/confirm")
    public String showConfirmationForm() {
        return "confirm";
    }

    @GetMapping("/request-reset-password")
    public String showResetPasswordRequestForm() {
        return "resetPasswordRequest";
    }

    @PostMapping("/register")
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
            loginService.register(appUser);
            return "redirect:/confirm";
        }

        return "register";
    }

    @PostMapping("/confirm")
    public String confirmRegistration(@
            RequestParam("confirmationCode") String confirmationCode,
            Model model) {

        Optional<AppUser> user = userFacade.findByConfirmationCode(confirmationCode);
        if (user.isPresent()) {
            loginService.confirm(user.get().getUsername());
            model.addAttribute("message", "Konto zostało pomyślnie aktywowane.");
            return "confirmSuccess";
        } else {
            model.addAttribute("message", "Nieprawidłowy kod potwierdzający.");
            return "confirm";
        }
    }

    @PostMapping("/request-reset-password")
    public String sendResetPasswordCode(
            @RequestParam("email") String email,
            Model model) {

        Optional<AppUser> userOptional = userFacade.findByEmail(email);
        if (userOptional.isPresent()) {
            loginService.requestPasswordReset(userOptional.get());
            model.addAttribute("message", "Kod resetowania hasła został wysłany na Twój adres e-mail.");
            return "enterResetCode";
        } else {
            model.addAttribute("error", "Nie znaleziono konta z podanym adresem e-mail.");
            return "resetPasswordRequest";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam("resetCode") String resetCode,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        //TODO: add this validation on client side
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Hasła nie są takie same.");
            return "enterResetCode";
        }

        Optional<AppUser> userOptional = userFacade.findByResetPasswordCode(resetCode);
        if (userOptional.isPresent()) {
            loginService.resetPassword(userOptional.get().getUsername(), newPassword);
            model.addAttribute("message", "Twoje hasło zostało zresetowane.");
            return "login";
        } else {
            model.addAttribute("error", "Nieprawidłowy kod resetowania.");
            return "enterResetCode";
        }
    }

}