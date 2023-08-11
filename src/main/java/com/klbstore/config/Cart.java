package com.klbstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.klbstore.dao.ChiTietGioHangDAO;
import com.klbstore.model.NguoiDung;
import com.klbstore.service.SessionService;
import com.klbstore.service.ShoppingCartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Cart implements HandlerInterceptor {

    @Autowired
    SessionService sessionService;

    private final ChiTietGioHangDAO chiTietGioHangDAO;

    public Cart(ChiTietGioHangDAO chiTietGioHangDAO) {
        this.chiTietGioHangDAO = chiTietGioHangDAO;
    }

    @Autowired
    ShoppingCartService shoppingCartService;

    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
    ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            int tongSoSanPham = 0;
            double tongTien = 0;
            NguoiDung nguoiDung = sessionService.get("user");
            if (nguoiDung != null) {
                if(chiTietGioHangDAO.tongSoSanPhamTrongGioHang(nguoiDung.getNguoiDungId()) != null && chiTietGioHangDAO.tinhTongTienTrongGioHang(nguoiDung.getNguoiDungId()) != null){
                    tongSoSanPham = chiTietGioHangDAO.tongSoSanPhamTrongGioHang(nguoiDung.getNguoiDungId());
                    tongTien = chiTietGioHangDAO.tinhTongTienTrongGioHang(nguoiDung.getNguoiDungId());
                }
            } else {
                if (shoppingCartService.getShoppingCartDTO() != null && shoppingCartService.getShoppingCartDTO().getTongSoLuong() != null && shoppingCartService.getShoppingCartDTO().getTongTien() != null){
                    tongSoSanPham = shoppingCartService.getShoppingCartDTO().getTongSoLuong();
                    tongTien = shoppingCartService.getShoppingCartDTO().getTongTien();
                }
            }
            if (tongSoSanPham != 0) {
                modelAndView.addObject("tongSoSanPham", tongSoSanPham);
            }
            
            if (tongTien != 0) {
                modelAndView.addObject("tongTien", tongTien);
            }            
        }
    }
}
