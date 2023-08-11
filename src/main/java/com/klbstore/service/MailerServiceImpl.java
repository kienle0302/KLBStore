package com.klbstore.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.klbstore.model.MailInfo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailerServiceImpl implements MailerService {

    @Autowired
    private JavaMailSender sender;

    private List<MailInfo> queue = new ArrayList<>();

    @Override
    public void queue(MailInfo mail) {
        queue.add(mail);
    }

    @Override
    public void queue(String to, String subject, String body) {
        queue(new MailInfo(to, subject, body));
    }
    @Override
    public void queue(String from, String to, String subject, String body) {
        queue(new MailInfo(from, to, subject, body));
    }

    @Override
    @Scheduled(fixedDelay = 5000) // Chạy mỗi 5 giây
    public void run() {
        while (!queue.isEmpty()) {
            MailInfo mail = queue.remove(0);
            try {
                this.send(mail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void send(MailInfo mail) throws MessagingException, IOException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom(mail.getFrom());
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getBody(), true);
        helper.setReplyTo(mail.getFrom());

        String[] cc = mail.getCc();
        if (cc != null && cc.length > 0) {
            helper.setCc(cc);
        }

        String[] bcc = mail.getBcc();
        if (bcc != null && bcc.length > 0) {
            helper.setBcc(bcc);
        }

        MultipartFile[] attachments = mail.getAttachments();
        if (attachments != null && attachments.length > 0) {
            for (MultipartFile attachment : attachments) {
                InputStreamSource attachmentSource = new ByteArrayResource(attachment.getBytes());
                helper.addAttachment(attachment.getOriginalFilename(), attachmentSource);
            }
        }

        sender.send(message);
    }

    @Override
    public void send(String to, String subject, String body) throws MessagingException {
        MailInfo mail = new MailInfo(to, subject, body);
        queue(mail);
    }
}
