package com.example.sailboatsapp.domain.boat;

import com.example.sailboatsapp.domain.boat.entity.Boat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoatService {

    private final BoatRepository boatRepository;

    public void addOrUpdateBoat(Boat boat) {
        boatRepository.save(boat);
    }

    public List<Boat> findAllByOwnerId(Long ownerId) {
        return boatRepository.findAllByOwnerId(ownerId);
    }

    public Boat findById(Long boatId) {
        return boatRepository.findById(boatId).orElseThrow();
    }

    public void deleteBoat(Long id) {
        boatRepository.deleteById(id);
    }

    public boolean checkIfBoatExists(Long id, String name) {
        Boat existingBoat = boatRepository.findByName(name);
        if (existingBoat == null) {
            return false;
        }
        return !existingBoat.getId().equals(id);
    }

}
