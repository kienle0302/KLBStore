package com.klbstore.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.klbstore.dao.CauHinhDienThoaiDAO;
import com.klbstore.dao.ChiTietDonHangDAO;
import com.klbstore.dao.ChiTietSanPhamDAO;
import com.klbstore.dao.DanhGiaDAO;
import com.klbstore.dao.DanhMucSanPhamDAO;
import com.klbstore.dao.DonHangDAO;
import com.klbstore.dao.GiamGiaDAO;
import com.klbstore.dao.NguoiDungDAO;
import com.klbstore.dao.NhaCungCapDAO;
import com.klbstore.dao.PhieuNhapDAO;
import com.klbstore.dao.PhieuXuatDAO;
import com.klbstore.dao.SanPhamDAO;
import com.klbstore.model.CauHinhDienThoai;
import com.klbstore.model.DanhGia;
import com.klbstore.model.DanhMucSanPham;
import com.klbstore.model.DonHang;
import com.klbstore.model.GiamGia;
import com.klbstore.model.NguoiDung;
import com.klbstore.model.NhaCungCap;
import com.klbstore.model.PhieuNhap;
import com.klbstore.model.PhieuXuat;
import com.klbstore.model.SanPham;
import com.klbstore.model.ThongKeTonKhoDanhMuc;
import com.klbstore.service.CookieService;
import com.klbstore.service.ParamService;
import com.klbstore.service.SessionService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminController {

    //DAO
    @Autowired
    CauHinhDienThoaiDAO cauHinhDienThoaiDAO;
    @Autowired
    PhieuXuatDAO phieuXuatDAO;
    @Autowired
    PhieuNhapDAO phieuNhapDAO;
    @Autowired
    DanhGiaDAO danhGiaDAO;
    @Autowired
    DonHangDAO donHangDAO;
    @Autowired
    ChiTietSanPhamDAO chiTietSanPhamDAO;
    @Autowired
    SanPhamDAO sanPhamDAO;
    @Autowired
    NhaCungCapDAO nhaCungCapDAO;
    @Autowired
    GiamGiaDAO giamGiaDAO;
    @Autowired
    ChiTietDonHangDAO chiTietDonHangDAO;
    @Autowired
    DanhMucSanPhamDAO danhMucSanPhamDAO;
    @Autowired
    NguoiDungDAO nguoiDungDAO;
    @Autowired
    CookieService cookieService;
    @Autowired
    ParamService paramService;
    @Autowired
    SessionService sessionService;
    @Autowired
    HttpServletRequest request;

    public NguoiDung getNguoiDung() { // Lấy người dùng từ session và cookie
        NguoiDung user = new NguoiDung();
        if (sessionService.get("user") != null) {
            user = sessionService.get("user");
            return user;
        } else if (cookieService.get("klb") != null) {
            int id = Integer.parseInt(cookieService.getValue("klb"));
            user = nguoiDungDAO.findById(id).get();
            return user;
        }
        return user;
    }

    //============================CHỨC NĂNG QL & DS DANH MỤC=================================================
    //DS
    @GetMapping("admin/danhmuc")
    public String DSDanhMuc(Model model, @RequestParam("sortBy") Optional<String> sortBy, @RequestParam("p") Optional<Integer> p) {
        Sort sort = Sort.by(Direction.ASC, sortBy.orElse("danhMucSanPhamId"));
        Pageable pageable = PageRequest.of(p.orElse(0), 5, sort);
        Page<DanhMucSanPham> danhMucSanPham = danhMucSanPhamDAO.findAll(pageable);
        var numberOfpages = danhMucSanPham.getTotalPages();
        model.addAttribute("curr", p.orElse(0));
        model.addAttribute("numberOfpages",numberOfpages);
        // List<DanhMucSanPham> danhMucSanPham = danhMucSanPhamDAO.findAll(sort);
        model.addAttribute("items", danhMucSanPham);
        return "admin/DSDanhMuc";
    }
    // QL
    @GetMapping("admin/danhmuc/edit")
    public String QLDanhMuc(Model model) {
        DanhMucSanPham items = new DanhMucSanPham();
        model.addAttribute("items", items);
        return "admin/QLDanhMuc";
    }
    // DS btn chỉnh sửa
    @GetMapping("admin/danhmuc/edit/{id}")
    public String EditQLDanhMuc(Model model, @PathVariable("id") Integer id) {
        DanhMucSanPham items = danhMucSanPhamDAO.getById(id);
        model.addAttribute("items", items);
        return "admin/QLDanhMuc";
    }
    // DS btn xóa
    @GetMapping("admin/danhmuc/delete/{id}")
    public String DeleteQLDanhMuc(Model model, @PathVariable("id") Integer id) {
        danhMucSanPhamDAO.deleteById(id);
        return "redirect:/admin/danhmuc";
    }
    
    //QL btn Lưu
    @RequestMapping("admin/danhmuc/save")
    public String saveDanhMuc(@Validated @ModelAttribute("items") DanhMucSanPham danhMucSanPham, Errors errors) {
        if (errors.hasErrors()) {
            return "admin/QLDanhMuc";
        }
        else {
            danhMucSanPhamDAO.save(danhMucSanPham);
            return "redirect:/admin/danhmuc";
        }
    }

    //============================CHỨC NĂNG QL & DS ĐƠN HÀNG=================================================
    //DS
    @GetMapping("admin/donhang")
    public String DSDonHang(Model model, @RequestParam("sortBy") Optional<String> sortBy, @RequestParam("p") Optional<Integer> p) {
        Sort sort = Sort.by(Direction.ASC, sortBy.orElse("donHangId"));
        Pageable pageable = PageRequest.of(p.orElse(0), 5, sort);
        Page<DonHang> donHang = donHangDAO.findAll(pageable);
        var numberOfpages = donHang.getTotalPages();
        model.addAttribute("curr", p.orElse(0));
        model.addAttribute("numberOfpages",numberOfpages);
        // List<DonHang> donHang = donHangDAO.findAll(sort);
        model.addAttribute("items", donHang);
        return "admin/DSDonHang";
    }
    //QL
    @GetMapping("admin/donhang/edit")
    public String QLDonHang(Model model) {
        DonHang items = new DonHang();
        model.addAttribute("items", items);
        return "admin/QLDonHang";
    }

    // DS btn xóa
    @GetMapping("admin/donhang/delete/{id}")
    public String editQLDonHang(Model model, @PathVariable("id") Integer id) {
        donHangDAO.deleteById(id);
        return "redirect:/admin/donhang";
    }
    // DS btn chỉnh sửa
    @GetMapping("admin/donhang/edit/{id}")
    public String deleteQLDonHang(Model model, @PathVariable("id") Integer id) {
        DonHang items = donHangDAO.getById(id);
        model.addAttribute("items", items);
        return "admin/QLDonHang";
    }
    
    //QL btn Lưu
    @RequestMapping("admin/donhang/save")
    public String saveDonHang(@Validated @ModelAttribute("items") DonHang donHang, Errors errors) {
        if(errors.hasErrors()) {
            return "admin/QLDonHang";
        } else {
            // if (donHang.getNgayDatHang() == null) {
            //     donHang.setNgayDatHang(new Date());
            // }
            donHangDAO.save(donHang);
            return "redirect:/admin/donhang";
        }
    }

    //============================CHỨC NĂNG QL & DS MÃ GIẢM GIÁ=================================================
    //DS
    @GetMapping("admin/magiamgia")
    public String DSMaGiamGia(Model model, @RequestParam("sortBy") Optional<String> sortBy, @RequestParam("p") Optional<Integer> p) {
        Sort sort = Sort.by(Direction.ASC, sortBy.orElse("giamGiaId"));
        Pageable pageable = PageRequest.of(p.orElse(0), 5, sort);
        Page<GiamGia> items = giamGiaDAO.findAll(pageable);
        var numberOfpages = items.getTotalPages();
        model.addAttribute("curr", p.orElse(0));
        model.addAttribute("numberOfpages",numberOfpages);
        model.addAttribute("items", items);
        return "admin/DSMaGiamGia";
    }
    //QL
    @GetMapping("admin/magiamgia/edit")
    public String QLMaGiamGia(Model model) {
        GiamGia items = new GiamGia();
        model.addAttribute("items", items);
        return "admin/QLMaGiamGia";
    }
    //DS btn Chỉnh Sửa
    @GetMapping("admin/magiamgia/edit/{id}")
    public String editQLMaGiamGia(Model model, @PathVariable("id") Integer id) {
        GiamGia items = giamGiaDAO.getOne(id);
        model.addAttribute("items", items);
        return "admin/QLMaGiamGia";
    }

    //DS btn xóa
    @GetMapping("admin/magiamgia/delete/{id}")
    public String deleteQLMaGiamGia(Model model, @PathVariable("id") Integer id) {
        giamGiaDAO.deleteById(id);
        return "redirect:/admin/magiamgia";
    }

    //QL btn Lưu
    @RequestMapping("admin/magiamgia/save")
    public String deleteQLMaGiamGia(@Validated @ModelAttribute("items") GiamGia giamGia, Errors errors) {
        if(errors.hasErrors()) {
            return "admin/QLMaGiamGia";
        } else {
            giamGiaDAO.save(giamGia);
            return "redirect:/admin/magiamgia";
        }
    }

    //============================CHỨC NĂNG QL & DS NGƯỜI DÙNG=================================================
    //DS
    @GetMapping("admin/nguoidung")
    public String DSNguoiDung(Model model, @RequestParam("sortBy") Optional<String> sortBy, @RequestParam("p") Optional<Integer> p) {
        Sort sort = Sort.by(Direction.ASC, sortBy.orElse("nguoiDungId"));
        Pageable pageable = PageRequest.of(p.orElse(0), 5, sort);
        Page<NguoiDung> accounts = nguoiDungDAO.findAll(pageable);
        var numberOfpages = accounts.getTotalPages();
        model.addAttribute("curr", p.orElse(0));
        model.addAttribute("numberOfpages",numberOfpages);
        model.addAttribute("items", accounts);
        // paramService.checkDangNhapString("admin/DSNguoiDung", "redirect:/user/index");
        return "admin/DSNguoiDung";
    }
    //QL
    @GetMapping("admin/nguoidung/edit")
    public String QLNguoiDung(Model model) {
        NguoiDung items = new NguoiDung();
        model.addAttribute("items", items);
        return "admin/QLNguoiDung";
    }

    //QL btn chỉnh sửa
    @GetMapping("/admin/nguoidung/edit/{nguoidungid}")
    public String updateNguoiDung(@PathVariable ("nguoidungid") Integer nguoidungid, Model model) {
        NguoiDung items = nguoiDungDAO.getReferenceById(nguoidungid);
        model.addAttribute("items", items);
        return "admin/QLNguoiDung";
    }

    @ModelAttribute("genders")
    public Map<Boolean, String> getGender() {
        Map<Boolean, String> map = new HashMap<>();
        map.put(true, "Nam");
        map.put(false, "Nữ");
        return map;
    }

    //QL btn Lưu
    @RequestMapping("admin/nguoidung/save")
    public String QLNguoiDungUpdate(@Validated @ModelAttribute("items") NguoiDung nguoiDung, Errors errors) {
        if (errors.hasErrors()) {
            return "admin/QLNguoiDung";
        }else {
            if (nguoiDung.getNgayDangKy() == null) {
                // nguoiDung.setNgayDangKy(java.time.LocalDate.now());
            }
            nguoiDungDAO.save(nguoiDung);
        return "redirect:/admin/nguoidung";
        }
        
    }
    //QL btn xóa
    @GetMapping("admin/nguoidung/delete/{nguoidungid}")
    public String QLNguoiDungDelete(@PathVariable("nguoidungid") Integer id) {
        
        nguoiDungDAO.deleteById(id);
        return "redirect:/admin/nguoidung";
    }

    //============================CHỨC NĂNG QL & DS NHÀ CUNG CẤP=================================================
    //DS
    @GetMapping("admin/nhacungcap")
    public String DSNhaCungCap(Model model, @RequestParam("sortBy") Optional<String> sortBy, @RequestParam("p") Optional<Integer> p) {
        Sort sort = Sort.by(Direction.ASC, sortBy.orElse("nhaCungCapId"));
         Pageable pageable = PageRequest.of(p.orElse(0), 5, sort);
        Page<NhaCungCap> items = nhaCungCapDAO.findAll(pageable);
        var numberOfpages = items.getTotalPages();
        model.addAttribute("curr", p.orElse(0));
        model.addAttribute("numberOfpages",numberOfpages);
        
        model.addAttribute("items", items);
        return "admin/DSNhaCungCap";
    }



    //QL
    @GetMapping("admin/nhacungcap/edit")
    public String QLNhaCungCap(Model model) {
        NhaCungCap nhaCungCap = new NhaCungCap();
        model.addAttribute("nhaCungCap", nhaCungCap);
        return "admin/QLNhaCungCap";
    }
    //QL btn chỉnh sửa
    @GetMapping("admin/nhacungcap/edit/{id}")
    public String editQLNhaCungCap(Model model, @PathVariable ("id") Integer id) {
        NhaCungCap nhaCungCap = nhaCungCapDAO.getOne(id);
        model.addAttribute("nhaCungCap", nhaCungCap);
        return "admin/QLNhaCungCap";
    }

    //QL btn xóa
    @GetMapping("admin/nhacungcap/delete/{id}")
    public String deleteQLNhaCungCap(Model model, @PathVariable ("id") Integer id) {
       nhaCungCapDAO.deleteById(id);
        return "redirect:/admin/nhacungcap";
    }

    //QL btn Lưu
    // @ResponseBody
    @RequestMapping ("admin/nhacungcap/save")
    public String saveNhaCungCap(@Validated @ModelAttribute("nhaCungCap") NhaCungCap nhaCungCap, Errors errors){
        if (errors.hasErrors()) {
            return "admin/QLNhaCungCap";
        }else {
            nhaCungCapDAO.save(nhaCungCap);
            return "redirect:/admin/nhacungcap";
        }
        
        
    }



    //============================CHỨC NĂNG QL & DS SẢN PHẨM=================================================
    //DS
    @GetMapping("admin/sanpham")
    public String DSSanPham(Model model, @RequestParam("sortBy") Optional<String> sortBy, @RequestParam("p") Optional<Integer> p) {
        // List<SanPham> items = sanPhamDAO.findAll();
        Sort sort = Sort.by(Direction.ASC, sortBy.orElse("sanPhamId"));
        Pageable pageable = PageRequest.of(p.orElse(0), 5, sort);
        Page<SanPham> items = sanPhamDAO.findAll(pageable);
        var numberOfpages = items.getTotalPages();
        model.addAttribute("curr", p.orElse(0));
        model.addAttribute("numberOfpages",numberOfpages);
        model.addAttribute("items", items);
        return "admin/DSSanPham";
    }

    //QL
    @GetMapping("admin/sanpham/edit")
    public String QLSanPham(Model model) {
        SanPham items = new SanPham();
        model.addAttribute("items", items);
        return "admin/QLSanPham";
    }

    //DS btn chỉnh sửa
     @GetMapping("admin/sanpham/edit/{id}")
    public String editQLSanPham(Model model, @PathVariable ("id") Integer id) {
        SanPham items = sanPhamDAO.getOne(id);
        model.addAttribute("items", items);
        return "admin/QLSanPham";
    }
    //DS btn xóa
    @GetMapping("admin/sanpham/delete/{id}")
    public String deleteQLSanPham(Model model, @PathVariable ("id") Integer id) {
        // SanPham sanPham = sanPhamDAO.getOne(id);
        sanPhamDAO.deleteById(id);
        
        return "redirect:/admin/sanpham";
    }
    //QL btn Lưu
    @RequestMapping("admin/sanpham/save")
    public String saveQLSanPham(@Validated @ModelAttribute("items") SanPham sanPham, Errors errors) {
        if (errors.hasErrors()) {
            return "admin/QLSanPham";
        } else {
            if (sanPham.getNgayTao() == null) {
                sanPham.setNgayTao(java.time.LocalDate.now());
            }
            sanPhamDAO.save(sanPham);
            return "redirect:/admin/sanpham";
        }
    }

    //DS btn thêm cấu hình
     @GetMapping("admin/sanpham/editCauHinh/{id}")
    public String editCauHinhQLSanPham(Model model, @PathVariable ("id") Integer id, @RequestParam("nhomSanPhamId") Integer nhomSanPhamId) {
        
        if(nhomSanPhamId ==  1){
            CauHinhDienThoai item1 = cauHinhDienThoaiDAO.getMaNguoiDung(id);
            CauHinhDienThoai item2 = new CauHinhDienThoai();
            if (item1 != null) {
                model.addAttribute("items", item1);
            }
            else {
                model.addAttribute("items", item2);
            }
            return "admin/QLCauHinhDienThoai";
        }
        else {
             return "admin/QLCauHinhLapTop";
        }
    }

    // Lưu cấu hình điện thoại
    @RequestMapping("admin/sanpham/saveCauHinhDienThoai")
    public String saveCauHinhDienThoai(@ModelAttribute("items") CauHinhDienThoai chdt) {
        cauHinhDienThoaiDAO.save(chdt);
        return "redirect:/admin/sanpham";
    }

    //============================CHỨC NĂNG QL & DS ĐÁNH GIÁ=================================================
    //DS & QL
    @GetMapping("admin/danhgia/edit")
    public String QLDanhGia(Model model) {
        List<DanhGia> items = danhGiaDAO.findAll();
        model.addAttribute("items", items);
        return "admin/QLDanhGia";
    }
    //DS btn xóa
    @GetMapping("admin/danhgia/delete/{id}")
    public String deleteQLDanhGia(Model model, @PathVariable ("id") Integer id) {
        
        danhGiaDAO.deleteById(id);
        
        return "redirect:/admin/danhgia/edit";
    }

    //============================Trang Chủ=================================================
    
    //Thống Kê
    @GetMapping("admin/index")
    public String Home(Model model) {
        List<ThongKeTonKhoDanhMuc> tkTonKho = danhMucSanPhamDAO.thongKeTonKhoDanhMuc();
        model.addAttribute("fruits", tkTonKho);
        model.addAttribute("headerUsername", getNguoiDung().getTenDangNhap());
        return "admin/index";
    }

    //============================CHỨC NĂNG Đăng Nhập=================================================
    

    @GetMapping("/admin/login")
    public String getlogin(Model model, @ModelAttribute("nguoidung") NguoiDung nguoidung) {
        if (sessionService.get("user") != null) {
            nguoidung = sessionService.get("user");
            model.addAttribute("nguoidung", nguoidung);
        } else if (cookieService.get("klb") != null) {
            String user = cookieService.getValue("klb");
            nguoidung = nguoiDungDAO.findByTenDangNhap(user);
            model.addAttribute("nguoidung", nguoidung);
        }
        return "admin/login";
    }

    @PostMapping("/admin/login")
    public String postogin(Model model, @ModelAttribute("nguoidung") NguoiDung nguoidung) {
        try {
            Integer numberFailPass = 0;
            NguoiDung user = nguoiDungDAO.findByTenDangNhap(nguoidung.getTenDangNhap());

            if (numberFailPass == 5) {
                // thực hiện chặn tài khoản
            }
            if (!user.getMatKhau().equals(nguoidung.getMatKhau())) {
                model.addAttribute("message", "Mật khẩu không chính xác!");
                numberFailPass += 1;
            } else {
                String uri = sessionService.get("security-uri");

                if (uri != null) {
                    return "redirect:" + uri;
                } else {
                    model.addAttribute("message", "Đăng nhập thành công!");
                    sessionService.set("user", user);
                    if (paramService.getBoolean("rememberMe", false)) {
                        cookieService.add("klb", String.valueOf(user.getNguoiDungId()), 10);
                    }
                    numberFailPass = 0;
                    return "redirect:/admin/index";
                }
            }
        } catch (Exception e) {
            model.addAttribute("message", "Sai tên đăng nhập hoặc mật khẩu!");
        }
        return "admin/login";
    }

    @RequestMapping("/admin/log-out")
    public String logOut() {
        cookieService.remove("klb");
        return "redirect:/admin/login";
    }

    @GetMapping("/admin/faq")
    public String faq(Model model) {
        model.addAttribute("headerUsername", getNguoiDung().getTenDangNhap());
        return "admin/faq";
    }

    @GetMapping("/admin/profile")
    public String AdminProfile(Model model) {
        model.addAttribute("headerUsername", getNguoiDung().getTenDangNhap());
        return "admin/profile";
    }

     //============================CHỨC NĂNG QL & DS PHIẾU NHẬP=================================================
    //DS
    @GetMapping("admin/phieunhap")
    public String DSPhieuNhap(Model model,@RequestParam("sortBy") Optional<String> sortBy, @RequestParam("p") Optional<Integer> p) {
        Sort sort = Sort.by(Direction.ASC, sortBy.orElse("phieuNhapId"));
        Pageable pageable = PageRequest.of(p.orElse(0), 5, sort);
        Page<PhieuNhap> items = phieuNhapDAO.findAll(pageable);
        var numberOfpages = items.getTotalPages();
        model.addAttribute("curr", p.orElse(0));
        model.addAttribute("numberOfpages",numberOfpages);
        model.addAttribute("items", items);
        return "admin/DSPhieuNhap";
    }
    //QL
    @GetMapping("admin/phieunhap/edit")
    public String QLPhieuNhap(Model model) {
        PhieuNhap tblPhieuNhap = new PhieuNhap();
        model.addAttribute("items", tblPhieuNhap);
        return "admin/QLPhieuNhap";
    }
    //QL btn chỉnh sửa
    @GetMapping("admin/phieunhap/edit/{id}")
    public String editQLPhieuNhap(Model model, @PathVariable("id") Integer id) {
        PhieuNhap formPhieuNhap = phieuNhapDAO.getOne(id);
        model.addAttribute("items", formPhieuNhap);
        
        return "admin/QLPhieuNhap";
    }

    //QL btn xóa
    @GetMapping("admin/phieunhap/delete/{id}")
    public String deleteQLPhieuNhap(Model model, @PathVariable("id") Integer id) {
        phieuNhapDAO.deleteById(id);
        return "redirect:/admin/phieunhap";
    }

    @ModelAttribute("nhaCungCapModel")
    public Map<Integer, String> getNhacungCap() {
        List<NhaCungCap> ncc = nhaCungCapDAO.findAll();
        Map<Integer, String> map = new HashMap<>();
        for (NhaCungCap x : ncc) {
            map.put(x.getNhaCungCapId(), x.getTenNhaCungCap());
        }
        
        return map;
    }
    
    //QL btn Lưu
    @RequestMapping("admin/phieunhap/save")
    public String saveQLPhieuNhap(@Validated @ModelAttribute("items") PhieuNhap phieuNhap, Errors errors) {
        if (errors.hasErrors()) {
            return "admin/QLPhieuNhap";
        }else {
            phieuNhapDAO.save(phieuNhap);
            return "redirect:/admin/phieunhap";
        }
    }


    //============================CHỨC NĂNG QL & DS PHIẾU XUẤT=================================================
    //DS
    @GetMapping("admin/phieuxuat")
    public String DSPhieuXuat(Model model,@RequestParam("sortBy") Optional<String> sortBy, @RequestParam("p") Optional<Integer> p) {
        Sort sort = Sort.by(Direction.ASC, sortBy.orElse("phieuXuatId"));
        Pageable pageable = PageRequest.of(p.orElse(0), 5, sort);
        Page<PhieuXuat> items = phieuXuatDAO.findAll(pageable);
        var numberOfpages = items.getTotalPages();
        model.addAttribute("curr", p.orElse(0));
        model.addAttribute("numberOfpages",numberOfpages);
        model.addAttribute("items", items);
        return "admin/DSPhieuXuat";
    }
    //QL
    @GetMapping("admin/phieuxuat/edit")
    public String QLPhieuXuat(Model model) {
        PhieuXuat items = new PhieuXuat();
        model.addAttribute("items", items);
        return "admin/QLPhieuXuat";
    }
    //DS btn chỉnh sửa
    @GetMapping("admin/phieuxuat/edit/{id}")
    public String editQLPhieuXuat(Model model, @PathVariable("id")Integer id) {
        PhieuXuat items = phieuXuatDAO.getOne(id);
        model.addAttribute("items", items);
        return "admin/QLPhieuXuat";
    }
    //DS btn xóa
    @GetMapping("admin/phieuxuat/delete/{id}")
    public String deleteQLPhieuXuat(Model model, @PathVariable("id")Integer id) {
        phieuXuatDAO.deleteById(id);
        return "redirect:/admin/phieuxuat";
    }
    //QL btn Lưu
    @RequestMapping("admin/phieuxuat/save")
    public String saveQLPhieuXuat(@Validated @ModelAttribute("items")PhieuXuat phieuXuat, Errors errors) {
        if(errors.hasErrors()){
            return "admin/QLPhieuXuat";
        }
        else{
            // NguoiDung nd = nguoiDungDAO.getOne(phieuXuat.getNguoiDung().getNguoiDungId());
            // nd.setSdt(phieuXuat.getNguoiDung().getSdt());
            // nguoiDungDAO.save(nd);
            phieuXuatDAO.save(phieuXuat);
        return "redirect:/admin/phieuxuat";
        }
    }

    @GetMapping ("admin/thongke/tonkho")
    public String thongKeTonKho(Model model) {
        List<ThongKeTonKhoDanhMuc> items = danhMucSanPhamDAO.thongKeTonKhoDanhMuc();
        model.addAttribute("items", items);
        return "admin/ThongKeTonKho";
    }


    //======================================================================
    @ModelAttribute("nguoiDungTenNguoiDung")
    public Map<Integer, String> getNguoiDungTenNguoiDungMap() {
        List<NguoiDung> nguoiDung = nguoiDungDAO.findAll();
        Map<Integer, String> map = new HashMap<>();
        for (NguoiDung x : nguoiDung) {
            map.put(x.getNguoiDungId(), x.getHoTen());
        }
        return map;
    }

    @ModelAttribute("danhMucSanPham")
    public Map<Integer, String> getTenDanhMuc() {
        List<DanhMucSanPham> tenDM = danhMucSanPhamDAO.findAll();
        Map<Integer, String> map = new HashMap<>();
        for (DanhMucSanPham x : tenDM) {
            map.put(x.getDanhMucSanPhamId(), x.getTenDanhMucSanPham());
        }
        return map;
    }

    @ModelAttribute("SanPhamTenSanPham")
    public Map<Long, String> getSanPhamTenSanPhamMap() {
        List<SanPham> sanPham = sanPhamDAO.findAll();
        Map<Long, String> map = new HashMap<>();
        for (SanPham x : sanPham) {
            map.put(x.getSanPhamId(), x.getTenSanPham());
        }
        return map;
    }

}
