package com.example.sailboatsapp.domain.user.repository;

import com.example.sailboatsapp.domain.user.model.AppUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByConfirmationCode(String confirmationCode);

    @Modifying
    @Transactional
    @Query("UPDATE AppUser u SET u.isConfirmed = true WHERE u.username = :username")
    void confirmUser(String username);

    @Modifying
    @Transactional
    @Query("UPDATE AppUser u SET u.resetPasswordCode = :resetPasswordCode WHERE u.username = :username")
    void setResetPasswordCode(String username, String resetPasswordCode);

    @Modifying
    @Transactional
    @Query("UPDATE AppUser u SET u.password = :password WHERE u.username = :username")
    void updatePassword(String username, String password);

    @Modifying
    @Transactional
    @Query("UPDATE AppUser u SET u.confirmationCode = null WHERE u.username = :username")
    void clearConfirmationCode(String username);

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByResetPasswordCode(String resetCode);

    @Modifying
    @Transactional
    void deleteByUsername(String username);

}
