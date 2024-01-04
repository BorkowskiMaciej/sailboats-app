package com.example.sailboatsapp.application;

import com.example.sailboatsapp.domain.user.UserService;
import com.example.sailboatsapp.domain.user.entity.AppUser;
import com.example.sailboatsapp.security.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    @GetMapping
    public String showAccount(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        AppUser user = userService.findByUsername(currentUser.getUsername());
        model.addAttribute("role", currentUser.getAuthorities().stream().findFirst().orElseThrow().getAuthority());
        model.addAttribute("user", user);
        return "user/account";
    }

    @PostMapping("/update")
    public String updateAccount(@ModelAttribute("user") @Valid AppUser user,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model,
            @AuthenticationPrincipal UserDetails currentUser) {
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
        if (bindingResult.hasErrors()) {
            model.addAttribute("role", currentUser.getAuthorities().stream().findFirst().orElseThrow().getAuthority());
            model.addAttribute("user", user);
            return "user/account";
        }
        userService.updateUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Dane zostały pomyślnie zaktualizowane.");
        return "redirect:/account";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam("password") String newPassword,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        loginService.resetPassword(username, newPassword);
        redirectAttributes.addFlashAttribute("successMessage", "Hasło zostało pomyślnie zaktualizowane.");
        return "redirect:/account";
    }

    @GetMapping("/change-password")
    public String changePassword() {
        return "user/changePassword";
    }

    @GetMapping("/delete")
    public String deleteAccount(Principal principal) {
        String username = principal.getName();
        userService.deleteUser(username);
        return "redirect:/logout";
    }
}
