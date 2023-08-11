package com.klbstore.extensions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klbstore.service.MailerService;
@Service
public class OtpService {
    @Autowired
    private MailerService mailerService;

    public String generateAndSendOtp(String email, String subject) {
        String otp = OtpGenerator.generateOtp("otp_key");
        String to = email;
        String body = generateEmailTemplate(otp);

        mailerService.queue(to, subject, body);
        return otp;
    }
    
    private String generateEmailTemplate(String otp) {
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
                "        }\n" +
                "\n" +
                "        .logo {\n" +
                "            text-align: center;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .p {\n" +
                "            text-align: justify;\n" +
                "        }\n" +
                "\n" +
                "        .content {\n" +
                "            padding: 20px;\n" +
                "            background-color: #ffffff;\n" +
                "        }\n" +
                "\n" +
                "        .code {\n" +
                "            background-color: #FFF4CE;\n" +
                "            padding: 8px 16px;\n" +
                "            font-size: 14px;\n" +
                "            color: #333333;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            color: #333333;\n" +
                "            font-size: 10px;\n" +
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
                "            <p>Để đảm bảo an toàn, bạn cần nhập mã dưới đây để xác minh tài khoản. Mã này chỉ hoạt động trong vòng 15 phút và nếu bạn yêu cầu một mã mới, mã này sẽ ngừng hoạt động.</p>\n"
                +
                "            <div class=\"code\">\n" +
                "                <p>Mã xác minh tài khoản:</p>\n" +
                "                <p><strong>" + otp + "</strong></p>\n" +
                "            </div>\n" +
                "            <p><strong>Gặp vấn đề với mã này?</strong></p>\n" +
                "            <p>Xem lỗi và đảm bảo rằng email của bạn vẫn đang hoạt động. Nếu không phải, hãy tìm kiếm một email cập nhật hoặc yêu cầu một mã mới.</p>\n"
                +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            &copy; 2023 KLBStore\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
