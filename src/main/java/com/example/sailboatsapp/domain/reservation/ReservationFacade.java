package com.example.sailboatsapp.domain.reservation;

import com.example.sailboatsapp.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationFacade {

    private final ReservationRepository reservationRepository;

}
