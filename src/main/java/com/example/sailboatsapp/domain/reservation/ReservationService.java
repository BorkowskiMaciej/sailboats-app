package com.example.sailboatsapp.domain.reservation;

import com.example.sailboatsapp.domain.offer.OfferService;
import com.example.sailboatsapp.domain.reservation.model.Reservation;
import com.example.sailboatsapp.domain.reservation.repository.ReservationRepository;
import com.example.sailboatsapp.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final OfferService offerService;
    private final UserService userService;

    public void createReservation(Long offerId, Long tenantId) {
        Reservation reservation = Reservation.builder()
                .offerId(offerId)
                .landlordId(offerService.findWithUserAndBoat(offerId).getOwnerId())
                .tenantId(tenantId)
                .build();
        reservationRepository.save(reservation);
    }

    public List<Reservation> findAllByTenantId(Long tenantId) {
        return reservationRepository.findAllByTenantId(tenantId)
                .stream()
                .map(reservation -> reservation.toBuilder()
                        .offer(offerService.findWithUserAndBoat(reservation.getOfferId()))
                        .tenant(userService.findById(reservation.getTenantId()))
                        .landlord(userService.findById(reservation.getLandlordId()))
                        .build())
                .toList();
    }

    public List<Reservation> findAllLandlordId(Long landlordId) {
        return reservationRepository.findAllByLandlordId(landlordId)
                .stream()
                .map(reservation -> reservation.toBuilder()
                        .offer(offerService.findWithUserAndBoat(reservation.getOfferId()))
                        .landlord(userService.findById(reservation.getLandlordId()))
                        .tenant(userService.findById(reservation.getTenantId()))
                        .build())
                .toList();
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

}
