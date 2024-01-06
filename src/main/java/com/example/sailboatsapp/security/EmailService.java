package com.example.sailboatsapp.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender emailSender;
    private final String senderEmailAddress;

    public EmailService(
            JavaMailSender emailSender,
            @Value("${spring.mail.username}") String senderEmailAddress) {
        this.emailSender = emailSender;
        this.senderEmailAddress = senderEmailAddress;
    }

    public void sendConfirmationCode(String to, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmailAddress);
            message.setTo(to);
            message.setSubject("[BoroJacht] Aktywacja konta");
            message.setText("Twój kod aktywujący to: " + code);
            emailSender.send(message);
        }
        catch (Exception e) {
            log.error("Błąd przy wysyłaniu e-maila: " + e.getMessage());
        }
    }

    public void sendResetPasswordCode(String to, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmailAddress);
            message.setTo(to);
            message.setSubject("[BoroJacht] Kod resetowania hasła");
            message.setText( "Twój kod resetowania hasła to: " + code);
            emailSender.send(message);
        }
        catch (Exception e) {
            log.error("Błąd przy wysyłaniu e-maila: " + e.getMessage());
        }
    }

}
