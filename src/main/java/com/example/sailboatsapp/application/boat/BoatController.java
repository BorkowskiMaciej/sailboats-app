package com.example.sailboatsapp.application.boat;

import com.example.sailboatsapp.domain.boat.BoatFacade;
import com.example.sailboatsapp.domain.boat.model.Boat;
import com.example.sailboatsapp.domain.boat.repository.BoatRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class BoatController {

    private final BoatFacade boatFacade;

    @GetMapping("/boats")
    public String listBoats(Model model) {
        model.addAttribute("boats", boatFacade.findAll());
        return "boats/list";
    }

    @GetMapping("/boats/add")
    public String showAddForm(Model model) {
        model.addAttribute("boat", new Boat());
        return "boats/add";
    }

    @PostMapping("/boats")
    public String addBoat(Boat boat) {
        boatFacade.addBoat(boat);
        return "redirect:/boats";
    }
}
