package com.klbstore.controller;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.klbstore.dao.SanPhamDAO;
import com.klbstore.dto.SanPhamDTO;
import com.klbstore.model.DanhGia;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class SanPhamRestController {
        @Autowired
        SanPhamDAO sanPhamDAO;

        @GetMapping("/rest/sanpham")
        public ResponseEntity<Page<SanPhamDTO>> getPg(
                        @RequestParam(name = "soLuongTrongKho") Optional<Integer> soLuongTrongKho,
                        @RequestParam(name = "minSoLuongTrongKho") Optional<Integer> minSoLuongTrongKho,
                        @RequestParam(name = "maxSoLuongTrongKho") Optional<Integer> maxSoLuongTrongKho,
                        @RequestParam("mauSacId") Optional<Integer> mauSacId,
                        @RequestParam("tenMauSac") Optional<String> tenMauSac,
                        @RequestParam("duongDanAnh") Optional<String> duongDanAnh,
                        @RequestParam("viTriLuuTru") Optional<String> viTriLuuTru,
                        @RequestParam("conHang") Optional<Boolean> conHang,
                        @RequestParam(name = "nhomSanPhamId") Optional<Integer> nhomSanPhamId,
                        @RequestParam("tenNhomSanPham") Optional<String> tenNhomSanPham,
                        @RequestParam("danhMucSanPhamId") Optional<Integer> danhMucSanPhamId,
                        @RequestParam("tenDanhMucSanPham") Optional<String> tenDanhMucSanPham,
                        @RequestParam("danhMucConId") Optional<Integer> danhMucConId,
                        @RequestParam("tenDanhMucCon") Optional<String> tenDanhMucCon,
                        @RequestParam(name = "sanPhamId") Optional<Long> sanPhamId,
                        @RequestParam("tenSanPham") Optional<String> tenSanPham,
                        @RequestParam("moTa") Optional<String> moTa,
                        @RequestParam("xuatSu") Optional<String> xuatSu,
                        @RequestParam("giaBan") Optional<Double> giaBan,
                        @RequestParam("minGiaBan") Optional<Double> minGiaBan,
                        @RequestParam("maxGiaBan") Optional<Double> maxGiaBan,
                        @RequestParam("soLuotXem") Optional<Integer> soLuotXem,
                        @RequestParam("minSoLuotXem") Optional<Integer> minSoLuotXem,
                        @RequestParam("maxSoLuotXem") Optional<Integer> maxSoLuotXem,
                        @RequestParam("ngayTao") Optional<LocalDate> ngayTao,
                        @RequestParam("minNgayTao") Optional<LocalDate> minNgayTao,
                        @RequestParam("maxNgayTao") Optional<LocalDate> maxNgayTao,
                        @RequestParam("noiBat") Optional<Boolean> noiBat,
                        @RequestParam("hienThi") Optional<Boolean> hienThi,
                        @RequestParam("giamGia") Optional<Boolean> giamGia,
                        @RequestParam("sanPhamLienQuan") Optional<Integer> sanPhamLienQuan,
                        @RequestParam("sortBy") Optional<String> sortBy,
                        @RequestParam(name = "page", defaultValue = "1") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size) {
                List<SanPhamDTO> list = sanPhamDAO
                                .getDTO(conHang.orElse(true) == true
                                                ? sanPhamDAO.findSanPhamConHang(soLuongTrongKho.orElse(null),
                                                                minSoLuongTrongKho.orElse(null),
                                                                maxSoLuongTrongKho.orElse(null), mauSacId.orElse(null), tenMauSac.orElse(null),
                                                                duongDanAnh.orElse(null),
                                                                viTriLuuTru.orElse(null))
                                                : sanPhamDAO.findSanPhamHetHang(soLuongTrongKho.orElse(null),
                                                                minSoLuongTrongKho.orElse(null),
                                                                maxSoLuongTrongKho.orElse(null), mauSacId.orElse(null), tenMauSac.orElse(null),
                                                                duongDanAnh.orElse(null),
                                                                viTriLuuTru.orElse(null)),
                                                nhomSanPhamId.orElse(null), tenNhomSanPham.orElse(null),
                                                danhMucSanPhamId.orElse(null),
                                                tenDanhMucSanPham.orElse(null), danhMucConId.orElse(null),
                                                sanPhamId.orElse(null),
                                                tenSanPham.orElse(null), moTa.orElse(null), xuatSu.orElse(null),
                                                giaBan.orElse(null),
                                                minGiaBan.orElse(null), maxGiaBan.orElse(null), soLuotXem.orElse(null),
                                                minSoLuotXem.orElse(null), maxSoLuotXem.orElse(null),
                                                ngayTao.orElse(null),
                                                minNgayTao.orElse(null), maxNgayTao.orElse(null), noiBat.orElse(null),
                                                hienThi.orElse(null),
                                                giamGia.orElse(null), sanPhamLienQuan.orElse(null), sortBy.orElse(null))
                                .stream()
                                .peek(dto -> {
                                        if (dto.getSanPham() != null && dto.getSanPham().getSanPhamDanhGias() != null) {
                                                dto.getSanPham().getSanPhamDanhGias()
                                                                .sort(Comparator.comparing(DanhGia::getNgayDanhGia)
                                                                                .reversed());
                                        }
                                })
                                .collect(Collectors.toList());
                int adjustedPage = Math.max(0, page - 1);

                int start = adjustedPage * size;
                int end = Math.min(start + size, list.size());

                List<SanPhamDTO> pagedSanPham = list.subList(start, end);
                if (page <= 0) {
                        return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(new PageImpl<>(pagedSanPham, PageRequest.of(adjustedPage, size), list.size()));

        }

        @GetMapping("/rest/xinchao")
        public ResponseEntity<List<SanPhamDTO>> sanPham(
                        @RequestParam(name = "soLuongTrongKho") Optional<Integer> soLuongTrongKho,
                        @RequestParam(name = "minSoLuongTrongKho") Optional<Integer> minSoLuongTrongKho,
                        @RequestParam(name = "maxSoLuongTrongKho") Optional<Integer> maxSoLuongTrongKho,
                        @RequestParam("mauSacId") Optional<Integer> mauSacId,
                        @RequestParam("tenMauSac") Optional<String> tenMauSac,
                        @RequestParam("duongDanAnh") Optional<String> duongDanAnh,
                        @RequestParam("viTriLuuTru") Optional<String> viTriLuuTru,
                        @RequestParam("conHang") Optional<Boolean> conHang,
                        @RequestParam(name = "nhomSanPhamId") Optional<Integer> nhomSanPhamId,
                        @RequestParam("tenNhomSanPham") Optional<String> tenNhomSanPham,
                        @RequestParam("danhMucSanPhamId") Optional<Integer> danhMucSanPhamId,
                        @RequestParam("tenDanhMucSanPham") Optional<String> tenDanhMucSanPham,
                        @RequestParam("danhMucConId") Optional<Integer> danhMucConId,
                        @RequestParam("tenDanhMucCon") Optional<String> tenDanhMucCon,
                        @RequestParam(name = "sanPhamId") Optional<Long> sanPhamId,
                        @RequestParam("tenSanPham") Optional<String> tenSanPham,
                        @RequestParam("moTa") Optional<String> moTa,
                        @RequestParam("xuatSu") Optional<String> xuatSu,
                        @RequestParam("giaBan") Optional<Double> giaBan,
                        @RequestParam("minGiaBan") Optional<Double> minGiaBan,
                        @RequestParam("maxGiaBan") Optional<Double> maxGiaBan,
                        @RequestParam("soLuotXem") Optional<Integer> soLuotXem,
                        @RequestParam("minSoLuotXem") Optional<Integer> minSoLuotXem,
                        @RequestParam("maxSoLuotXem") Optional<Integer> maxSoLuotXem,
                        @RequestParam("ngayTao") Optional<LocalDate> ngayTao,
                        @RequestParam("minNgayTao") Optional<LocalDate> minNgayTao,
                        @RequestParam("maxNgayTao") Optional<LocalDate> maxNgayTao,
                        @RequestParam("noiBat") Optional<Boolean> noiBat,
                        @RequestParam("hienThi") Optional<Boolean> hienThi,
                        @RequestParam("giamGia") Optional<Boolean> giamGia,
                        @RequestParam("sanPhamLienQuan") Optional<Integer> sanPhamLienQuan,
                        @RequestParam("sortBy") Optional<String> sortBy) {

                List<SanPhamDTO> sp = sanPhamDAO
                                .getDTO(conHang.orElse(true) == true
                                                ? sanPhamDAO.findSanPhamConHang(soLuongTrongKho.orElse(null),
                                                                minSoLuongTrongKho.orElse(null),
                                                                maxSoLuongTrongKho.orElse(null), mauSacId.orElse(null), tenMauSac.orElse(null),
                                                                duongDanAnh.orElse(null),
                                                                viTriLuuTru.orElse(null))
                                                : sanPhamDAO.findSanPhamHetHang(soLuongTrongKho.orElse(null),
                                                                minSoLuongTrongKho.orElse(null),
                                                                maxSoLuongTrongKho.orElse(null), mauSacId.orElse(null), tenMauSac.orElse(null),
                                                                duongDanAnh.orElse(null),
                                                                viTriLuuTru.orElse(null)),
                                                nhomSanPhamId.orElse(null), tenNhomSanPham.orElse(null),
                                                danhMucSanPhamId.orElse(null),
                                                tenDanhMucSanPham.orElse(null), danhMucConId.orElse(null),
                                                sanPhamId.orElse(null),
                                                tenSanPham.orElse(null), moTa.orElse(null), xuatSu.orElse(null),
                                                giaBan.orElse(null),
                                                minGiaBan.orElse(null), maxGiaBan.orElse(null), soLuotXem.orElse(null),
                                                minSoLuotXem.orElse(null), maxSoLuotXem.orElse(null),
                                                ngayTao.orElse(null),
                                                minNgayTao.orElse(null), maxNgayTao.orElse(null), noiBat.orElse(null),
                                                hienThi.orElse(null),
                                                giamGia.orElse(null), sanPhamLienQuan.orElse(null), sortBy.orElse(null))
                                .stream()
                                .peek(dto -> {
                                        if (dto.getSanPham() != null && dto.getSanPham().getSanPhamDanhGias() != null) {
                                                dto.getSanPham().getSanPhamDanhGias()
                                                                .sort(Comparator.comparing(DanhGia::getNgayDanhGia)
                                                                                .reversed());
                                        }
                                })
                                .collect(Collectors.toList());

                return ResponseEntity.ok(sp);
        }

}
