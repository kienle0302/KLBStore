package com.klbstore.service;  
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;

import com.klbstore.model.MailInfo;

import jakarta.mail.MessagingException;

public interface MailerService {
    void send(MailInfo mail) throws MessagingException, IOException;

    void send(String to, String subject, String body) throws MessagingException;

    List<MailInfo> list = new ArrayList<>();

    public void queue(MailInfo mail);

    public void queue(String to, String subject, String body);
    public void queue(String from,String to, String subject, String body);
    @Scheduled(fixedDelay = 5000)
    public void run();
}
