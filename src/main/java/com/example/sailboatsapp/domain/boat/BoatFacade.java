package com.example.sailboatsapp.domain.boat;

import com.example.sailboatsapp.domain.boat.model.Boat;
import com.example.sailboatsapp.domain.boat.repository.BoatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class BoatFacade {

    private final BoatRepository boatRepository;

    public void addBoat(Boat boat) {
        boatRepository.save(boat);
    }

    public Collection<Boat> findAll() {
        return boatRepository.findAll();
    }

    public Collection<Boat> findAllByOwnerId(Long ownerId) {
        return boatRepository.findAllByOwnerId(ownerId);
    }



}
