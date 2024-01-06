package com.example.sailboatsapp.domain.reservation;

import com.example.sailboatsapp.domain.offer.Offer;
import com.example.sailboatsapp.domain.offer.OfferService;
import com.example.sailboatsapp.domain.user.AppUser;
import com.example.sailboatsapp.domain.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private OfferService offerService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void createReservationTest() {
        Long offerId = 1L;
        Long tenantId = 1L;
        AppUser landlord = new AppUser();
        landlord.setId(2L);
        Offer offer = new Offer();
        offer.setOwnerId(landlord.getId());

        when(offerService.findWithUserAndBoat(offerId)).thenReturn(offer);

        reservationService.createReservation(offerId, tenantId);

        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void findAllByTenantIdTest() {
        Long tenantId = 1L;
        List<Reservation> mockReservations = List.of(new Reservation());

        when(reservationRepository.findAllByTenantId(tenantId)).thenReturn(mockReservations);
        when(offerService.findWithUserAndBoat(anyLong())).thenReturn(new Offer());
        when(userService.findById(anyLong())).thenReturn(new AppUser());

        List<Reservation> result = reservationService.findAllByTenantId(tenantId);

        assertNotNull(result);
        assertEquals(mockReservations.size(), result.size());
    }

    @Test
    void findAllLandlordIdTest() {
        Long landlordId = 2L;
        List<Reservation> mockReservations = List.of(new Reservation());

        when(reservationRepository.findAllByLandlordId(landlordId)).thenReturn(mockReservations);
        when(offerService.findWithUserAndBoat(anyLong())).thenReturn(new Offer());
        when(userService.findById(anyLong())).thenReturn(new AppUser());

        List<Reservation> result = reservationService.findAllLandlordId(landlordId);

        assertNotNull(result);
        assertEquals(mockReservations.size(), result.size());
    }

    @Test
    void deleteReservationTest() {
        Long reservationId = 1L;

        reservationService.deleteReservation(reservationId);

        verify(reservationRepository).deleteById(reservationId);
    }
}
