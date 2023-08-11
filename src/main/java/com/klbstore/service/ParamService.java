package com.klbstore.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.klbstore.dao.NguoiDungDAO;
import com.klbstore.model.NguoiDung;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ParamService {
    @Autowired
    HttpServletRequest request;
    @Autowired
    CookieService cookieService;
    @Autowired
    SessionService sessionService;
    @Autowired
    NguoiDungDAO nguoidungDAO;

    public boolean // check phonenumber format
    isValidPhonenumber(String phonenumber) {

        // Regex to check valid phonenumber.
        String regex = "/^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$/";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the phonenumber is empty
        // return false
        if (phonenumber == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given phonenumber
        // and regular expression.
        Matcher m = p.matcher(phonenumber);

        // Return if the password
        // matched the ReGex
        return m.matches();

    }

    public boolean // check password format
            isValidPassword(String password) {
        // Regex to check valid password.
        String regex = "^(?=.*[A-Z].*[A-Z])(?=.*[!@#$&*])(?=.*[0-9].*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{8}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if (password == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);

        // Return if the password
        // matched the ReGex
        return m.matches();
    }

    public boolean // check username format
            isValidUsername(String username) {
        // Regex to check valid username.
        String regex = "/^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$/";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the username is empty
        // return false
        if (username == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given username
        // and regular expression.
        Matcher m = p.matcher(username);

        // Return if the username
        // matched the ReGex
        return m.matches();
    }

    // Check người dùng đăng nhập
    public NguoiDung checkDangNhap() {
        // check login
        if (cookieService.get("user") != null) {
            for (NguoiDung i : nguoidungDAO.findAll()) {
                if (cookieService.getValue("user").equalsIgnoreCase(i.getTenDangNhap())) {
                    return i;
                }
            }
        } else if (sessionService.get("username") != null && cookieService.get("user") == null) {
            for (NguoiDung i : nguoidungDAO.findAll()) {
                if (sessionService.get("username").equals(i.getTenDangNhap())) {
                    return i;
                }
            }
        }
        return null;
    }

    // Check đăng nhập
    public String checkDangNhapString(String defaultAdmin, String defaultUser) {
        if (checkDangNhap() != null) {
            if (checkDangNhap().getQuyenDangNhap()) {
                return defaultAdmin;
            } else if (!checkDangNhap().getQuyenDangNhap()) {
                return defaultUser;
            }
        }
        return "redirect:/user/login-register";
    }

    // /**
    // * Đọc chuỗi giá trị của tham số
    // * @param name tên tham số
    // * @param defaultValue giá trị mặc định
    // * @return giá trị tham số hoặc giá trị mặc định nếu không tồn tại
    // */
    public String getString(String name, String defaultValue) {
        if (request.getParameter(name) != null)
            return name;
        else
            return null;
    }

    // /**
    // * Đọc số nguyên giá trị của tham số
    // * @param name tên tham số
    // * @param defaultValue giá trị mặc định
    // * @return giá trị tham số hoặc giá trị mặc định nếu không tồn tại
    // */
    public int getInt(String name, int defaultValue) {
        if (request.getParameter(name) != null)
            return 0;
        else
            return defaultValue;
    }

    // /**
    // * Đọc số thực giá trị của tham số
    // * @param name tên tham số
    // * @param defaultValue giá trị mặc định
    // * @return giá trị tham số hoặc giá trị mặc định nếu không tồn tại
    // */
    public double getDouble(String name, double defaultValue) {
        if (request.getParameter(name) != null)
            return 0;
        else
            return defaultValue;
    }
    public boolean getBoolean(String name, boolean defaultValue) {
        if (request.getParameter(name) != null)
            return true;
        else
            return defaultValue;
    }
    // * @throws RuntimeException lỗi sai định dạng
    // */
    public Date getDate(String name, String pattern) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);

            String dateInString = "7-Jun-2013";
            Date date = formatter.parse(dateInString);
            
            if (!request.getParameter(name).isEmpty())
            return date;
            else
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // /**
    // * Lưu file upload vào thư mục
    // * @param file chứa file upload từ client
    // * @param path đường dẫn tính từ webroot
    // * @return đối tượng chứa file đã lưu hoặc null nếu không có file upload
    // * @throws RuntimeException lỗi lưu file
    // */
    ServletContext app;
    public File save(MultipartFile file, String path) {
        File dir = new File(app.getRealPath(path));
        if(!dir.exists())
        dir.mkdirs();
        try {
            File saveFile = new File(dir, file.getOriginalFilename());
            file.transferTo(saveFile);
            return saveFile;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
