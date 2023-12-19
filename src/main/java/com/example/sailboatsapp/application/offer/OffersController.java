package com.example.sailboatsapp.application.offer;

import com.example.sailboatsapp.domain.boat.BoatService;
import com.example.sailboatsapp.domain.boat.model.Boat;
import com.example.sailboatsapp.domain.offer.OfferService;
import com.example.sailboatsapp.domain.offer.model.Offer;
import com.example.sailboatsapp.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OffersController {

    private final BoatService boatService;
    private final OfferService offerService;
    private final UserService userService;

    @GetMapping("/new")
    public String showNewOfferForm(Model model) {
        List<Boat> userBoats = boatService.findAllByOwnerId(userService.getAuthenticatedUserId());
        model.addAttribute("userBoats", userBoats);
        model.addAttribute("offer", new Offer());

        return "offers/newOfferForm";
    }

    @GetMapping()
    public String showOffers(Model model) {
        List<Offer> offers = offerService.findAllWithUserAndBoat();
        model.addAttribute("offers", offers);
        return "offers/list";
    }
    @PostMapping("/new")
    public String createNewOffer(@ModelAttribute("offer") @Valid Offer offer,
            BindingResult bindingResult,
            Model model,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        if (bindingResult.hasErrors()) {
            List<Boat> userBoats = boatService.findAllByOwnerId(userService.getAuthenticatedUserId());
            model.addAttribute("userBoats", userBoats);
            return "offers/newOfferForm";
        }
        offer.setStartDate(startDate);
        offer.setEndDate(endDate);
        offer.setOwnerId(userService.getAuthenticatedUserId());
        offerService.addOffer(offer);
        return "redirect:/offers";
    }

}
