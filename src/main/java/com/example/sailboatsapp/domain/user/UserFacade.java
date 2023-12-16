package com.example.sailboatsapp.domain.user;

import com.example.sailboatsapp.domain.user.model.AppUser;
import com.example.sailboatsapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserRepository userRepository;

    public void addUser(AppUser appUser) {
        userRepository.save(appUser);
    }

    public boolean checkIfUserExists(String username) {
        return userRepository.existsByUsername(username);
    }

}
