package com.klbstore.controller;


import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.klbstore.dao.NguoiDungDAO;
import com.klbstore.dao.SanPhamDAO;
import com.klbstore.dto.SanPhamDTO;
import com.klbstore.model.NguoiDung;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class UserRestController {
    @Autowired
    NguoiDungDAO nguoiDungDAO;

    @Autowired
    SanPhamDAO sanPhamDAO;

    
    @GetMapping("/rest/users")
    public List<SanPhamDTO> getAllUsers(@RequestParam(name = "sanPhamId") Optional<Long> sanPhamId,
            @RequestParam(name = "tenSanPham") Optional<String> tenSanPham,
            @RequestParam(name = "moTa") Optional<String> moTa, @RequestParam(name = "xuatSu") Optional<String> xuatSu,
            @RequestParam(name = "noiBat") Optional<Boolean> noiBat,
            @RequestParam(name = "hienThi") Optional<Boolean> hienThi,
            @RequestParam(name = "giamGia") Optional<Boolean> giamGia,
            @RequestParam(name = "conHang") Optional<Boolean> conHang,
            @RequestParam(name = "giaBan") Optional<Double> giaBan,
            @RequestParam(name = "minGiaBan") Optional<Double> minGiaBan,
            @RequestParam(name = "maxGiaBan") Optional<Double> maxGiaBan,
            @RequestParam(name = "ngayTao") Optional<LocalDate> ngayTao,
            @RequestParam(name = "minNgayTao") Optional<LocalDate> minNgayTao,
            @RequestParam(name = "maxNgayTao") Optional<LocalDate> maxNgayTao, @RequestParam(name = "soLuongTrongKho") Optional<Integer> soLuongTrongKho, @RequestParam(name = "minSoLuongTrongKho") Optional<Integer> minSoLuongTrongKho, @RequestParam(name = "maxSoLuongTrongKho") Optional<Integer> maxSoLuongTrongKho, @RequestParam(name = "viTriLuuTru") Optional<String> viTriLuuTru) {
        return sanPhamDAO.getSanPham(sanPhamId.orElse(null), tenSanPham.orElse(""),
                moTa.orElse(""), xuatSu.orElse(""), noiBat.orElse(null), hienThi.orElse(true), giamGia.orElse(null),
                conHang.orElse(true), giaBan.orElse(null), minGiaBan.orElse(null), maxGiaBan.orElse(null),
                ngayTao.orElse(null), minNgayTao.orElse(null), maxNgayTao.orElse(null), soLuongTrongKho.orElse(null), minSoLuongTrongKho.orElse(null), maxSoLuongTrongKho.orElse(null), viTriLuuTru.orElse("")).stream()
                .filter(sp -> sp.getSanPham().getSanPhamChiTietSanPhams().stream().allMatch(ctsp -> ctsp.getSoLuongTrongKho() > 0))
                .collect(Collectors.toList());
    }

    


    @GetMapping("/rest/users/{id}")
    public NguoiDung getUserById(@PathVariable("id") Integer id) {
        return nguoiDungDAO.findById(id).orElse(null);
    }

    @PostMapping("/rest/users")
    public NguoiDung createUser(@RequestBody NguoiDung nguoiDung) {
        return nguoiDungDAO.save(nguoiDung);
    }

    @PatchMapping("/rest/users/{id}")
    public NguoiDung updateUser(@PathVariable("id") String id, @RequestBody NguoiDung nguoiDung) {
        return nguoiDungDAO.save(nguoiDung);
    }

    @DeleteMapping("/rest/users/{id}")
    public void deleteUser(@PathVariable("id") Integer id) {
        nguoiDungDAO.deleteById(id);
    }

    private Map<Integer, NguoiDung> map = new HashMap<>();

    @GetMapping("/api/users")
    public Collection<NguoiDung> getStudents() {
        return map.values();
    }

}
