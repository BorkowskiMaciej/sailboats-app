package com.example.sailboatsapp.domain.offer;

import com.example.sailboatsapp.domain.boat.BoatService;
import com.example.sailboatsapp.domain.reservation.ReservationRepository;
import com.example.sailboatsapp.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final ReservationRepository reservationService;
    private final BoatService boatService;
    private final UserService userService;

    public void addOrUpdateOffer(Offer offer) {
        offerRepository.save(offer);
    }

    public List<Offer> findAllAvailableWithUserAndBoat() {
        return findAllWithUserAndBoat().stream()
                .filter(offer -> !isOfferReserved(offer.getId()))
                .collect(Collectors.toList());
    }

    public List<Offer> findAllWithUserAndBoat() {
        return offerRepository.findAll()
                .stream().peek(offer -> {
                    offer.setBoat(boatService.findById(offer.getBoatId()));
                    offer.setOwner(userService.findById(offer.getOwnerId()));
                }).toList();
    }

    public List<Offer> findAllWithUserAndBoatByOwnerId(Long ownerId) {
        return offerRepository.findAllByOwnerId(ownerId)
                .stream().peek(offer -> {
                    offer.setBoat(boatService.findById(offer.getBoatId()));
                    offer.setOwner(userService.findById(offer.getOwnerId()));
                }).toList();
    }

    public Offer findWithUserAndBoat(Long offerId) {
        return offerRepository.findById(offerId)
                .map(offer -> {
                    offer.setBoat(boatService.findById(offer.getBoatId()));
                    offer.setOwner(userService.findById(offer.getOwnerId()));
                    return offer;
                }).orElse(null);
    }

    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }

    public boolean isOfferReserved(Long offerId) {
        return reservationService.existsByOfferId(offerId);
    }

    public boolean isOverlapWithExistingOffers(Long offerId, Long boatId, LocalDate startDate, LocalDate endDate) {
        List<Offer> existingOffers = offerRepository.findByBoatId(boatId);

        for (Offer existingOffer : existingOffers) {
            if (existingOffer.getId().equals(offerId)) {
                continue;
            }
            if (!(endDate.isBefore(existingOffer.getStartDate()) || startDate.isAfter(existingOffer.getEndDate()))) {
                return true;
            }
        }

        return false;
    }

}
