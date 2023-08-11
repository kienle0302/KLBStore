package com.klbstore.dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.klbstore.model.DanhMucSanPham;
import com.klbstore.model.ThongKeTonKhoDanhMuc;


public interface DanhMucSanPhamDAO extends JpaRepository<DanhMucSanPham, Integer> {
    //Thống kê tồn kho theo danh mục
    @Query(value =  "SELECT new ThongKeTonKhoDanhMuc(p.danhMucSanPham.danhMucSanPhamId, p.danhMucSanPham.tenDanhMucSanPham, count(p))"
     + " FROM SanPham p"
     + " GROUP BY (p.danhMucSanPham.danhMucSanPhamId, p.danhMucSanPham.tenDanhMucSanPham)"
    )
    List<ThongKeTonKhoDanhMuc> thongKeTonKhoDanhMuc();

    //thống kê tồn kho theo sản phẩm
   
}
