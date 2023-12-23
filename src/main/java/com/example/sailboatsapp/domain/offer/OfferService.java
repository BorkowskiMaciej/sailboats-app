package com.example.sailboatsapp.domain.offer;

import com.example.sailboatsapp.domain.boat.BoatService;
import com.example.sailboatsapp.domain.offer.model.Offer;
import com.example.sailboatsapp.domain.offer.repository.OfferRepository;
import com.example.sailboatsapp.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final BoatService boatService;
    private final UserService userService;

    public void addOffer(Offer offer) {
        offerRepository.save(offer);
    }

    public List<Offer> findAllWithUserAndBoat() {
        return offerRepository.findAll()
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
}
