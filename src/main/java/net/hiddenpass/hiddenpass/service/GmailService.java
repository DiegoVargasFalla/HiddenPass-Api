package net.hiddenpass.hiddenpass.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;

public interface GmailService {
    void sendMail(String to, String subject, String url) throws MessagingException;
}
