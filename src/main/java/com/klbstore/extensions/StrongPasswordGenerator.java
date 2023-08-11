package com.klbstore.extensions;


import java.security.SecureRandom;
import java.util.Base64;

public class StrongPasswordGenerator {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_-+=[]{}|;:,.<>?";

    public static String generateStrongPassword(int length) {
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();

        // Thêm ký tự in hoa vào mật khẩu
        password.append(UPPER.charAt(random.nextInt(UPPER.length())));

        // Thêm ký tự thường vào mật khẩu
        password.append(LOWER.charAt(random.nextInt(LOWER.length())));

        // Thêm số vào mật khẩu
        password.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));

        // Thêm ký tự đặc biệt vào mật khẩu
        password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        // Thêm các ký tự ngẫu nhiên cho đến khi độ dài của mật khẩu đạt được yêu cầu
        for (int i = 4; i < length; i++) {
            String characterSet = UPPER + LOWER + NUMBERS + SPECIAL_CHARACTERS;
            password.append(characterSet.charAt(random.nextInt(characterSet.length())));
        }

        // Đảo ngược chuỗi mật khẩu để tránh việc các ký tự đặc biệt bị mã hóa
        password.reverse();

        // Trả về chuỗi mật khẩu đã được mã hóa Base64
        return Base64.getEncoder().encodeToString(password.toString().getBytes());
    }

}
