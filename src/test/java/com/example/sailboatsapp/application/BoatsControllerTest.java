package com.example.sailboatsapp.application;

import com.example.sailboatsapp.domain.boat.BoatService;
import com.example.sailboatsapp.domain.boat.entity.Boat;
import com.example.sailboatsapp.domain.boat.entity.BoatType;
import com.example.sailboatsapp.domain.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoatsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BoatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoatService boatService;

    @MockBean
    private UserService userService;

    private Boat testBoat;
    private List<Boat> boatList;

    @BeforeEach
    void setUp() {
        testBoat = Boat.builder()
                .id(1L)
                .name("TestBoat")
                .type(BoatType.SAILBOAT)
                .model("Model123")
                .maxHeadcount(5)
                .cabinsNumber(2)
                .prodYear(2020)
                .enginePower(250)
                .ownerId(1L)
                .image(null)
                .imageName("boat.jpg")
                .build();

        boatList = Collections.singletonList(testBoat);

        when(boatService.findAll()).thenReturn(boatList);
        when(boatService.findAllByOwnerId(anyLong())).thenReturn(boatList);
        when(boatService.findById(anyLong())).thenReturn(testBoat);
        when(userService.getAuthenticatedUserId()).thenReturn(1L);
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    void listBoats_ShouldReturnBoatsList() throws Exception {
        mockMvc.perform(get("/boats"))
                .andExpect(status().isOk())
                .andExpect(view().name("boats/list"))
                .andExpect(model().attribute("boats", hasSize(1)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void listAllBoats_ShouldReturnAllBoatsList() throws Exception {
        mockMvc.perform(get("/boats/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("boats/list"))
                .andExpect(model().attribute("boats", hasSize(1)));
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    void addBoat_ShouldAddBoat() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

        mockMvc.perform(multipart("/boats/add")
                        .file(file)
                        .param("name", "TestBoat")
                        .param("model", "TestModel")
                        .param("type", "SAILBOAT")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boats"));

        ArgumentCaptor<Boat> boatCaptor = ArgumentCaptor.forClass(Boat.class);
        verify(boatService).addOrUpdateBoat(boatCaptor.capture());
        assertEquals("TestBoat", boatCaptor.getValue().getName());
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    void deleteBoat_ShouldDeleteBoat() throws Exception {
        mockMvc.perform(post("/boats/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/boats"));

        verify(boatService).deleteBoat(1L);
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    void showAddBoatFormTest() throws Exception {
        mockMvc.perform(get("/boats/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("boats/add"))
                .andExpect(model().attributeExists("boat"));
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    void showEditFormTest() throws Exception {
        mockMvc.perform(get("/boats/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("boats/edit"))
                .andExpect(model().attributeExists("boat"));
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    void getBoatImageTest() throws Exception {
        when(boatService.findById(1L)).thenReturn(testBoat);
        testBoat.setImage("test image content".getBytes());

        mockMvc.perform(get("/boats/image/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));

        verify(boatService).findById(1L);
    }



}
