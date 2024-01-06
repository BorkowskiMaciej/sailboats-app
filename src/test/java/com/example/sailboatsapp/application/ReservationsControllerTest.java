package com.example.sailboatsapp.application;

import com.example.sailboatsapp.domain.reservation.ReservationService;
import com.example.sailboatsapp.domain.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ReservationsControllerTest {

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void createReservationTest() throws Exception {
        when(userService.getAuthenticatedUserId()).thenReturn(1L);

        mockMvc.perform(post("/reservations/new/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reservations/booked"));

        verify(reservationService).createReservation(1L, 1L);
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void showTenantsReservationsTest() throws Exception {
        when(userService.getAuthenticatedUserId()).thenReturn(1L);

        mockMvc.perform(get("/reservations/booked"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservations/bookedList"));

        verify(reservationService).findAllByTenantId(1L);
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    void showLandlordsReservationsTest() throws Exception {
        when(userService.getAuthenticatedUserId()).thenReturn(1L);

        mockMvc.perform(get("/reservations/hosted"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservations/hostedList"));

        verify(reservationService).findAllLandlordId(1L);
    }

    @Test
    void cancelReservationTest() throws Exception {
        mockMvc.perform(post("/reservations/cancel/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reservations/booked"));

        verify(reservationService).deleteReservation(1L);
    }

    @Test
    void deleteReservationTest() throws Exception {
        mockMvc.perform(post("/reservations/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reservations/hosted"));

        verify(reservationService).deleteReservation(1L);
    }
}
