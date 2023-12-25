package com.example.sailboatsapp.application;

import com.example.sailboatsapp.domain.user.UserService;
import com.example.sailboatsapp.domain.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String showAccount(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        AppUser user = userService.findByUsername(currentUser.getUsername());
        model.addAttribute("user", user);
        return "user/account";
    }

//    @PatchMapping("/update")
//    public String updateAccount(AppUser user) {
//        userService.updateUser(user);
//        return "redirect:/account";
//    }
}
