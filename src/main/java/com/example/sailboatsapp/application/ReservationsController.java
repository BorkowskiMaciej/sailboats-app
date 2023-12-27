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

import java.util.List;

@Controller
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationsController {

    private final ReservationService reservationService;
    private final UserService userService;

    @PostMapping("/new/{offerId}")
    public String createReservation(@PathVariable Long offerId) {
        reservationService.createReservation(offerId, userService.getAuthenticatedUserId());
        return "redirect:/reservations/booked";
    }

    @GetMapping("/booked")
    public String showTenantsReservations(Model model) {
        List<Reservation> reservations = reservationService.findAllByTenantId(userService.getAuthenticatedUserId());
        model.addAttribute("reservations", reservations);
        return "reservations/list";
    }

    @GetMapping("/hosted")
    public String showLandlordsReservations(Model model) {
        List<Reservation> reservations = reservationService.findAllLandlordId(userService.getAuthenticatedUserId());
        model.addAttribute("reservations", reservations);
        return "reservations/list";
    }

}
