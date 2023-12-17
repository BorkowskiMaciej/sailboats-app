package com.example.sailboatsapp.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
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
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmailAddress);
        message.setTo(to);
        message.setSubject("[Sailboats] Potwierdzenie rejestracji");
        message.setText("Twój kod potwierdzający to: " + code);
        emailSender.send(message);
    }

    public void sendResetPasswordCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmailAddress);
        message.setTo(to);
        message.setSubject("[Sailboats] Kod resetowania hasła");
        message.setText( "Twój kod resetowania hasła to: " + code);
        emailSender.send(message);
    }

}
