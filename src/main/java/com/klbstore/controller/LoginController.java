package com.klbstore.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.klbstore.dao.ChiTietDonHangDAO;
import com.klbstore.dao.ChiTietGioHangDAO;
import com.klbstore.dao.ChiTietSanPhamDAO;
import com.klbstore.dao.GiamGiaTrucTiepDAO;
import com.klbstore.dao.GioHangDAO;
import com.klbstore.dao.HinhThucThanhToanDAO;
import com.klbstore.dao.HoatDongSaiMatKhauDAO;
import com.klbstore.dao.MaXacNhanDAO;
import com.klbstore.dao.MauSacDAO;
import com.klbstore.dao.NguoiDungDAO;
import com.klbstore.extensions.HashedPasswordArgon2;
import com.klbstore.extensions.OtpService;
import com.klbstore.model.ChiTietGioHang;
import com.klbstore.model.ChiTietSanPham;
import com.klbstore.model.GioHang;
import com.klbstore.model.NguoiDung;
import com.klbstore.model.Role;
import com.klbstore.model.SanPham;
import com.klbstore.security.auth.AuthenticationService;
import com.klbstore.security.service.JwtService;
import com.klbstore.service.CookieService;
import com.klbstore.service.MailerServiceImpl;
import com.klbstore.service.ParamService;
import com.klbstore.service.SessionService;
import com.klbstore.service.ShoppingCartService;
import com.klbstore.valid.ChangePass;
import com.klbstore.valid.Login;
import com.klbstore.valid.Profile;
import com.klbstore.valid.Registry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class LoginController {
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
    ChiTietSanPhamDAO chiTietSanPhamDAO;
    @Autowired
    HinhThucThanhToanDAO htttDAO;
    @Autowired
    ChiTietDonHangDAO ctdhDAO;
    @Autowired
    GioHangDAO cartDAO;
    @Autowired
    ChiTietGioHangDAO chiTietGioHangDAO;
    @Autowired
    GiamGiaTrucTiepDAO giamGiaTrucTiepDAO;

    

    private final AuthenticationService authenticationService;
    private final com.klbstore.config.SecurityRestTemplate securityRestTemplate;


    @GetMapping("/user/update-phone-number/{username}")
    public String updatePhone(Model model) {
        return "phone_update";
    }

    @PostMapping("/user/update-phone-number/{username}")
    public String updatePhone(Model model, @PathVariable("username") String username) {
        return "phone_update";
    }

    @GetMapping("/user/login/error")
    public String loginError(Model model) {
        model.addAttribute("message", "Đăng nhập thất bại!");
        return "forward:/user/login";
    }

    @PostMapping("/user/access/denied")
    public String accessDenied(Model model) {
        model.addAttribute("message", "Truy cập bị từ chối!");
        return "login";
    }

    @PostMapping("/user/register")
    public String dangKy(Model model, HttpServletResponse response,
            @Valid @ModelAttribute("registerForm") Registry registry,
            BindingResult result) {
        String url = "http://localhost:8080/api/v1/registration";

        model.addAttribute("registerForm", registry);
        if (!result.hasErrors()) {
            registry.setQuyenDangNhap(false);
            registry.setRole(Role.USER);
            securityRestTemplate.post(url, registry);
            model.addAttribute("errorMessage", "Mã xác nhận đã được gửi qua email của bạn");
        }

        return "register";
    }

    // @GetMapping("/user/profile")
    // public String profile(Model model, @ModelAttribute("userProfile") Profile user) {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     model.addAttribute("userProfile", nguoiDungDao.findByTenDangNhap(authentication.getName()).get());
    //     return "user/profile";
    // }

    // @PostMapping("/user/profile")
    // public String profileUpdate(Model model, 
    //         @Valid @ModelAttribute("userProfile") Profile userForm,
    //         BindingResult result) {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    //     boolean check = true;
    //     NguoiDung userOfPhone = nguoiDungDao.findBySdt(userForm.getSdt()).get();

    //     System.out.println(nguoiDungDao.findByTenDangNhap(authentication.getName()).get().getSdt());
    //     System.out.println(userForm.getSdt());

    //     if (userOfPhone != null
    //             &&
    //             !nguoiDungDao.findByTenDangNhap(authentication.getName()).get().getSdt().equalsIgnoreCase(userForm.getSdt())) {
    //         check = false;
    //         model.addAttribute("message", "Số điện thoại đã tồn tại!");
    //         return "user/profile";
    //     }
    //     System.out.println(result);
    //     if (!result.hasErrors() && (check)) {
    //         NguoiDung nguoiDung = nguoiDungDao.findByTenDangNhap(authentication.getName()).get();
    //         nguoiDung.setNgaySinh(userForm.getNgaySinh());
    //         nguoiDung.setHoTen(userForm.getHoTen());
    //         nguoiDung.setGioiTinh(userForm.getGioiTinh());
    //         nguoiDung.setSdt(userForm.getSdt());
    //         nguoiDung.setDiaChi(userForm.getDiaChi());

    //         nguoiDungDao.save(nguoiDung);
    //         model.addAttribute("message", "Cập nhật thành công");
    //         return "user/profile";
    //     }
    //     model.addAttribute("message", "Cập nhật thất bại");
    //     return "user/profile";
    // }

    @GetMapping("/user/profile/change-password")
    public String changePasswordProfile(Model model, 
    @ModelAttribute("changeNewPassword") ChangePass changePass) {
        return "user/change-password-profile";
    }

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/user/profile/change-password")
    public String change(Model model,
            @Valid @ModelAttribute("changeNewPassword") ChangePass change,
            BindingResult result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!result.hasErrors()) {
            NguoiDung nguoiDung = nguoiDungDao.findByTenDangNhap(authentication.getName()).get();
            if (change.getNewPass().equalsIgnoreCase(change.getSubPass())) {
                if (passwordEncoder.matches(change.getCurPass(), nguoiDung.getMatKhau())) {
                    nguoiDung.setMatKhau(passwordEncoder.encode(change.getNewPass()));
                    model.addAttribute("message", "Cập nhật mật khẩu thành công");
                    return "redirect:/user/profile";
                }
                model.addAttribute("message", "Mật khẩu hiện tại không chính xác");
                return "user/change-password-profile";

            } else {
                model.addAttribute("message", "Mật khẩu xác nhận không chính xác");
                return "user/change-password-profile";
            }
        }
        return "user/change-password-profile";
    }

}
