package com.klbstore.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.klbstore.config.SecurityRestTemplate;
import com.klbstore.config.VnpayConfig;
import com.klbstore.dao.ChiTietDonHangDAO;
import com.klbstore.dao.DonHangDAO;
import com.klbstore.dao.GiamGiaTrucTiepDAO;
import com.klbstore.dao.GioHangDAO;
import com.klbstore.dao.HinhThucThanhToanDAO;
import com.klbstore.dao.MaXacNhanDAO;
import com.klbstore.dao.MauSacDAO;
import com.klbstore.dao.NguoiDungDAO;
import com.klbstore.dao.SanPhamDAO;
import com.klbstore.dto.AllChiTietGioHangDTO;
import com.klbstore.dto.NguoiDungDTO;
import com.klbstore.extensions.ContactService;
import com.klbstore.extensions.HashedPasswordArgon2;
import com.klbstore.extensions.OrderService;
import com.klbstore.extensions.OtpGenerator;
import com.klbstore.extensions.OtpService;
import com.klbstore.model.ChiTietDonHang;
import com.klbstore.model.ChiTietGioHang;
import com.klbstore.model.ChiTietSanPham;
import com.klbstore.model.DanhGia;
import com.klbstore.model.DonHang;
import com.klbstore.model.GioHang;
import com.klbstore.model.HinhThucThanhToan;
import com.klbstore.model.LienHe;
import com.klbstore.model.MaXacNhan;
import com.klbstore.model.MauSac;
import com.klbstore.model.NguoiDung;
import com.klbstore.model.SanPham;
import com.klbstore.service.CookieService;
import com.klbstore.service.MailerServiceImpl;
import com.klbstore.service.ParamService;
import com.klbstore.service.SessionService;
import com.klbstore.service.ShoppingCartService;
import com.klbstore.valid.ChangePass;
import com.klbstore.valid.EmailForm;
import com.klbstore.valid.Login;
import com.klbstore.valid.UnlockAcc;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class UserController {
    @Autowired
    NguoiDungDAO nguoiDungDao;
    @Autowired
    CookieService cookieService;
    @Autowired
    ParamService paramService;
    @Autowired
    SessionService sessionService;
    @Autowired
    HttpServletRequest request;
    @Autowired
    private SanPhamDAO sanPhamDAO;
    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    MauSacDAO mauSacDAO;
    @Autowired
    MaXacNhanDAO maXacNhanDAO;
    @Autowired
    MailerServiceImpl mailer;
    @Autowired
    OtpService otpService;
    @Autowired
    HashedPasswordArgon2 hashedPassword;
    @Autowired
    com.klbstore.dao.ChiTietSanPhamDAO chiTietSanPhamDAO;
    @Autowired
    HinhThucThanhToanDAO htttDAO;
    @Autowired
    ChiTietDonHangDAO ctdhDAO;

    @Autowired
    SecurityRestTemplate restTemplate;

    public NguoiDung getNguoiDung() { // Lấy người dùng từ session và cookie
        NguoiDung user = new NguoiDung();
        if (sessionService.get("user") != null) {
            user = sessionService.get("user");
            return user;
        } else if (cookieService.get("klb") != null) {
            int id = Integer.parseInt(cookieService.getValue("klb"));
            user = nguoiDungDao.findById(id).get();
            return user;
        }
        return user = null;
    }

    @RequestMapping("/user/log-out")
    public String logOut() {
        cookieService.remove("klb");
        sessionService.remove("user");
        return "redirect:/user/login-register";
    }

    @GetMapping("/user/404")
    public String error404() {
        return "user/404";
    }

    @Autowired
    GiamGiaTrucTiepDAO giamGiaTrucTiepDAO;

    @GetMapping(value = { "/", "/user/index" })
    public String index(Model model) {
        model.addAttribute("noiBat",
                restTemplate.get("http://localhost:8080/rest/xinchao?hienThi=true&noiBat=true&sortBy=giamGiaGiamDan"));
        model.addAttribute("dienThoai",
                restTemplate.get("http://localhost:8080/rest/xinchao?hienThi=true&sortBy=giaGiamDan"));
        model.addAttribute("useHeader", getNguoiDung());
        return "user/index";
    }

    // GET đăng nhập - đăng kí
    @GetMapping("/user/login-register")
    public String loginRegister(Model model, @RequestParam("error") Optional<String> error) {
        model.addAttribute("login", new Login());
        sessionService.remove("security-uri");

        model.addAttribute("login", new Login());

        var nguoidung = new NguoiDung();
        model.addAttribute("login", nguoidung);
        model.addAttribute("registry", nguoidung);

        if (!error.isEmpty()) {
            model.addAttribute("message", error.get());
        }

        return "user/login-register";
    }

    @PostMapping("/user/contact")
    public String contact(Model model, @Valid @ModelAttribute("lienhe") LienHe lh, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/contact";
        }
        contactService.receiveContactFormSubmission(lh.getName(), lh.getEmail(), lh.getMessage());
        model.addAttribute("sendSuccess", "Gửi liên hệ thành công");
        model.addAttribute("lienhe", new LienHe());
        return "user/contact";
    }

    // Đăng nhập
    private int i = 0;

    @PostMapping("/user/login-register")
    public String processLoginForm(Model model, @Valid @ModelAttribute("login") Login login, BindingResult result) {
        // Xử lý đăng nhập
        var nguoidung = new NguoiDung();
        model.addAttribute("registry", nguoidung); // set người dùng cho registry
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id); // argon2
        if (!result.hasErrors()) {
            try {
                NguoiDung user = nguoiDungDao.findBySdt(login.getSdt());

                if (user.isTrangThaiKhoa()) {
                    i = 0;
                    model.addAttribute("message", "Tài khoản hiện đang bị khóa. Click vào ");
                    model.addAttribute("link", " link sau để mở khóa");
                    return "user/login-register";
                }
                if (i == 5) { // lock tài khoản
                    user.setTrangThaiKhoa(true);
                    nguoiDungDao.save(user);
                }
                // check mật khẩu
                if (!argon2.verify(user.getMatKhau().trim(), login.getMatKhau())) {
                    i = i + 1;
                    model.addAttribute("message", "Mật khẩu không chính xác!");
                    return "user/login-register";
                } else {
                    String uri = sessionService.get("security-uri");

                    if (uri != null) {
                        model.addAttribute("message", "Đăng nhập để tiếp tục");
                        return "redirect:" + uri;
                    } else {
                        i = 0;
                        model.addAttribute("message", "Đăng nhập thành công!");
                        sessionService.set("user", user); // Gán cho session user
                        if (paramService.getBoolean("rememberMe", false)) {
                            cookieService.add("klb", String.valueOf(user.getNguoiDungId()), 10);
                        }
                        sessionService.remove("check");
                        NguoiDung nd = sessionService.get("user");
                        if (gioHangDAO.findByNguoiDung_NguoiDungId(nd.getNguoiDungId()) == null) {
                            GioHang gioHang = new GioHang();
                            gioHang.setNguoiDung(nd);
                            gioHangDAO.save(gioHang);
                        }
                        return "redirect:/user/index";
                    }
                }
            } catch (Exception e) {
                model.addAttribute("message", "Số điện thoại hoặc mật khẩu không chính xác!");
            }

        }
        return "user/login-register";
    }

    // Đăng ký (send otp + xử lý)
    @RequestMapping("/user/login-register/registry")
    public String getRegister(Model model, @Valid @ModelAttribute("registry") NguoiDung registry,
            BindingResult result, RedirectAttributes rattrs) {
        var nguoiDung = new NguoiDung();
        model.addAttribute("login", nguoiDung);
        try {
            Pattern p = Pattern.compile(
                    "^(?=.*[`!@#\\$%\\^&*()-=_+\\\\[\\\\]{}|;':\\\",./<>?])(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])\\S{8,}$");
            Matcher m = p.matcher(registry.getMatKhau());
            Boolean e = m.matches();
            if (!result.hasErrors()) {
                Boolean check = true;
                if (nguoiDungDao.findByTenDangNhap(registry.getTenDangNhap()) != null) {
                    check = false;
                    model.addAttribute("errorMessage", "Tên đăng nhập đã tồn tại!");
                    return "user/login-register";
                }
                if (nguoiDungDao.findByEmail(registry.getEmail()) != null) {
                    check = false;
                    model.addAttribute("errorMessage", "Email đã tồn tại!");
                    return "user/login-register";
                }
                if (nguoiDungDao.findBySdt(registry.getSdt()) != null) {
                    check = false;
                    model.addAttribute("errorMessage", "Số điện thoại đã tồn tại!");
                    return "user/login-register";
                }
                if (e == false) {
                    check = false;
                    model.addAttribute("errorMessage", "Mật khẩu chưa đúng định dạng!");
                    return "user/login-register";
                }
                if (!registry.getMatKhau().equals(request.getParameter("matKhauXN"))) {
                    check = false;
                    model.addAttribute("errorMessage", "Sai mật khẩu xác nhận!");
                    return "user/login-register";
                }
                if (check == true) {
                    registry.setMatKhau(hashedPassword.stringToArgon2(registry.getMatKhau()));
                    nguoiDungDao.save(registry);

                    model.addAttribute("errorMessage", "Đăng ký tài khoản thành công");

                    String otp = "";
                    String message = "";
                    try {
                        NguoiDung user = nguoiDungDao.findByEmail(registry.getEmail());

                        Date curDate = new Date();
                        MaXacNhan maXacNhan = maXacNhanDAO.findByNguoiDungId(user.getNguoiDungId());
                        // MaXacNhan hết hạn tạo mã mới
                        if (maXacNhan == null || maXacNhan.getDaXacNhan()
                                || (curDate.compareTo(maXacNhan.getHanHieuLucOtp()) > 0)) {
                            otp = OtpGenerator.generateOtp("otp_key");
                            MaXacNhan mxacnhan = new MaXacNhan();
                            mxacnhan.setNguoiDungId(user.getNguoiDungId());
                            mxacnhan.setMaOtp(otp);
                            mxacnhan.setNgayTaoOtp(new Date());
                            maXacNhanDAO.save(mxacnhan);

                            // Gửi email
                            otpService.generateAndSendOtp(registry.getEmail(), "MÃ XÁC NHẬN KÍCH HOẠT TÀI KHOẢN");
                            message = "Mã xác nhận sẽ được gửi đến email của bạn trong vài giây\r\n" +
                                    "Mã có hiệu lực trong 5 phút";
                        } else {// chưa hết hạn lấy mã cũ
                            message = "Mã xác nhận đã được gửi vào email của bạn";
                            otp = maXacNhan.getMaOtp();
                        }
                        model.addAttribute("message", message);
                        model.addAttribute("email", registry.getEmail());
                        return "user/active";
                    } catch (Exception err) {
                        model.addAttribute("message", "Email không tồn tại hoặc chưa được đăng ký!");
                        err.printStackTrace();
                    }

                    return "user/active";
                    // return "user/login-register";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Lỗi");
        }
        return "user/login-register";
    }

    @GetMapping("/user/active")
    public String activeAcc(Model model) {

        model.addAttribute("message", "Mã xác nhận đã được gửi qua email của bạn.");
        return "user/active";
    }

    @PostMapping("/user/active/{email}")
    public String activeAcc(Model model, @PathVariable("email") String email, @RequestParam("otp") String usetotp) {
        System.out.println(email);
        try {
            var user = nguoiDungDao.findByEmail(email);
            var otp = maXacNhanDAO.findByNguoiDungId(user.getNguoiDungId());
            if (usetotp.equals(otp.getMaOtp())) {
                user.setTrangThaiKhoa(false);
                nguoiDungDao.save(user);
                model.addAttribute("message", "Tài khoản được kích hoạt thành công. Quay về trang ");
                model.addAttribute("error", "đăng nhập");
            }
        } catch (Exception e) {
            model.addAttribute("message", "Có lỗi xảy ra trong quá trình xử lý. Quay về trang ");
            model.addAttribute("error", "đăng nhập");
        }
        return "user/active";
    }

    // Gửi email - quên mật khẩu
    @RequestMapping("/user/forgot-password/sendmail")
    public String sendCode(Model model, @Valid @ModelAttribute("sendForm") EmailForm sendForm, BindingResult result) {
        String otp = "";
        String message = "";
        try {
            NguoiDung user = nguoiDungDao.findByEmail(sendForm.getEmail());

            Date curDate = new Date();
            MaXacNhan maXacNhan = maXacNhanDAO.findByNguoiDungId(user.getNguoiDungId());
            // MaXacNhan hết hạn tạo mã mới
            if (maXacNhan == null || maXacNhan.getDaXacNhan()
                    || (curDate.compareTo(maXacNhan.getHanHieuLucOtp()) > 0)) {
                otp = OtpGenerator.generateOtp("otp_key");
                MaXacNhan mxacnhan = new MaXacNhan();
                mxacnhan.setNguoiDungId(user.getNguoiDungId());
                mxacnhan.setMaOtp(otp);
                mxacnhan.setNgayTaoOtp(new Date());
                maXacNhanDAO.save(mxacnhan);

                // Gửi email
                otpService.generateAndSendOtp(sendForm.getEmail(), "MÃ XÁC NHẬN KHÔI PHỤC MẬT KHẨU");
                message = "Mã xác nhận sẽ được gửi đến email của bạn trong vài giây\r\n" +
                        "Mã có hiệu lực trong 5 phút";
            } else {// chưa hết hạn lấy mã cũ
                message = "Mã xác nhận đã được gửi vào email của bạn";
                otp = maXacNhan.getMaOtp();
            }

            model.addAttribute("message", message);
            return "user/send-code";
        } catch (Exception e) {
            model.addAttribute("message", "Email không tồn tại hoặc chưa được đăng ký!");
            e.printStackTrace();
        }
        return "user/send-code";
    }

    // GET Quên mật khẩu
    @GetMapping("/user/forgot-password")
    public String getForgotPassword(Model model) {
        EmailForm emailForm = new EmailForm();
        model.addAttribute("sendForm", emailForm);
        model.addAttribute("useHeader", getNguoiDung());

        return "user/send-code";
    }

    // Sang trang quên mk
    @PostMapping("/user/forgot-password")
    public String postForgotPassword(Model model, @Valid @ModelAttribute("sendForm") EmailForm sendForm,
            BindingResult result) {
        String message = "Email của bạn chưa được đăng ký";

        if (result.hasErrors()) {
            // in lỗi
        } else {
            try {
                NguoiDung user = nguoiDungDao.findByEmail(sendForm.getEmail());
                MaXacNhan otp = maXacNhanDAO.findByNguoiDungId(user.getNguoiDungId());
                Date curDate = new Date();
                if (otp != null && !otp.getDaXacNhan() && !(curDate.compareTo(otp.getHanHieuLucOtp()) > 0)) {
                    if (sendForm.getEmail().equals(user.getEmail()) && sendForm.getMxn().equals(otp.getMaOtp())) {
                        sessionService.set("user-email", user.getEmail());
                        return "redirect:/user/change-password";
                    } else if (!sendForm.getEmail().equals(user.getEmail())) {
                        message = "Địa chỉ email không hợp lệ";
                    } else {
                        message = "Mã xác nhận không chính xác";
                    }
                } else {
                    message = "Mã xác nhận không chính xác";
                }
            } catch (Exception e) {
                model.addAttribute("message", "Mã xác nhận không đúng!");
            }
            model.addAttribute("message", message);
        }
        return "user/send-code";
    }

    // GET Đổi mật khẩu
    @GetMapping("/user/change-password")
    public String changePassword(Model model, @ModelAttribute("changeNewPassword") ChangePass change) {
        model.addAttribute("nguoidung", getNguoiDung());
        model.addAttribute("usercheck", sessionService.get("user"));
        return "user/change-password";
    }

    // Đổi mật khẩu xử lý
    @PostMapping("/user/change-password")
    public String changePassword(Model model, @Valid @ModelAttribute("changeNewPassword") ChangePass change,
            BindingResult result) {

        model.addAttribute("nguoidung", getNguoiDung());
        model.addAttribute("usercheck", sessionService.get("user"));
        if (!result.hasErrors()) {
            NguoiDung nguoiDung = nguoiDungDao.findByEmail(sessionService.get("user-email"));
            Pattern p = Pattern.compile(
                    "^(?=.*[`!@#\\$%\\^&*()-=_+\\\\[\\\\]{}|;':\\\",./<>?])(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])\\S{8,}$");
            Matcher m = p.matcher(change.getNewPass());
            Boolean b = m.matches();

            if (b == false) {
                model.addAttribute("message", "Mật khẩu chưa đúng định dạng!");
                return "user/change-password";
            }
            if (change.getNewPass().equals(change.getSubPass())) {
                try {
                    nguoiDung.setMatKhau(hashedPassword.stringToArgon2(change.getNewPass()));
                    nguoiDungDao.save(nguoiDung);
                    model.addAttribute("message", "Đổi mật khẩu thành công");
                    sessionService.remove("user-email");
                    return "user/change-password";
                } catch (Exception e) {
                    model.addAttribute("message", "Có lỗi xảy ra. Quay về ");
                    model.addAttribute("error", "trang đăng nhập");
                    return "user/change-password";
                }
            } else {
                model.addAttribute("message", "Mật khẩu xác nhận không chính xác");
            }
        }
        return "user/change-password";
    }

    // Gửi mail unlock account
    @GetMapping("/user/unlock-account")
    public String getUnlockAccount(Model model, @RequestParam("sdt") String phone,
            @ModelAttribute("unlockFrom") UnlockAcc unlockAccount) {
        String otp = "";
        String message = "";
        model.addAttribute("unlockFrom", new UnlockAcc());
        try {
            NguoiDung user = nguoiDungDao.findBySdt(phone);

            Date curDate = new Date();
            MaXacNhan maXacNhan = maXacNhanDAO.findByNguoiDungId(user.getNguoiDungId());
            // MaXacNhan hết hạn tạo mã mới
            if (maXacNhan == null || maXacNhan.getDaXacNhan()
                    || (curDate.compareTo(maXacNhan.getHanHieuLucOtp()) > 0)) {
                otp = OtpGenerator.generateOtp("otp_key");
                MaXacNhan mxacnhan = new MaXacNhan();
                mxacnhan.setNguoiDungId(user.getNguoiDungId());
                mxacnhan.setMaOtp(otp);
                mxacnhan.setNgayTaoOtp(new Date());
                maXacNhanDAO.save(mxacnhan);

                // Gửi email
                otpService.generateAndSendOtp(user.getEmail(), "MÃ XÁC NHẬN KHÔI PHỤC TÀI KHOẢN");
                message = "Mã xác nhận sẽ được gửi đến email của bạn trong vài giây\r\n" +
                        "Mã có hiệu lực trong 5 phút";
            } else {// chưa hết hạn lấy mã cũ
                message = "Mã xác nhận đã được gửi vào email của bạn";
                otp = maXacNhan.getMaOtp();
            }
            // Lưu tài khoản vào session cập nhật
            sessionService.set("user-phone", user.getSdt());

            model.addAttribute("message", message);
            return "user/unlock-account";
        } catch (Exception e) {
            model.addAttribute("message", "Có lỗi xảy ra. Quay về trang ");
            model.addAttribute("error", "đăng nhập");
            e.printStackTrace();
        }
        return "user/unlock-account";
    }

    // Gửi mã mở khóa tài khoản
    @PostMapping("/user/unlock-account")
    public String postUnlockAccount(Model model, @Valid @ModelAttribute("unlockFrom") UnlockAcc unlockForm,
            BindingResult result) {
        cookieService.remove("klb");
        String phone = sessionService.get("user-phone");
        if (!result.hasErrors()) {

            try {
                Pattern p = Pattern.compile(
                        "^(?=.*[`!@#\\$%\\^&*()-=_+\\\\[\\\\]{}|;':\\\",./<>?])(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])\\S{8,}$");
                Matcher m = p.matcher(unlockForm.getNewPass());
                Boolean b = m.matches();

                NguoiDung user = nguoiDungDao.findBySdt(phone);
                MaXacNhan mxn = maXacNhanDAO.findByNguoiDungId(user.getNguoiDungId());
                if (b == false) {
                    model.addAttribute("message", "Mật khẩu chưa đúng định dạng!");
                    return "user/unlock-account";
                }
                if (unlockForm.getSubCode() == "") {
                    model.addAttribute("message", "Hãy nhập mã xác nhận!");
                    return "user/unlock-account";
                }
                if (!unlockForm.getSubCode().equals(mxn.getMaOtp())) {
                    model.addAttribute("message", "Mã xác nhận không chính xác!");
                    return "user/unlock-account";
                }
                if (unlockForm.getNewPass().equals(unlockForm.getSubPass())) {
                    try {
                        user.setTrangThaiKhoa(false);
                        user.setMatKhau(hashedPassword.stringToArgon2(unlockForm.getNewPass()));
                        nguoiDungDao.save(user);
                        sessionService.remove("user-phone");
                        model.addAttribute("message", "Đổi mật khẩu thành công. Về trang");
                        model.addAttribute("error", " đăng nhập");
                        sessionService.remove("user-email");
                        return "user/unlock-account";
                    } catch (Exception e) {
                        model.addAttribute("message", "Có lỗi xảy ra. Quay về ");
                        model.addAttribute("error", "trang đăng nhập");
                        return "user/unlock-account?sdt=" + phone;
                    }
                } else {
                    model.addAttribute("message", "Mật khẩu xác nhận không chính xác");
                }
                return "user/unlock-account";
            } catch (Exception e) {
                model.addAttribute("message", "Có lỗi xảy ra. Quay về trang ");
                model.addAttribute("error", "đăng nhập");
                e.printStackTrace();
            }
        }
        return "user/unlock-account";
    }

    @GetMapping("/user/profile")
    public String profile(Model model, @ModelAttribute("userProfile") NguoiDungDTO user) {
        NguoiDung userForm = getNguoiDung();
        NguoiDungDTO nddto = new NguoiDungDTO();
        nddto.setTenDangNhap(userForm.getTenDangNhap());
        nddto.setHoTen(userForm.getHoTen());
        nddto.setSdt(userForm.getSdt());
        nddto.setEmail(userForm.getEmail());
        nddto.setDiaChi(userForm.getDiaChi());
        nddto.setGioiTinh(userForm.getGioiTinh());
        if(userForm.getNgaySinh() != null){
            nddto.setNgaySinh(userForm.getNgaySinh().toString());
        }
        else{
            nddto.setNgaySinh(null);
        }
        String diaChi = nddto.getDiaChi();
        String[] diaChiComponents = diaChi.split(","); // Tách theo dấu phẩy
        String diaChiCuThe = diaChiComponents[0].trim(); // Địa chỉ cụ thể
        String xaPhuong = diaChiComponents[diaChiComponents.length - 3].trim(); // Phường/Xã
        String quanHuyen = diaChiComponents[diaChiComponents.length - 2].trim(); // Quận/Huyện
        String tinhThanh = diaChiComponents[diaChiComponents.length - 1].trim(); // Tỉnh/Thành phố
        nddto.setDiaChi(diaChiCuThe);
        nddto.setXaPhuong(xaPhuong);
        nddto.setQuanHuyen(quanHuyen);
        nddto.setTinhThanh(tinhThanh);
        System.out.print(xaPhuong + " " + quanHuyen + " " + tinhThanh);
        model.addAttribute("userProfile", nddto);
        model.addAttribute("email", user.getEmail());
        return "user/profile";
    }

    @PostMapping("/user/profile")
    public String profileUpdate(Model model, @Valid @ModelAttribute("userProfile") NguoiDungDTO userForm,
            BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("message", "Cập nhật thất bại");
            System.out.println(result.getAllErrors());
            return "user/profile";
        }

        NguoiDung user = getNguoiDung();

        NguoiDung userName = nguoiDungDao.findByTenDangNhap(userForm.getTenDangNhap());
        NguoiDung userPhone = nguoiDungDao.findBySdt(userForm.getSdt());
        NguoiDung userEmail = nguoiDungDao.findByEmail(userForm.getEmail());

        String diaChi = userForm.getDiaChi().trim();
        diaChi = diaChi.replace(",", "");
        diaChi = diaChi + ", " + userForm.getXaPhuong().trim() + ", " + userForm.getQuanHuyen().trim() + ", "
                + userForm.getTinhThanh().trim();

        if (userName != null && !userName.getNguoiDungId().equals(user.getNguoiDungId())) {
            model.addAttribute("message", "Tên đăng nhập đã tồn tại!");
            return "user/profile";
        }
        if (userPhone != null && !userPhone.getNguoiDungId().equals(user.getNguoiDungId())) {
            model.addAttribute("message", "Số điện thoại đã tồn tại!");
            return "user/profile";
        }
        if (userEmail != null && !userEmail.getNguoiDungId().equals(user.getNguoiDungId())) {
            model.addAttribute("message", "Email đã tồn tại!");
            return "user/profile";
        }
        user.setDiaChi(diaChi);
        user.setTenDangNhap(userForm.getTenDangNhap());
        user.setHoTen(userForm.getHoTen());
        user.setSdt(userForm.getSdt());
        user.setEmail(userForm.getEmail());
        user.setGioiTinh(userForm.getGioiTinh());
        nguoiDungDao.save(user);
        model.addAttribute("message", "Cập nhật thành công");

        return "user/profile";
    }

    // GET Đổi mật khẩu từ profile
    @GetMapping("/user/profile/change-password/{email}")
    public String changePasswordProfile(Model model, @ModelAttribute("changeNewPassword") ChangePass change,
            @PathVariable("email") String email) {
        model.addAttribute("nguoidung", getNguoiDung());
        model.addAttribute("usercheck", sessionService.get("user"));
        return "user/change-password-profile";
    }

    // Đổi mật khẩu xử lý từ profile
    @PostMapping("/user/profile/change-password/{email}")
    public String changePasswordProfile(Model model, @Valid @ModelAttribute("changeNewPassword") ChangePass change,
            BindingResult result, @PathVariable("email") String email) {

        model.addAttribute("nguoidung", getNguoiDung());
        model.addAttribute("usercheck", sessionService.get("user"));
        if (!result.hasErrors()) {
            // NguoiDung nguoiDung =
            // nguoiDungDao.findByEmail(sessionService.get("user-email"));
            NguoiDung nguoiDung = nguoiDungDao.findByEmail(email);
            Pattern p = Pattern.compile(
                    "^(?=.*[`!@#\\$%\\^&*()-=_+\\\\[\\\\]{}|;':\\\",./<>?])(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])\\S{8,}$");
            Matcher m = p.matcher(change.getNewPass());
            Boolean b = m.matches();
            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id); // argon2

            if (getNguoiDung() != null) { // Lấy người dùng trong cookie boặc session nếu có tồn tại
                nguoiDung = getNguoiDung();

                if (change.getCurPass() == "") {
                    model.addAttribute("message", "Hãy nhập mật khẩu hiện tại!");
                    return "user/change-password-profile";
                }
                if (!argon2.verify(nguoiDung.getMatKhau().trim(), change.getCurPass())) {
                    model.addAttribute("message", "Mật khẩu hiện tại chưa chính xác!");
                    return "user/change-password-profile";
                }
                if (b == false) {
                    model.addAttribute("message", "Mật khẩu chưa đúng định dạng!");
                    return "user/change-password-profile";
                }
                if (change.getNewPass().equals(change.getSubPass())) {
                    try {
                        nguoiDung.setMatKhau(hashedPassword.stringToArgon2(change.getNewPass()));
                        nguoiDungDao.save(nguoiDung);
                        model.addAttribute("message", "Đổi mật khẩu thành công");
                        sessionService.remove("user-email");
                        return "user/change-password-profile";
                    } catch (Exception e) {
                        model.addAttribute("message", "Có lỗi xảy ra. Quay về ");
                        model.addAttribute("error", "trang đăng nhập");
                        return "user/change-password-profile";
                    }
                } else {
                    model.addAttribute("message", "Mật khẩu xác nhận không chính xác");
                }
            } else {
                model.addAttribute("message", "Có lỗi xảy ra trong quá trình xử lý. Quay về ");
                model.addAttribute("error", "trang chủ.");
            }
        }
        return "user/change-password-profile";
    }

    @GetMapping("/user/orders-list")
    public String getOrderList() {

        return "user/orders-list";
    }

    @GetMapping("/user/order")
    public String getOrder(Model model) {
        if (getNguoiDung() == null) {
            return "redirect:/login";
        } else {
            NguoiDung nguoiDung = getNguoiDung();
            List<DonHang> donHangs = donHangDAO.findByNguoiDung_NguoiDungId(nguoiDung.getNguoiDungId());
            model.addAttribute("donHangs", donHangs);
            return "user/order";
        }
    }

    @Autowired
    ChiTietGioHangRestController chiTietGioHangRestController;

    public void deleteAllChiTietGioHang(GioHang gioHang) {
        List<ChiTietGioHang> chiTietGioHangList = chiTietGioHangDAO.findAllByGioHang(gioHang);
        chiTietGioHangDAO.deleteAll(chiTietGioHangList);
    }

    @GetMapping("/user/checkout")
    public String checkout(Model model) {
        NguoiDung nguoiDung = sessionService.get("user");
        List<HinhThucThanhToan> paymentMethods = htttDAO.findAll();
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("cart", restTemplate.get("http://localhost:8080/checkoutCart?address="
                + nguoiDung.getDiaChi() + "&userId=" + nguoiDung.getNguoiDungId()));
        model.addAttribute("paymentMethods", paymentMethods);
        return "user/checkout";

    }

    public static Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null; // Trả về null nếu không thể chuyển đổi
        }
    }

    @PostMapping("/user/checkout")
    public String paymentVnpay(
            @RequestParam(value = "phuongThucThanhToanId", defaultValue = "1") int phuongThucThanhToanId,
            @RequestParam(value = "ghiChu", required = false) String ghiChu,
            Model model) throws InvalidKeyException, UnsupportedEncodingException, UnknownHostException,
            SignatureException, NoSuchAlgorithmException {
        NguoiDung nguoiDung = sessionService.get("user");
        JsonNode map = restTemplate.get("http://localhost:8080/checkoutSpecifiedCart?address=" + nguoiDung.getDiaChi()
                + "&userId=" + nguoiDung.getNguoiDungId());
        {
            String tongPhiVanChuyen = map.get("tongPhiVanChuyen").asText();
            String ngayGiaoHangDuKien = map.get("ngayGiaoHangDuKien").asText();
            if (phuongThucThanhToanId == 1) {

                if (tongPhiVanChuyen != "null" && ngayGiaoHangDuKien != "null") {
                    String orderId = donHangDAO.taoDonHang(nguoiDung.getNguoiDungId(), 1,
                            convertStringToDate(ngayGiaoHangDuKien), null, Double.parseDouble(tongPhiVanChuyen));
                    return "redirect:/user/find-order?donHangId=" + orderId;
                } else {
                    String orderId = donHangDAO.taoDonHang(nguoiDung.getNguoiDungId(), 1, null, null, 0.0);
                    return "redirect:/user/find-order?donHangId=" + orderId;
                }
            } else {
                if (tongPhiVanChuyen != "null" && ngayGiaoHangDuKien != "null") {
                    String orderId = donHangDAO.taoDonHang(nguoiDung.getNguoiDungId(), 1,
                            convertStringToDate(ngayGiaoHangDuKien), null, Double.parseDouble(tongPhiVanChuyen));
                    DonHang dh = donHangDAO.findById(Integer.parseInt(orderId)).orElse(null);
                    double value = dh.getTongTienSanPham() + dh.getPhiVanChuyen();
                    long longValue = (long) value;
                    return "redirect:" + paymentvnpay("Thanh toán đơn hàng " + orderId, longValue);
                } else {
                    String orderId = donHangDAO.taoDonHang(nguoiDung.getNguoiDungId(), 1, null, null, 0.0);
                    DonHang dh = donHangDAO.findById(Integer.parseInt(orderId)).orElse(null);
                    double value = dh.getTongTienSanPham() + dh.getPhiVanChuyen();
                    long longValue = (long) value;
                    return "redirect:" + paymentvnpay("Thanh toán đơn hàng " + orderId, longValue);
                }
            }
        }
    }

    @GetMapping("/user/recheckout")
    public String recheckout(@RequestParam("orderId") Integer orderId) throws InvalidKeyException,
            UnsupportedEncodingException, UnknownHostException, SignatureException, NoSuchAlgorithmException {
        DonHang dh = donHangDAO.findById(orderId).orElse(null);
        double value = dh.getTongTienSanPham() + dh.getPhiVanChuyen();
        long longValue = (long) value;
        return "redirect:" + paymentvnpay("Thanh toán đơn hàng " + dh.getDonHangId(), longValue);
    }

    @GetMapping("/product/quick-view/{id}")
    public String quickView(@PathVariable(value = "id") int sanPhamId, Model model) {
        SanPham sanPham = sanPhamDAO.findById(sanPhamId).get();

        List<MauSac> mauSacList = new ArrayList<>();
        List<MauSac> spMauSacList = sanPham.getSanPhamMauSacs();
        for (MauSac mauSac : spMauSacList) {
            List<ChiTietSanPham> chiTietSanPhamList = mauSac.getMauSacChiTietSanPhams();
            boolean coSanPhamConHang = false;
            for (ChiTietSanPham chiTietSanPham : chiTietSanPhamList) {
                int soLuongTrongKho = chiTietSanPham.getSoLuongTrongKho();
                if (soLuongTrongKho > 0) {
                    coSanPhamConHang = true;
                    break;
                }
            }
            if (coSanPhamConHang) {
                mauSacList.add(mauSac);
            }
        }
        sanPham.setSanPhamMauSacs(mauSacList);

        if (!mauSacList.isEmpty()) {
            model.addAttribute("quickView", sanPham);
        }

        return "user/index";
    }

    @Autowired
    GioHangDAO cartDAO;

    @GetMapping("/user/contact")
    public String contact(Model model) {
        NguoiDung nguoiDung = sessionService.get("user");
        if (nguoiDung == null) {
            model.addAttribute("lienhe", new LienHe());
        } else {
            model.addAttribute("lienhe", new LienHe(nguoiDung.getHoTen(), nguoiDung.getEmail()));
        }
        return "user/contact";
    }

    @Autowired
    DonHangDAO donHangDAO;
    @Autowired
    com.klbstore.dao.DanhGiaDAO danhGiaDAO;

    public boolean hasSuccessfulPurchaseAndNoComment(NguoiDung nguoiDung, Integer sanPhamId) {
        List<DonHang> donHangs = donHangDAO.findByNguoiDungAndTinhTrangThanhToan(nguoiDung, true);
        if (donHangs.isEmpty()) {
            return false;
        }
        for (DonHang donHang : donHangs) {
            List<ChiTietDonHang> chiTietDonHangs = donHang.getDonHangChiTietDonHangs();

            for (ChiTietDonHang chiTietDonHang : chiTietDonHangs) {
                if (chiTietDonHang.getSanPham().getSanPhamId().equals(sanPhamId)) {
                    List<DanhGia> danhGias = danhGiaDAO.findByNguoiDungAndSanPham(nguoiDung,
                            chiTietDonHang.getSanPham());
                    if (danhGias.isEmpty()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public String paymentvnpay(String tenGiaoDich, long soTienThanhToan)
            throws UnsupportedEncodingException, UnknownHostException, InvalidKeyException,
            SignatureException, NoSuchAlgorithmException {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        String createDate = sdf.format(currentDate);

        calendar.add(Calendar.MINUTE, 5);
        Date futureDate = calendar.getTime();

        // Gán ngày hết hạn xa hơn với định dạng chuẩn
        String expireDate = sdf.format(futureDate);

        // Sử dụng createDate và expireDate trong chuỗi input data
        String input = "vnp_Amount=" + (soTienThanhToan * 100) +
                "&vnp_BankCode=NCB" +
                "&vnp_Command=pay" +
                "&vnp_CreateDate=" + createDate +
                "&vnp_CurrCode=VND" +
                "&vnp_ExpireDate=" + expireDate +
                "&vnp_IpAddr=127.0.0.1" +
                "&vnp_Locale=vn" +
                "&vnp_OrderInfo=" + URLEncoder.encode(tenGiaoDich, StandardCharsets.UTF_8) +
                "&vnp_OrderType=other" +
                "&vnp_ReturnUrl=" + URLEncoder.encode(VnpayConfig.vnp_Returnurl, StandardCharsets.UTF_8) +
                "&vnp_TmnCode=ZRW18TX8" +
                "&vnp_TxnRef=" + VnpayConfig.getRandomNumber(8) +
                "&vnp_Version=2.1.0";
        String vnp_SecureHash = VnpayConfig.hmacSHA512("SNLNODLJHCHDAKWQUZEPFCIECKIRPTIE", input);
        input += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + input;
        return paymentUrl;
    }

    public Integer getDonHangId(String chuoi) {
        int viTriSo = -1;
        for (int i = 0; i < chuoi.length(); i++) {
            if (Character.isDigit(chuoi.charAt(i))) {
                viTriSo = i;
                break;
            }
        }
        if (viTriSo != -1) {
            StringBuilder so = new StringBuilder();
            while (viTriSo < chuoi.length() && Character.isDigit(chuoi.charAt(viTriSo))) {
                so.append(chuoi.charAt(viTriSo));
                viTriSo++;
            }
            return Integer.parseInt(so.toString());
        }

        return null;
    }

    @Autowired
    OrderService orderService;

    @GetMapping("/user/payment")
    public String payment(@RequestParam("vnp_ResponseCode") String vnp_ResponseCode,
            @RequestParam("vnp_OrderInfo") String vnp_OrderInfo, Model model) {
        if (vnp_ResponseCode.equals("00")) {
            Integer donHangId = getDonHangId(vnp_OrderInfo);
            DonHang donHang = donHangDAO.findById(donHangId).orElse(null);
            if (donHang != null) {
                donHangDAO.capNhatTrangThaiThanhToanDonHang(donHang.getDonHangId());
            }
            return "redirect:/user/find-order?donHangId=" + donHangId;
        } else {
            Integer donHangId = getDonHangId(vnp_OrderInfo);
            return "redirect:/user/find-order?donHangId=" + donHangId;
        }
    }

    @Autowired
    com.klbstore.dao.NguoiDungDAO nguoiDungDAO;

    @Autowired
    com.klbstore.dao.GioHangDAO gioHangDAO;

    @Autowired
    com.klbstore.dao.ChiTietGioHangDAO chiTietGioHangDAO;

    @GetMapping("/user/product-details")
    public String getProductDetails(@RequestParam(name = "productId", required = false) Integer productId,
            @RequestParam(name = "productName", required = false) String productName,
            Model model) {
        if (productId == null || productName == null) {
            return "redirect:/user/404";
        }
        model.addAttribute("chiTiet", restTemplate.get("http://localhost:8080/rest/xinchao?hienThi=true&sanPhamId="
                + productId + "&tenSanPham=" + productName));
        model.addAttribute("sanPhamLienQuan",
                restTemplate.get("http://localhost:8080/rest/xinchao?hienThi=true&sanPhamLienQuan=" + productId));
        return "user/product-details";

    }

    @RequestMapping("/user/shop-list")
    public String searchAndPage(Model model,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "nhomSanPhamId", defaultValue = "") int nhomSanPhamId,
            @RequestParam(value = "sortBy", defaultValue = "") String sortBy) {
        model.addAttribute("pg", restTemplate.get("http://localhost:8080/rest/sanpham?hienThi=true" + "&page=" + page
                + "&nhomSanPhamId=" + nhomSanPhamId + "&sortBy=" + sortBy));
        model.addAttribute("sortBy", sortBy);
        return "user/shop-list";
    }

    @RequestMapping("/user/search")
    public String search(Model model,
            @RequestParam(value = "keywords", defaultValue = "") String keywords,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "sortBy", defaultValue = "") String sortBy) {
        model.addAttribute("pg", restTemplate.get("http://localhost:8080/rest/sanpham?hienThi=true" + "&page=" + page
                + "&tenSanPham=" + keywords + "&sortBy=" + sortBy));
        model.addAttribute("sortBy", sortBy);
        return "user/shop-list";
    }

    @Autowired
    ContactService contactService;

    @GetMapping("/user/shopping-cart")
    public String shoppingCart(Model model) {
        NguoiDung nguoiDung = sessionService.get("user");
        if (nguoiDung != null) {
            chiTietGioHangDAO.kiemTraVaXoaSanPhamKhongHopLeTrongGioHang(nguoiDung.getNguoiDungId());
            AllChiTietGioHangDTO allChiTietGioHangDTO = new AllChiTietGioHangDTO(
                    chiTietGioHangDAO.layDanhSachSanPhamTrongGioHangTheoNguoiDung(nguoiDung.getNguoiDungId()),
                    chiTietGioHangDAO.tongSoSanPhamTrongGioHang(nguoiDung.getNguoiDungId()),
                    chiTietGioHangDAO.tinhTongTienTrongGioHang(nguoiDung.getNguoiDungId()), null,
                    null);
            System.out.println(nguoiDung.getNguoiDungId());
            model.addAttribute("allCart", allChiTietGioHangDTO);
        } else {
            model.addAttribute("allCart", shoppingCartService.getShoppingCartDTO());
        }
        return "user/shopping-cart";
    }

    @GetMapping("/user/wishlist")
    public String wishlist() {
        return "user/wishlist";
    }

    public Double getTotalAmountOrder(List<ChiTietDonHang> list) {
        Double total = 0.0;
        for (ChiTietDonHang ctdh : list) {
            total += ctdh.getGiaBan() * ctdh.getSoLuong();
        }
        return total;
    }

    // @GetMapping("/user/order")
    // public String getOrder(Model model) {
    // NguoiDung nguoiDung = sessionService.get("user");
    // if (nguoiDung != null) {
    // List<DonHang> donHangs =
    // donHangDAO.findByNguoiDung_NguoiDungId(nguoiDung.getNguoiDungId());
    // for (DonHang donHang : donHangs) {
    // donHang.setTongDonHang(getTotalAmountOrder(donHang.getDonHangChiTietDonHangs()));
    // }
    // model.addAttribute("donHangs", donHangs);
    // }
    // return "user/order";
    // }
    @GetMapping("/user/find-order")
    public String findOrder(@RequestParam(name = "donHangId", required = false) Integer donHangId, Model model) {
        NguoiDung nguoiDung = sessionService.get("user");
        if (nguoiDung != null) {
            DonHang dh = donHangDAO.findById(donHangId).orElse(null);
            model.addAttribute("donHangs", dh);
        }
        return "user/order";
    }

    @Autowired
    ChiTietDonHangDAO chiTietDonHangDAO;

    @GetMapping("/user/order-details")
    public String getOrder(@RequestParam(name = "donHangId", required = false) Integer donHangId,
            Model model) {
        NguoiDung nguoiDung = sessionService.get("user");
        if (nguoiDung != null) {
            List<ChiTietDonHang> ctdh = chiTietDonHangDAO.findByDonHangId(donHangId);
            model.addAttribute("chiTietDonHangLists", ctdh);
            System.out.println(ctdh);
        }
        return "user/order-detail";
    }
}