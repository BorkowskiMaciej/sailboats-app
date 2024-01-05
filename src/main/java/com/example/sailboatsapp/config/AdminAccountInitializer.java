package com.example.sailboatsapp.config;

import com.example.sailboatsapp.domain.user.AppUser;
import com.example.sailboatsapp.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminAccountInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdminAccount() {
        if (!userRepository.existsByUsername("admin")) {
            AppUser adminUser = new AppUser();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("adminadmin"));
            adminUser.setEmail("admin@admin");
            adminUser.setName("admin");
            adminUser.setSurname("admin");
            adminUser.setPhoneNumber("123456789");
            adminUser.setIsCompany(false);
            adminUser.setIsConfirmed(true);
            adminUser.setRoles(Set.of("ADMIN"));
            userRepository.save(adminUser);
        }
    }


}
