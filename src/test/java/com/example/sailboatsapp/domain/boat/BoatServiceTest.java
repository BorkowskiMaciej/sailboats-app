package com.example.sailboatsapp.domain.boat;

import com.example.sailboatsapp.domain.boat.entity.Boat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BoatServiceTest {

    @Mock
    private BoatRepository boatRepository;

    private BoatService boatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        boatService = new BoatService(boatRepository);
    }

    @Test
    void shouldAddNewBoatSuccessfully() {
        Boat boat = new Boat();
        boat.setName("Test Boat");
        boat.setOwnerId(1L);

        boatService.addOrUpdateBoat(boat);

        verify(boatRepository, times(1)).save(boat);
    }

    @Test
    void shouldDeleteBoatById() {
        Long boatId = 1L;

        boatService.deleteBoat(boatId);

        verify(boatRepository, times(1)).deleteById(boatId);
    }

    @Test
    void shouldFindBoatById() {
        Long boatId = 1L;
        Boat boat = new Boat();
        boat.setId(boatId);

        when(boatRepository.findById(boatId)).thenReturn(Optional.of(boat));

        Boat foundBoat = boatService.findById(boatId);

        assertEquals(boatId, foundBoat.getId());
    }

}
