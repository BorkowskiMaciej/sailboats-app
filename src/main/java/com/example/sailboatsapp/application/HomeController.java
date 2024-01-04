package com.example.sailboatsapp.application;

import com.example.sailboatsapp.domain.offer.OfferService;
import com.example.sailboatsapp.domain.offer.Offer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final OfferService offerService;

    @GetMapping("/")
    public String home(Model model) {
        List<Offer> offers = offerService.findAllAvailableWithUserAndBoat();
        model.addAttribute("offers", offers);
        return "home";
    }

}
