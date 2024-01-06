package com.example.sailboatsapp.domain.offer;

import com.example.sailboatsapp.domain.boat.entity.Boat;
import com.example.sailboatsapp.domain.boat.BoatService;
import com.example.sailboatsapp.domain.reservation.ReservationRepository;
import com.example.sailboatsapp.domain.user.AppUser;
import com.example.sailboatsapp.domain.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private BoatService boatService;
    @Mock
    private UserService userService;

    @InjectMocks
    private OfferService offerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddOrUpdateOffer() {
        Offer offer = new Offer();
        offerService.addOrUpdateOffer(offer);
        verify(offerRepository).save(offer);
    }

    @Test
    public void testFindAllAvailableWithUserAndBoat() {
        Offer offer = new Offer();
        offer.setId(1L);
        when(offerRepository.findAll()).thenReturn(Collections.singletonList(offer));
        when(reservationRepository.existsByOfferId(1L)).thenReturn(false);

        List<Offer> availableOffers = offerService.findAllAvailableWithUserAndBoat();
        assertFalse(availableOffers.isEmpty());
        assertEquals(1, availableOffers.size());
    }

    @Test
    public void testFindWithUserAndBoat() {
        Long offerId = 1L;
        Offer offer = new Offer();
        offer.setId(offerId);
        offer.setBoatId(1L);
        offer.setOwnerId(1L);

        when(offerRepository.findById(offerId)).thenReturn(Optional.of(offer));
        when(boatService.findById(1L)).thenReturn(new Boat());
        when(userService.findById(1L)).thenReturn(new AppUser());

        Offer foundOffer = offerService.findWithUserAndBoat(offerId);
        assertNotNull(foundOffer);
        assertNotNull(foundOffer.getBoat());
        assertNotNull(foundOffer.getOwner());
    }

    @Test
    public void testFindAllWithUserAndBoat() {
        Offer offer = new Offer();
        offer.setId(1L);
        offer.setBoatId(1L);
        offer.setOwnerId(1L);

        when(offerRepository.findAll()).thenReturn(Collections.singletonList(offer));
        when(boatService.findById(1L)).thenReturn(new Boat());
        when(userService.findById(1L)).thenReturn(new AppUser());

        List<Offer> offers = offerService.findAllWithUserAndBoat();
        assertFalse(offers.isEmpty());
        assertEquals(1, offers.size());
        assertNotNull(offers.get(0).getBoat());
        assertNotNull(offers.get(0).getOwner());
    }

    @Test
    public void testFindAllWithUserAndBoatByOwnerId() {
        Long ownerId = 1L;
        Offer offer = new Offer();
        offer.setId(1L);
        offer.setBoatId(1L);
        offer.setOwnerId(ownerId);

        when(offerRepository.findAllByOwnerId(ownerId)).thenReturn(Collections.singletonList(offer));
        when(boatService.findById(1L)).thenReturn(new Boat());
        when(userService.findById(ownerId)).thenReturn(new AppUser());

        List<Offer> offers = offerService.findAllWithUserAndBoatByOwnerId(ownerId);
        assertFalse(offers.isEmpty());
        assertEquals(1, offers.size());
        assertNotNull(offers.get(0).getBoat());
        assertNotNull(offers.get(0).getOwner());
    }

    @Test
    public void testDeleteOffer() {
        Long offerId = 1L;
        offerService.deleteOffer(offerId);
        verify(offerRepository).deleteById(offerId);
    }

    @Test
    public void testIsOfferReserved() {
        Long offerId = 1L;
        when(reservationRepository.existsByOfferId(offerId)).thenReturn(true);

        assertTrue(offerService.isOfferReserved(offerId));
    }

    @Test
    public void testIsOverlapWithExistingOffers() {
        Long offerId = 1L, boatId = 1L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(10);

        Offer existingOffer = new Offer();
        existingOffer.setId(2L);
        existingOffer.setBoatId(boatId);
        existingOffer.setStartDate(startDate.minusDays(5));
        existingOffer.setEndDate(endDate.minusDays(5));

        when(offerRepository.findByBoatId(boatId)).thenReturn(Collections.singletonList(existingOffer));

        assertTrue(offerService.isOverlapWithExistingOffers(offerId, boatId, startDate, endDate));
    }

}
