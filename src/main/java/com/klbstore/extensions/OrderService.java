package com.klbstore.extensions;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klbstore.model.ChiTietDonHang;
import com.klbstore.model.DonHang;
import com.klbstore.service.MailerService;

@Service
public class OrderService {
    @Autowired
    private MailerService mailerService;

    public void sendEmailOrderSuccess(DonHang donHang, List<ChiTietDonHang> chiTietDonHangList) {
        String to = donHang.getNguoiDung().getEmail();
        String body = generateEmailTemplate(donHang, chiTietDonHangList);

        mailerService.queue(to, "ĐẶT HÀNG THÀNH CÔNG", body);
    }

    public void sendEmailCheckoutSuccess(DonHang donHang) {
        String to = donHang.getNguoiDung().getEmail();
        String body = emailCheckout(donHang);

        mailerService.queue(to, "THANH TOÁN ĐƠN HÀNG THÀNH CÔNG", body);
    }

    private String convertDate(LocalDateTime ngayDatHang) {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public String formatCurrency(Double amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormatter.format(amount);
    }

    public String getTotalAmountCart(List<ChiTietDonHang> list) {
        Double total = 0.0;
        for (ChiTietDonHang ctdh : list) {
            total += ctdh.getGiaBan() * ctdh.getSoLuong();
        }
        return formatCurrency(total);
    }

    private String generateEmailTemplate(DonHang donHang, List<ChiTietDonHang> chiTietDonHang) {
        String productsHtml = "";
        for (ChiTietDonHang donHangChiTiet : chiTietDonHang) {
            String tenSanPham = donHangChiTiet.getSanPham().getTenSanPham();
            int soLuong = donHangChiTiet.getSoLuong();
            double giaBan = donHangChiTiet.getGiaBan();

            String productHtml = "<p>" +
                    "Sản phẩm: <strong>" + tenSanPham + "</strong><br>" +
                    "Số lượng: " + soLuong + "<br>" +
                    "Giá bán: " + formatCurrency(giaBan) + "<br>" +
                    "</p>";

            productsHtml += productHtml;
        }
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
                "            <img src=\"https://firebasestorage.googleapis.com/v0/b/fir-e2be5.appspot.com/o/user%2Fimages%2Fmenu%2Flogo%2Flogo.png?alt=media&token=d3f193ce-da9c-4f67-a50e-f998785f29fb\" alt=\"KLBStore\" height=\"80\">\n"
                +

                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p>Xin chào, " + donHang.getNguoiDung().getHoTen() + "</p>\n" +
                "            <p>Cảm ơn bạn đã đặt hàng từ cửa hàng của chúng tôi. Đơn hàng của bạn đã được tiếp nhận và đang được xử lý.</p>\n"
                +
                "            <div class=\"code\">\n" +
                "                <p>Thông tin đơn hàng:</p>\n" +
                "                <p>" + "Mã đơn hàng: " + "<strong>" + donHang.getDonHangId() + "</strong></p>\n" +
                "                " + productsHtml + "\n" +
                "                <p>" + "Ngày đặt hàng: " + "<strong>" + convertDate(donHang.getNgayDatHang())
                + "</strong></p>\n" +
                "                <p>" + "Số điện thoại: " + "<strong>" + donHang.getNguoiDung().getHoTen()
                + "</strong></p>\n" +
                "                <p>" + "Tổng cộng: " + "<strong>"
                + getTotalAmountCart(chiTietDonHang) + "</strong></p>\n" +
                "            </div>\n" +
                "            <p>Ch\u00FAng t\u00F4i s\u1EBD li\u00EAn h\u1EC7 v\u1EDBi b\u1EA1n trong th\u1EDDi gian s\u1EDBm nh\u1EA5t \u0111\u1EC3 x\u00E1c nh\u1EADn \u0111\u01A1n h\u00E0ng v\u00E0 th\u00F4ng b\u00E1o v\u1EC1 qu\u00E1 tr\u00ECnh v\u1EADn chuy\u1EC3n. N\u1EBFu b\u1EA1n c\u00F3 b\u1EA5t k\u1EF3 c\u00E2u h\u1ECFi n\u00E0o, vui l\u00F2ng li\u00EAn h\u1EC7 v\u1EDBi ch\u00FAng t\u00F4i qua th\u00F4ng tin li\u00EAn l\u1EA1c d\u01B0\u1EDBi \u0111\u00E2y.</p>\n"
                +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            &copy; 2023 KLBStore\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    private String emailCheckout(DonHang donHang) {
        String productsHtml = "";
        for (ChiTietDonHang donHangChiTiet : donHang.getDonHangChiTietDonHangs()) {
            String tenSanPham = donHangChiTiet.getSanPham().getTenSanPham();
            int soLuong = donHangChiTiet.getSoLuong();
            double giaBan = donHangChiTiet.getGiaBan();

            String productHtml = "<p>" +
                    "Sản phẩm: <strong>" + tenSanPham + "</strong><br>" +
                    "Số lượng: " + soLuong + "<br>" +
                    "Giá bán: " + formatCurrency(giaBan) + "<br>" +
                    "</p>";

            productsHtml += productHtml;
        }

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
                "            <img src=\"https://firebasestorage.googleapis.com/v0/b/fir-e2be5.appspot.com/o/user%2Fimages%2Fmenu%2Flogo%2Flogo.png?alt=media&token=d3f193ce-da9c-4f67-a50e-f998785f29fb\" alt=\"KLBStore\" height=\"80\">\n"
                +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p>Xin chào, " + donHang.getNguoiDung().getHoTen() + "</p>\n" +
                "            <p>Cảm ơn bạn đã thanh toán thành công. Đơn hàng của bạn đã được xử lý và sẽ được giao trong thời gian sớm nhất.</p>\n"
                +
                "            <div class=\"code\">\n" +
                "                <p>Thông tin đơn hàng:</p>\n" +
                "                <p>Mã đơn hàng: <strong>" + donHang.getDonHangId() + "</strong></p>\n" +
                "                " + productsHtml + "\n" +
                "                <p>Ngày đặt hàng: <strong>" + convertDate(donHang.getNgayDatHang()) + "</strong></p>\n"
                +
                "                <p>Số điện thoại: <strong>" + donHang.getNguoiDung().getSdt() + "</strong></p>\n" +
                "                <p>Tổng cộng: <strong>" + getTotalAmountCart(donHang.getDonHangChiTietDonHangs())
                + "</strong></p>\n" +
                "            </div>\n" +
                "            <p>Chúng tôi sẽ liên hệ với bạn trong trường hợp cần thiết. Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi qua thông tin liên lạc dưới đây.</p>\n"
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
