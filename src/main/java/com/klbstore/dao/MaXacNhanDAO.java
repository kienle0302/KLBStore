package com.klbstore.dao;
import org.springframework.data.jpa.repository.JpaRepository;

import com.klbstore.model.MaXacNhan;

public interface MaXacNhanDAO extends JpaRepository<MaXacNhan, Integer> {
    MaXacNhan findByNguoiDungId(int nguoiDungId);
}
