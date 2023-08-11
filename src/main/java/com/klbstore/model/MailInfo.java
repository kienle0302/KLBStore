package com.klbstore.model;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailInfo {
    private String from;
    private String to;
    private String[] cc;
    private String[] bcc;
    private String subject;
    private String body;
    private MultipartFile[] attachments;

    public MailInfo(String to, String subject, String body) {
        this.from = "KLBStore <kien21265@gmail.com>";
        this.to = to;
        this.subject = subject;
        this.body = body;
    }
    public MailInfo(String from, String to, String subject, String body) {
        this.from = "KLB Customer <" + from + ">";
        this.to = to;
        this.subject = subject;
        this.body = body;
    }
}


