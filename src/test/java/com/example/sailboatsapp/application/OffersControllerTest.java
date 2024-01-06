package com.example.sailboatsapp.application;

import com.example.sailboatsapp.domain.boat.BoatService;
import com.example.sailboatsapp.domain.boat.entity.Boat;
import com.example.sailboatsapp.domain.offer.Offer;
import com.example.sailboatsapp.domain.offer.OfferService;
import com.example.sailboatsapp.domain.user.AppUser;
import com.example.sailboatsapp.domain.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class OffersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferService offerService;

    @MockBean
    private BoatService boatService;

    @MockBean
    private UserService userService;
    @Test
    @WithMockUser(roles = {"OWNER"})
    public void shouldReturnOffersForOwner() throws Exception {
        mockMvc.perform(get("/offers"))
                .andExpect(status().isOk())
                .andExpect(view().name("offers/list"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void shouldReturnAllOffersForAdmin() throws Exception {
        mockMvc.perform(get("/offers/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("offers/list"));
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    public void showNewOfferFormTest() throws Exception {
        mockMvc.perform(get("/offers/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("offers/add"));
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    public void createNewOfferTest() throws Exception {
        mockMvc.perform(post("/offers/new")
                        .param("port", "Testowy port")
                        .param("price", "500")
                        .param("deposit", "100")
                        .param("startDate", "2024-06-01")
                        .param("endDate", "2024-06-10")
                        .param("description", "Testowa oferta"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/offers"));
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    public void showEditFormTest() throws Exception {
        Long offerId = 1L;
        when(offerService.findWithUserAndBoat(offerId)).thenReturn(new Offer());
        mockMvc.perform(get("/offers/update/" + offerId))
                .andExpect(status().isOk())
                .andExpect(view().name("offers/edit"));
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    public void updateOfferTest() throws Exception {
        mockMvc.perform(post("/offers/update/1")
                        .param("port", "Zaktualizowany port")
                        .param("price", "600")
                        .param("deposit", "150")
                        .param("startDate", "2024-07-01")
                        .param("endDate", "2024-07-15")
                        .param("description", "Zaktualizowana oferta"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/offers"));
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    public void deleteBoatTest() throws Exception {
        mockMvc.perform(post("/offers/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/offers"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void showOfferDetailsTest_ExistingOffer() throws Exception {
        Long offerId = 1L;
        Offer mockOffer = new Offer();
        mockOffer.setId(offerId);

        Boat mockBoat = new Boat();
        mockBoat.setId(1L);
        mockBoat.setName("Test Boat");
        mockOffer.setBoat(mockBoat);

        AppUser mockOwner = new AppUser();
        mockOwner.setId(1L);
        mockOwner.setUsername("TestUser");
        mockOffer.setOwner(mockOwner);

        when(offerService.findWithUserAndBoat(offerId)).thenReturn(mockOffer);
        when(offerService.isOfferReserved(offerId)).thenReturn(false);

        mockMvc.perform(get("/offers/show/" + offerId))
                .andExpect(status().isOk())
                .andExpect(view().name("offers/detail"));
    }


    @Test
    @WithMockUser(roles = {"USER"})
    public void showOfferDetailsTest_NonExistingOffer() throws Exception {
        Long offerId = 2L;
        when(offerService.findWithUserAndBoat(offerId)).thenReturn(null);

        mockMvc.perform(get("/offers/show/" + offerId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/offers"));
    }


}
