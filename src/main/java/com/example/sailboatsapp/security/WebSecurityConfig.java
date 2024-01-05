package com.example.sailboatsapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/auth/*", "/style.css", "/css/**", "/js/**", "/images/**",
                                 "boats/image/*", "/offers/show/**").permitAll()
                        .requestMatchers( "/boats/all", "/offers/all", "/account/all", "/account/delete/*").hasAuthority("ADMIN")
                        .requestMatchers( "/boats/update/*", "/offers/update/*",
                                "/boats/delete/*", "/offers/delete/*").hasAnyAuthority("ADMIN", "OWNER")
                        .requestMatchers( "/boats", "/offers", "reservations/hosted").hasAnyAuthority("OWNER")
                        .requestMatchers( "/reservations/booked").hasAnyAuthority("USER")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/", true).permitAll()
                        .failureUrl("/auth/login?error=true"))
                .logout(config -> config
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .csrf().disable();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
