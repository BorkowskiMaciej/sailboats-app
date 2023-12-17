package com.example.sailboatsapp.application.boat;

import com.example.sailboatsapp.domain.boat.BoatFacade;
import com.example.sailboatsapp.domain.boat.model.Boat;
import com.example.sailboatsapp.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boats")
public class BoatController {

    private final BoatFacade boatFacade;
    private final UserService userService;

    @GetMapping("/add")
    public String showAddBoatForm(Model model) {
        model.addAttribute("boat", new Boat());
        return "boats/addBoatForm";
    }

    @PostMapping("/add")
    public String addBoat(@Valid Boat boat, BindingResult result) {
        if (result.hasErrors()) {
            return "boats/addBoatForm";
        }
        boat.setOwnerId(userService.getAuthenticatedUserId());
        boatFacade.addBoat(boat);
        return "redirect:/boats/list";
    }

    @GetMapping("/list")
    public String listBoats(Model model) {
        Collection<Boat> boats = boatFacade.findAllByOwnerId(userService.getAuthenticatedUserId());
        model.addAttribute("boats", boats);
        return "boats/boatsList";
    }

}
