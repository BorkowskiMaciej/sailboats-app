package com.example.sailboatsapp.domain.boat;

import com.example.sailboatsapp.domain.boat.entity.Boat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoatRepository extends JpaRepository<Boat, Long> {

    List<Boat> findAllByOwnerId(Long ownerId);

    Boat findByName(String name);
}
