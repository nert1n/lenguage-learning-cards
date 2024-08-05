package com.api.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailUtils {
    private JavaMailSender mailSender;
    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(subject);
        mailMessage.setTo(emailTo);
        mailMessage.setFrom("");
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}
