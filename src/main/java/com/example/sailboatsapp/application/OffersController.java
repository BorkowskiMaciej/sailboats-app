package com.example.sailboatsapp.application;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        return "offers/add";
    }

    @GetMapping()
    public String showOffers(Model model) {
        List<Offer> offers = offerService.findAllWithUserAndBoat();
        model.addAttribute("offers", offers);
        return "offers/list";
    }

    @GetMapping("/{offerId}")
    public String showOfferDetails(@PathVariable Long offerId, Model model) {
        Offer offer = offerService.findWithUserAndBoat(offerId);
        if (offer == null) {
            return "redirect:/offers";
        }
        model.addAttribute("offer", offer);
        return "offers/detail";
    }

    @PostMapping("/new")
    public String createNewOffer(@ModelAttribute("offer") @Valid Offer offer,
            BindingResult bindingResult,
            Model model,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            List<Boat> userBoats = boatService.findAllByOwnerId(userService.getAuthenticatedUserId());
            model.addAttribute("userBoats", userBoats);
            return "offers/add";
        }
        offer.setStartDate(startDate);
        offer.setEndDate(endDate);
        offer.setOwnerId(userService.getAuthenticatedUserId());
        offerService.addOffer(offer);
        redirectAttributes.addFlashAttribute("successMessage", "Oferta pomyślnie została dodana.");
        return "redirect:/offers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Offer offer = offerService.findWithUserAndBoat(id);
        List<Boat> userBoats = boatService.findAllByOwnerId(userService.getAuthenticatedUserId());
        model.addAttribute("userBoats", userBoats);
        model.addAttribute("offer", offer);
        return "offers/edit";
    }

    @PostMapping("/update/{id}")
    public String updateOffer(@ModelAttribute("offer") @Valid Offer offer,
            BindingResult bindingResult,
            Model model,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            List<Boat> userBoats = boatService.findAllByOwnerId(userService.getAuthenticatedUserId());
            model.addAttribute("userBoats", userBoats);
            return "offers/edit";
        }
        offer.setStartDate(startDate);
        offer.setEndDate(endDate);
        offer.setOwnerId(userService.getAuthenticatedUserId());
        offerService.addOffer(offer);
        redirectAttributes.addFlashAttribute("successMessage", "Oferta została zaktualizowana.");
        return "redirect:/offers";
    }

    @PostMapping("/delete/{id}")
    public String deleteBoat(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        offerService.deleteOffer(id);
        redirectAttributes.addFlashAttribute("successMessage", "Oferta została usunięta.");
        return "redirect:/offers";
    }


}
