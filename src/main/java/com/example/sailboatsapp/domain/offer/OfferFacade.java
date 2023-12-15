package com.example.sailboatsapp.domain.offer;

import com.example.sailboatsapp.domain.offer.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfferFacade {

    private final OfferRepository offerRepository;

}
