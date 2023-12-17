package com.example.sailboatsapp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendConfirmationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("sailboats@wp.pl");
        message.setTo(to);
        message.setSubject("Potwierdzenie rejestracji");
        message.setText("Twój kod potwierdzający to: " + code);
        emailSender.send(message);
    }

    public void sendResetPasswordCodeEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("sailboats@wp.pl");
        message.setTo(to);
        message.setSubject("Kod resetowania hasła");
        message.setText( "Twój kod resetowania hasła to: " + code);
        emailSender.send(message);
    }

}
