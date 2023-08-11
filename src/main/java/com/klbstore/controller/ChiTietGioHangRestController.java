package com.klbstore.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.klbstore.config.SecurityRestTemplate;
import com.klbstore.dao.ChiTietGioHangDAO;
import com.klbstore.dao.MauSacDAO;
import com.klbstore.delivery.District;
import com.klbstore.delivery.DistrictResponse;
import com.klbstore.delivery.Province;
import com.klbstore.delivery.ProvinceResponse;
import com.klbstore.delivery.Ward;
import com.klbstore.delivery.WardResponse;
import com.klbstore.dto.AllChiTietGioHangDTO;
import com.klbstore.dto.ChiTietGioHangDTO;
import com.klbstore.model.NguoiDung;
import com.klbstore.service.SessionService;
import com.klbstore.service.ShoppingCartService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class ChiTietGioHangRestController {
    @Autowired
    ChiTietGioHangDAO chiTietGioHangDAO;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    MauSacDAO mauSacDAO;

    @Autowired
    SecurityRestTemplate restTemplate;

    @GetMapping("/authozied/login")
    public ResponseEntity<Integer> login() {
        NguoiDung nguoiDung = sessionService.get("user");
        return ResponseEntity.ok().body(nguoiDung.getNguoiDungId());
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addToCart(@RequestParam("colorId") int mauSacId,
            @RequestParam("quantity") Optional<Integer> soLuong,
            @RequestParam(value = "userId", defaultValue = "0") int userId) {
        NguoiDung nguoiDung = sessionService.get("user");
        // int actualUserId = (userId != 0) ? userId : nguoiDung.getNguoiDungId();
        if (nguoiDung != null) {
            Map<String, Object> result = chiTietGioHangDAO.themSanPhamVaoGioHang(mauSacId, soLuong.orElse(1),
                    nguoiDung.getNguoiDungId());
            String thongBao = (String) result.get("ThongBao");

            if (thongBao != null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", thongBao);
                return ResponseEntity.ok().body(errorResponse);
            }

            return ResponseEntity.ok().build();

        } else {
            String errorMessage = shoppingCartService.addToCart(mauSacId, soLuong.orElse(1));

            if (errorMessage != null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", errorMessage);
                return ResponseEntity.ok().body(errorResponse);
            }
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateToCart(@RequestParam("colorId") int mauSacId,
            @RequestParam("quantity") Optional<Integer> soLuong) {
        NguoiDung nguoiDung = sessionService.get("user");
        if (nguoiDung != null) {
            Map<String, Object> result = chiTietGioHangDAO.capNhatSanPhamTrongGioHang(mauSacId, soLuong.orElse(1),
                    nguoiDung.getNguoiDungId());
            String thongBao = (String) result.get("ThongBao");

            System.out.println(thongBao);
            if (thongBao != null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", thongBao);
                return ResponseEntity.ok().body(errorResponse);
            }

            return ResponseEntity.ok().build();
        } else {
            String errorMessage = shoppingCartService.updateToCart(mauSacId, soLuong.orElse(1));

            if (errorMessage != null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", errorMessage);
                return ResponseEntity.ok().body(errorResponse);
            }

            return ResponseEntity.ok().build();

        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteToCart(@RequestParam("colorId") int mauSacId) {
        NguoiDung nguoiDung = sessionService.get("user");
        if (nguoiDung != null) {
            chiTietGioHangDAO.xoaSanPhamKhoiGioHang(mauSacId, nguoiDung.getNguoiDungId());
            return ResponseEntity.ok().build();
        } else {
            String errorMessage = shoppingCartService.deleteFromCart(mauSacId);

            if (errorMessage != null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", errorMessage);
                return ResponseEntity.ok().body(errorResponse);
            }
        }

        return ResponseEntity.ok().build();
    }

    @Autowired
    SessionService sessionService;

    public Integer getProvinceIDByName(String provinceName) {
        ProvinceResponse provinceResponse = restTemplate.getProvinces();

        List<Province> provinces = provinceResponse.getData();
        Optional<Province> optionalProvince = provinces.stream()
                .filter(province -> province.getProvinceName().equals(provinceName))
                .findFirst();

        return optionalProvince.map(province -> Integer.parseInt(province.getProvinceID())).orElse(null);
    }

    public Integer getDistrictIDByName(String districtName, Integer provinceID) {
        DistrictResponse districtResponse = restTemplate.getDistricts(provinceID);

        List<District> districts = districtResponse.getData();
        Optional<District> optionalDistrict = districts.stream()
                .filter(district -> district.getDistrictName().equals(districtName))
                .findFirst();

        return optionalDistrict.map(district -> Integer.parseInt(district.getDistrictID())).orElse(null);
    }

    public Integer getWardIDByName(String wardName, Integer districtID) {
        WardResponse wardResponse = restTemplate.getWards(districtID);

        List<Ward> wards = wardResponse.getData();
        Optional<Ward> optionalWard = wards.stream()
                .filter(ward -> ward.getWardName().equals(wardName))
                .findFirst();
        return optionalWard.map(ward -> Integer.parseInt(ward.getWardCode())).orElse(null);
    }

    @GetMapping("/giohanginfo")
    public Map<String, Object> getGioHangInfo() {
        NguoiDung nguoiDung = sessionService.get("user");
        int tongSoSanPham = 0;
        double tongTien = 0;
        if (nguoiDung != null) {
            if (chiTietGioHangDAO.tongSoSanPhamTrongGioHang(nguoiDung.getNguoiDungId()) != null
                    && chiTietGioHangDAO.tinhTongTienTrongGioHang(nguoiDung.getNguoiDungId()) != null) {
                tongSoSanPham = chiTietGioHangDAO.tongSoSanPhamTrongGioHang(nguoiDung.getNguoiDungId());
                tongTien = chiTietGioHangDAO.tinhTongTienTrongGioHang(nguoiDung.getNguoiDungId());
            }
        } else {
            if (shoppingCartService.getShoppingCartDTO() != null
                    && shoppingCartService.getShoppingCartDTO().getTongSoLuong() != null
                    && shoppingCartService.getShoppingCartDTO().getTongTien() != null) {
                tongSoSanPham = shoppingCartService.getShoppingCartDTO().getTongSoLuong();
                tongTien = shoppingCartService.getShoppingCartDTO().getTongTien();
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("tongSoSanPham", tongSoSanPham);
        response.put("tongTien", tongTien);

        return response;
    }

    @GetMapping("/get")
    public ResponseEntity<AllChiTietGioHangDTO> hello() {
        NguoiDung nguoiDung = sessionService.get("user");
        if (nguoiDung != null) {
            String diaChi = nguoiDung.getDiaChi();
            String[] diaChiComponents = diaChi.split(","); // Tách theo dấu phẩy
            // String diaChiCuThe = diaChiComponents[0].trim(); // Địa chỉ cụ thể
            String xaPhuong = diaChiComponents[diaChiComponents.length - 3].trim(); // Phường/Xã
            String quanHuyen = diaChiComponents[diaChiComponents.length - 2].trim(); // Quận/Huyện
            String tinhThanh = diaChiComponents[diaChiComponents.length - 1].trim(); // Tỉnh/Thành phố
            Integer provinceId = getProvinceIDByName(tinhThanh);
            Integer districtId = getDistrictIDByName(quanHuyen, provinceId);
            String wardId = String.valueOf(getWardIDByName(xaPhuong, districtId));
            Integer weight = chiTietGioHangDAO.tongCanNangTrongGioHang(nguoiDung.getNguoiDungId());
            Integer length = chiTietGioHangDAO.tongChieuDaiTrongGioHang(nguoiDung.getNguoiDungId());
            Integer width = chiTietGioHangDAO.tongChieuRongTrongGioHang(nguoiDung.getNguoiDungId());
            Integer height = chiTietGioHangDAO.tongDoDayTrongGioHang(nguoiDung.getNguoiDungId());
            Double tongPhiVanChuyen = restTemplate
                    .calculateShippingFee(districtId, wardId, height, length, weight, width).getData().getTotal();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String ngayGiaoHangDuKien = dateFormat
                    .format(restTemplate.getDateDelivery(districtId, wardId).getData().getLeadtime());
            chiTietGioHangDAO.kiemTraVaXoaSanPhamKhongHopLeTrongGioHang(nguoiDung.getNguoiDungId());
            AllChiTietGioHangDTO allChiTietGioHangDTO = new AllChiTietGioHangDTO(
                    chiTietGioHangDAO.layDanhSachSanPhamTrongGioHangTheoNguoiDung(nguoiDung.getNguoiDungId()),
                    chiTietGioHangDAO.tongSoSanPhamTrongGioHang(nguoiDung.getNguoiDungId()),
                    chiTietGioHangDAO.tinhTongTienTrongGioHang(nguoiDung.getNguoiDungId()), tongPhiVanChuyen,
                    ngayGiaoHangDuKien);
            return new ResponseEntity<>(allChiTietGioHangDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(shoppingCartService.getShoppingCartDTO(), HttpStatus.OK);
        }
    }

    @GetMapping("/findColorId")
    public ResponseEntity<Map<String, Object>> color(@RequestParam("colorId") int mauSacId) {
        NguoiDung nguoiDung = sessionService.get("user");
        if (nguoiDung != null) {
            chiTietGioHangDAO.kiemTraVaXoaSanPhamKhongHopLeTrongGioHang(nguoiDung.getNguoiDungId());
            AllChiTietGioHangDTO allChiTietGioHangDTO = new AllChiTietGioHangDTO(
                    chiTietGioHangDAO.layDanhSachSanPhamTrongGioHangTheoNguoiDung(nguoiDung.getNguoiDungId()),
                    chiTietGioHangDAO.tongSoSanPhamTrongGioHang(nguoiDung.getNguoiDungId()),
                    chiTietGioHangDAO.tinhTongTienTrongGioHang(nguoiDung.getNguoiDungId()), null,
                    null);
            Map<String, Object> response = new HashMap<>();
            if (allChiTietGioHangDTO.getDanhSachSanPhamTrongGioHang() != null) {
                for (ChiTietGioHangDTO chiTietGioHangDTO : allChiTietGioHangDTO.getDanhSachSanPhamTrongGioHang()) {
                    if (chiTietGioHangDTO.getChiTietGioHang().getMauSac().getMauSacId() == mauSacId) {
                        response.put("tongGia", chiTietGioHangDTO.getTongGia());
                        response.put("soLuong", chiTietGioHangDTO.getChiTietGioHang().getSoLuong());
                    }
                }
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            if (shoppingCartService.getShoppingCartDTO().getDanhSachSanPhamTrongGioHang() != null) {
                for (ChiTietGioHangDTO chiTietGioHangDTO : shoppingCartService.getShoppingCartDTO()
                        .getDanhSachSanPhamTrongGioHang()) {
                    if (chiTietGioHangDTO.getChiTietGioHang().getMauSac().getMauSacId() == mauSacId) {
                        response.put("tongGia", chiTietGioHangDTO.getTongGia());
                        response.put("soLuong", chiTietGioHangDTO.getChiTietGioHang().getSoLuong());
                    }
                }
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/checkoutCart")
    public ResponseEntity<AllChiTietGioHangDTO> allcheckout(@RequestParam("userId") String us,
            @RequestParam("address") String dc) {
        Integer nguoiDungId = Integer.parseInt(us);
        String diaChi = dc;
        String[] diaChiComponents = diaChi.split(",");
        String xaPhuong = diaChiComponents[diaChiComponents.length - 3].trim();
        String quanHuyen = diaChiComponents[diaChiComponents.length - 2].trim();
        String tinhThanh = diaChiComponents[diaChiComponents.length - 1].trim();
        Integer provinceId = getProvinceIDByName(tinhThanh);
        Double tongPhiVanChuyen = 0.0;
        String ngayGiaoHangDuKien = null;
        if (provinceId == null) {
        } else {
            Integer districtId = getDistrictIDByName(quanHuyen, provinceId);
            String wardId = String.valueOf(getWardIDByName(xaPhuong, districtId));
            Integer weight = chiTietGioHangDAO.tongCanNangTrongGioHang(nguoiDungId);
            Integer length = chiTietGioHangDAO.tongChieuDaiTrongGioHang(nguoiDungId);
            Integer width = chiTietGioHangDAO.tongChieuRongTrongGioHang(nguoiDungId);
            Integer height = chiTietGioHangDAO.tongDoDayTrongGioHang(nguoiDungId);
            try {
                tongPhiVanChuyen = restTemplate
                        .calculateShippingFee(districtId, wardId, height, length, weight, width).getData().getTotal();

            } catch (Exception e) {
                tongPhiVanChuyen = 0.0;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            ngayGiaoHangDuKien = dateFormat
                    .format(restTemplate.getDateDelivery(districtId, wardId).getData().getLeadtime());
        }
        chiTietGioHangDAO.kiemTraVaXoaSanPhamKhongHopLeTrongGioHang(nguoiDungId);
        AllChiTietGioHangDTO allChiTietGioHangDTO = new AllChiTietGioHangDTO(
                chiTietGioHangDAO.layDanhSachSanPhamTrongGioHangTheoNguoiDung(nguoiDungId),
                chiTietGioHangDAO.tongSoSanPhamTrongGioHang(nguoiDungId),
                chiTietGioHangDAO.tinhTongTienTrongGioHang(nguoiDungId), tongPhiVanChuyen,
                ngayGiaoHangDuKien);
        return new ResponseEntity<>(allChiTietGioHangDTO, HttpStatus.OK);
    }

    @GetMapping("/checkoutSpecifiedCart")
    public ResponseEntity<Map<String, Object>> specifiedCart(@RequestParam("userId") String us,
            @RequestParam("address") String dc) {
        Integer nguoiDungId = Integer.parseInt(us);
        String diaChi = dc;
        String[] diaChiComponents = diaChi.split(",");
        String xaPhuong = diaChiComponents[diaChiComponents.length - 3].trim();
        String quanHuyen = diaChiComponents[diaChiComponents.length - 2].trim();
        String tinhThanh = diaChiComponents[diaChiComponents.length - 1].trim();
        Integer provinceId = getProvinceIDByName(tinhThanh);
        if (provinceId == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("tongPhiVanChuyen", null);
            response.put("ngayGiaoHangDuKien", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Integer districtId = getDistrictIDByName(quanHuyen, provinceId);
            String wardId = String.valueOf(getWardIDByName(xaPhuong, districtId));
            Integer weight = chiTietGioHangDAO.tongCanNangTrongGioHang(nguoiDungId);
            Integer length = chiTietGioHangDAO.tongChieuDaiTrongGioHang(nguoiDungId);
            Integer width = chiTietGioHangDAO.tongChieuRongTrongGioHang(nguoiDungId);
            Integer height = chiTietGioHangDAO.tongDoDayTrongGioHang(nguoiDungId);
            Double tongPhiVanChuyen = 0.0;
            try {
                tongPhiVanChuyen = restTemplate
                        .calculateShippingFee(districtId, wardId, height, length, weight, width).getData().getTotal();

            } catch (Exception e) {
                tongPhiVanChuyen = 0.0;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String ngayGiaoHangDuKien = dateFormat
                    .format(restTemplate.getDateDelivery(districtId, wardId).getData().getLeadtime());
            chiTietGioHangDAO.kiemTraVaXoaSanPhamKhongHopLeTrongGioHang(nguoiDungId);
            Map<String, Object> response = new HashMap<>();
            response.put("tongPhiVanChuyen", tongPhiVanChuyen);
            response.put("ngayGiaoHangDuKien", ngayGiaoHangDuKien);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

}
