package com.example.sailboatsapp.application;

import com.example.sailboatsapp.domain.boat.BoatService;
import com.example.sailboatsapp.domain.boat.entity.Boat;
import com.example.sailboatsapp.domain.offer.OfferService;
import com.example.sailboatsapp.domain.offer.Offer;
import com.example.sailboatsapp.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@Slf4j
public class OffersController {

    private final BoatService boatService;
    private final OfferService offerService;
    private final UserService userService;

    @GetMapping
    public String showMyOffers(Model model) {
        List<Offer> offers = offerService.findAllWithUserAndBoatByOwnerId(userService.getAuthenticatedUserId());
        model.addAttribute("offers", offers);
        return "offers/list";
    }

    @GetMapping("/all")
    public String showAllOffers(Model model) {
        List<Offer> offers = offerService.findAllWithUserAndBoat();
        model.addAttribute("offers", offers);
        return "offers/list";
    }

    @GetMapping("/show/{offerId}")
    public String showOfferDetails(@PathVariable Long offerId, Model model) {
        Offer offer = offerService.findWithUserAndBoat(offerId);
        if (offer == null) {
            return "redirect:/offers";
        }
        model.addAttribute("offer", offer);
        model.addAttribute("isOfferAvailable", !offerService.isOfferReserved(offerId));
        return "offers/detail";
    }

    @GetMapping("/new")
    public String showNewOfferForm(Model model) {
        prepareModelForOfferForm(model);
        model.addAttribute("offer", new Offer());
        return "offers/add";
    }

    @PostMapping("/new")
    public String createNewOffer(@ModelAttribute("offer") @Valid Offer offer,
            BindingResult bindingResult,
            Model model,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails currentUser) {
        return processOffer(offer, bindingResult, model, startDate, endDate, redirectAttributes, true, currentUser);
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Offer offer = offerService.findWithUserAndBoat(id);
        if (offer == null) {
            return "redirect:/offers";
        }
        prepareModelForOfferForm(model);
        model.addAttribute("offer", offer);
        return "offers/edit";
    }

    @PostMapping("/update/{id}")
    public String updateOffer(@ModelAttribute("offer") @Valid Offer offer,
            BindingResult bindingResult,
            Model model,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails currentUser) {
        return processOffer(offer, bindingResult, model, startDate, endDate, redirectAttributes, true, currentUser);
    }

    @PostMapping("/delete/{id}")
    public String deleteBoat(@PathVariable("id") Long id, RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails currentUser) {
        offerService.deleteOffer(id);
        redirectAttributes.addFlashAttribute("successMessage", "Oferta została usunięta.");
        if (currentUser.getAuthorities().stream().findFirst().orElseThrow().getAuthority().equals("ADMIN")) {
            return "redirect:/offers/all";
        }
        return "redirect:/offers";
    }

    private void prepareModelForOfferForm(Model model) {
        List<Boat> userBoats = boatService.findAllByOwnerId(userService.getAuthenticatedUserId());
        model.addAttribute("userBoats", userBoats);
    }

    private String processOffer(Offer offer, BindingResult bindingResult, Model model,
            LocalDate startDate, LocalDate endDate,
            RedirectAttributes redirectAttributes, boolean isNew, UserDetails currentUser) {
        validateOfferDates(offer, bindingResult, startDate, endDate);

        if (bindingResult.hasErrors()) {
            prepareModelForOfferForm(model);
            return isNew ? "offers/add" : "offers/edit";
        }

        offer.setStartDate(startDate);
        offer.setEndDate(endDate);
        offer.setOwnerId(userService.getAuthenticatedUserId());
        offerService.addOrUpdateOffer(offer);
        redirectAttributes.addFlashAttribute("successMessage", isNew ? "Oferta dodana." : "Oferta zaktualizowana.");
        if (currentUser.getAuthorities().stream().findFirst().orElseThrow().getAuthority().equals("ADMIN")) {
            return "redirect:/offers/all";
        }
        return "redirect:/offers";
    }


    private void validateOfferDates(Offer offer, BindingResult bindingResult, LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(LocalDate.now())) {
            bindingResult.rejectValue("startDate", "error.startDate", "Data rozpoczęcia oferty nie może być wcześniejsza niż dzisiejsza data.");
        }
        if (startDate.isAfter(endDate)) {
            bindingResult.rejectValue("endDate", "error.endDate", "Data zakończenia oferty nie może być wcześniejsza niż data rozpoczęcia oferty.");
        }
        if (offerService.isOverlapWithExistingOffers(offer.getId(), offer.getBoatId(), startDate, endDate)) {
            bindingResult.rejectValue("boatId", "error.boatId", "Istnieje już oferta dla tej łodzi w wybranym okresie czasu.");
        }
    }

}
