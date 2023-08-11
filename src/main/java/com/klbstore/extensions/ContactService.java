package com.klbstore.extensions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klbstore.service.MailerService;

@Service
public class ContactService {
    @Autowired
    private MailerService mailerService;

   public void receiveContactFormSubmission(String name, String email, String message) {
        String to = "kien21265@gmail.com";
        String subject = "Liên hệ mới từ khách hàng: " + name;
        String body = generateEmailTemplate(name, email, message);
        mailerService.queue(email, to, subject, body);
        String replySubject = "Cảm ơn bạn đã liên hệ";
        String replyBody = generateReplyEmailTemplate(message);
        mailerService.queue(email, replySubject, replyBody);
    }

private String generateEmailTemplate(String name, String email, String message) {
    return "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Email Template</title>\n" +
            "    <style>\n" +
            "        body {\n" +
            "            color: #333333;\n" +
            "            font-family: 'Segoe UI', Arial, sans-serif;\n" +
            "            font-size: 14px;\n" +
            "            padding: 8px 16px 0px 16px;\n" +
            "            line-height: 1.5;\n" +
            "        }\n" +
            "\n" +
            "        .container {\n" +
            "            max-width: 600px;\n" +
            "            margin: 0 auto;\n" +
            "            background-color: #f9f9f9;\n" +
            "            padding: 20px;\n" +
            "            border: 1px solid #e8e8e8;\n" +
            "        }\n" +
            "\n" +
            "        .header {\n" +
            "            text-align: center;\n" +
            "            margin-bottom: 20px;\n" +
            "        }\n" +
            "\n" +
            "        .content {\n" +
            "            background-color: #ffffff;\n" +
            "            padding: 20px;\n" +
            "            border: 1px solid #e8e8e8;\n" +
            "            border-top: none;\n" +
            "        }\n" +
            "\n" +
            "        .message {\n" +
            "            margin-bottom: 20px;\n" +
            "        }\n" +
            "\n" +
            "        .footer {\n" +
            "            text-align: center;\n" +
            "            color: #999999;\n" +
            "            font-size: 12px;\n" +
            "            margin-top: 20px;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <div class=\"header\">\n" +
            "            <img src=\"https://firebasestorage.googleapis.com/v0/b/fir-e2be5.appspot.com/o/user%2Fimages%2Fmenu%2Flogo%2Flogo.png?alt=media&token=d3f193ce-da9c-4f67-a50e-f998785f29fb\" alt=\"KLBStore Logo\" height=\"80\">\n" +
            "            <h2>Liên hệ mới từ khách hàng: " + name + "</h2>\n" +
            "        </div>\n" +
            "        <div class=\"content\">\n" +
            "            <div class=\"message\">\n" +
            "                <p><strong>Tên:</strong> " + name + "</p>\n" +
            "                <p><strong>Email:</strong> " + email + "</p>\n" +
            "                <p><strong>Nội dung:</strong></p>\n" +
            "                <p>" + message + "</p>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div class=\"footer\">\n" +
            "            &copy; " + java.time.Year.now().getValue() + " KLBStore. Tất cả các quyền đã được bảo lưu.\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";
}

    private String generateReplyEmailTemplate(String message) {
    return "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Email Template</title>\n" +
            "    <style>\n" +
            "        body {\n" +
            "            color: #333333;\n" +
            "            font-family: 'Segoe UI', Arial, sans-serif;\n" +
            "            font-size: 14px;\n" +
            "            padding: 8px 16px 0px 16px;\n" +
            "            line-height: 1.5;\n" +
            "        }\n" +
            "\n" +
            "        .container {\n" +
            "            max-width: 600px;\n" +
            "            margin: 0 auto;\n" +
            "            background-color: #ffffff;\n" +
            "            padding: 20px;\n" +
            "            border: 1px solid #e8e8e8;\n" +
            "        }\n" +
            "\n" +
            "        .logo {\n" +
            "            text-align: center;\n" +
            "            margin-bottom: 20px;\n" +
            "        }\n" +
            "\n" +
            "        .content {\n" +
            "            padding: 20px;\n" +
            "        }\n" +
            "\n" +
            "        .message {\n" +
            "            margin-bottom: 20px;\n" +
            "        }\n" +
            "\n" +
            "        .footer {\n" +
            "            text-align: center;\n" +
            "            color: #333333;\n" +
            "            font-size: 12px;\n" +
            "            margin-top: 20px;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <div class=\"logo\">\n" +
            "            <img src=\"https://firebasestorage.googleapis.com/v0/b/fir-e2be5.appspot.com/o/user%2Fimages%2Fmenu%2Flogo%2Flogo.png?alt=media&token=d3f193ce-da9c-4f67-a50e-f998785f29fb\" alt=\"KLBStore\" height=\"80\">\n" +
            "        </div>\n" +
            "        <div class=\"content\">\n" +
            "            <p>Xin chào,</p>\n" +
            "            <p>Cảm ơn bạn đã liên hệ với chúng tôi. Dưới đây là nội dung tin nhắn mà chúng tôi nhận được:</p>\n" +
            "            <div class=\"message\">\n" +
            "                <p><strong>" + message + "</strong></p>\n" +
            "            </div>\n" +
            "            <p>Chúng tôi sẽ xem xét và phản hồi lại bạn trong thời gian sớm nhất.</p>\n" +
            "        </div>\n" +
            "        <div class=\"footer\">\n" +
            "            &copy; " + java.time.Year.now().getValue() + " KLBStore. Tất cả các quyền đã được bảo lưu.\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";
}

}
