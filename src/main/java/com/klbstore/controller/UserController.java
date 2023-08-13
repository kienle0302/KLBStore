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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public NguoiDung getNguoiDung() { 
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return  nguoiDungDAO.findByTenDangNhap(auth.getName()).get();
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

    @GetMapping("/user/profile")
    public String profileDTP(Model model, @ModelAttribute("userProfile") NguoiDungDTO user) {
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

        NguoiDung userName = nguoiDungDao.findByTenDangNhap(userForm.getTenDangNhap()).get();
        NguoiDung userPhone = nguoiDungDao.findBySdt(userForm.getSdt()).get();
        NguoiDung userEmail = nguoiDungDao.findByEmail(userForm.getEmail()).get();

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
        NguoiDung nguoiDung = getNguoiDung();
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
        NguoiDung nguoiDung = getNguoiDung();
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
        NguoiDung nguoiDung = getNguoiDung();
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
        NguoiDung nguoiDung = getNguoiDung();
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


    // @GetMapping("/user/order")
    // public String getOrder(Model model) {
    // NguoiDung nguoiDung = getNguoiDung();
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
        NguoiDung nguoiDung = getNguoiDung();
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
        NguoiDung nguoiDung = getNguoiDung();
        if (nguoiDung != null) {
            List<ChiTietDonHang> ctdh = chiTietDonHangDAO.findByDonHangId(donHangId);
            model.addAttribute("chiTietDonHangLists", ctdh);
            System.out.println(ctdh);
        }
        return "user/order-detail";
    }
}