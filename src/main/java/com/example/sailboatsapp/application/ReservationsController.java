package com.example.sailboatsapp.application;

import com.example.sailboatsapp.domain.reservation.ReservationService;
import com.example.sailboatsapp.domain.reservation.model.Reservation;
import com.example.sailboatsapp.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationsController {

    private final ReservationService reservationService;
    private final UserService userService;

    @PostMapping("/new/{offerId}")
    public String createReservation(@PathVariable Long offerId,  RedirectAttributes redirectAttributes) {
        reservationService.createReservation(offerId, userService.getAuthenticatedUserId());
        redirectAttributes.addFlashAttribute("successMessage", "Dokonano rezerwacji.");
        return "redirect:/reservations/booked";
    }

    @GetMapping("/booked")
    public String showTenantsReservations(Model model) {
        List<Reservation> reservations = reservationService.findAllByTenantId(userService.getAuthenticatedUserId());
        model.addAttribute("reservations", reservations);
        return "reservations/bookedList";
    }

    @GetMapping("/hosted")
    public String showLandlordsReservations(Model model) {
        List<Reservation> reservations = reservationService.findAllLandlordId(userService.getAuthenticatedUserId());
        model.addAttribute("reservations", reservations);
        return "reservations/hostedList";
    }

    @PostMapping("/cancel/{id}")
    public String cancelReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reservationService.deleteReservation(id);
        redirectAttributes.addFlashAttribute("successMessage", "Rezerwacja została anulowana.");
        return "redirect:/reservations/booked";
    }

    @PostMapping("/delete/{id}")
    public String deleteReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reservationService.deleteReservation(id);
        redirectAttributes.addFlashAttribute("successMessage", "Rezerwacja została usunięta.");
        return "redirect:/reservations/hosted";
    }

}
