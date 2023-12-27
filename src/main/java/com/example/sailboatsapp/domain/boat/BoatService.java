package com.example.sailboatsapp.domain.boat;

import com.example.sailboatsapp.domain.boat.model.Boat;
import com.example.sailboatsapp.domain.boat.repository.BoatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoatService {

    private final BoatRepository boatRepository;

    public void addBoat(Boat boat) {
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

    public void updateBoat(Boat boat) {
        boatRepository.save(boat);
    }

}
