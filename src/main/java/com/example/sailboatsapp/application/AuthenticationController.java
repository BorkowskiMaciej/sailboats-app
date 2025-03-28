package com.example.sailboatsapp.application;

import com.example.sailboatsapp.domain.user.UserService;
import com.example.sailboatsapp.domain.user.AppUser;
import com.example.sailboatsapp.security.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    private final UserService userService;
    private final LoginService loginService;

    public boolean checkIfAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth != null && !auth.getName().equals("anonymousUser");
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (checkIfAuthenticated()){
            return "redirect:/";
        }
        if (error != null) {
            model.addAttribute("loginError", "Błędne dane logowania lub niezweryfikowane konto.");
        }
        return "authentication/login";
    }

    @GetMapping("/register")
    public ModelAndView showRegistrationForm() {
        if (checkIfAuthenticated()){
            return new ModelAndView("redirect:/");
        }
        ModelAndView mav = new ModelAndView("authentication/register");
        mav.addObject("appUser", new AppUser());
        return mav;
    }

    @GetMapping("/confirm")
    public String showConfirmationForm() {
        if (checkIfAuthenticated()){
            return "redirect:/";
        }
        return "authentication/confirm";
    }

    @GetMapping("/request-reset-password")
    public String showResetPasswordRequestForm() {
        if (checkIfAuthenticated()){
            return "redirect:/";
        }
        return "authentication/resetPasswordRequest";
    }

    @PostMapping("/register")
    public String registerUser(@Valid AppUser appUser, BindingResult bindingResult, RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        if (userService.checkIfUsernameExists(appUser.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Konto z taką nazwą użytkownika już istnieje.");
            return "authentication/register";
        }
        if (userService.checkIfEmailExists(appUser.getEmail())) {
            bindingResult.rejectValue("email", "error.email", "Konto z takim adresem e-mail już istnieje.");
            return "authentication/register";
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
            String role = request.getParameter("role");
            Set<String> roles = new HashSet<>();
            roles.add(role);
            appUser.setRoles(roles);
            loginService.register(appUser);
            redirectAttributes.addFlashAttribute("message",
                    "Pomyślnie zarejestrowano. W celu dokończenia rejestracji wprowadź kod wysłany na Twój adres e-mail.");
            return "redirect:/auth/confirm";
        }

        return "authentication/register";
    }

    @PostMapping("/confirm")
    public String confirmRegistration(
            @RequestParam("confirmationCode") String confirmationCode,
            Model model) {

        Optional<AppUser> user = userService.findByConfirmationCode(confirmationCode);
        if (user.isPresent()) {
            loginService.confirm(user.get().getUsername());
            model.addAttribute("message", "Konto zostało pomyślnie aktywowane.");
            return "authentication/login";
        } else {
            model.addAttribute("error", "Nieprawidłowy kod potwierdzający.");
            return "authentication/confirm";
        }
    }

    @PostMapping("/request-reset-password")
    public String sendResetPasswordCode(
            @RequestParam("email") String email,
            Model model) {

        Optional<AppUser> user = userService.findByEmail(email);
        if (user.isPresent()) {
            loginService.requestPasswordReset(user.get());
            model.addAttribute("message", "Kod resetowania hasła został wysłany na Twój adres e-mail.");
            return "authentication/enterResetCode";
        } else {
            model.addAttribute("error", "Nie znaleziono konta z podanym adresem e-mail.");
            return "authentication/resetPasswordRequest";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam("resetCode") String resetCode,
            @RequestParam("password") String newPassword,
            Model model) {

        Optional<AppUser> user = userService.findByResetPasswordCode(resetCode);
        if (user.isPresent()) {
            loginService.resetPassword(user.get().getUsername(), newPassword);
            model.addAttribute("message", "Twoje hasło zostało zresetowane.");
            return "authentication/login";
        } else {
            model.addAttribute("error", "Nieprawidłowy kod resetowania.");
            return "authentication/enterResetCode";
        }
    }

}