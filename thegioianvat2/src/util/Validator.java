package util;

import java.util.regex.Pattern;

public class Validator {
    // Biên dịch các pattern trước để tăng hiệu suất
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^(0|\\+84)(\\d{9,10})$"); // Số ĐT Việt Nam
    private static final Pattern NAME_PATTERN = 
        Pattern.compile("^[\\p{L} .'-]+$"); // Cho phép ký tự Unicode (tiếng Việt)
    private static final Pattern ADDRESS_PATTERN = 
        Pattern.compile("^[\\p{L}0-9 .,/-]+$"); // Địa chỉ với các ký tự đặc biệt cơ bản

    // Kiểm tra chuỗi rỗng/null
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    // Kiểm tra số nguyên
    public static boolean isInteger(String text) {
        if (isEmpty(text)) return false;
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Kiểm tra số thực
    public static boolean isDouble(String text) {
        if (isEmpty(text)) return false;
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Kiểm tra email hợp lệ
    public static boolean isEmail(String text) {
        return !isEmpty(text) && EMAIL_PATTERN.matcher(text).matches();
    }

    // Kiểm tra số điện thoại Việt Nam hợp lệ
    public static boolean isValidPhoneNumber(String text) {
        if (isEmpty(text)) return false;
        // Xóa tất cả khoảng trắng và dấu + nếu có
        String cleaned = text.replaceAll("\\s+", "").replace("+84", "0");
        return PHONE_PATTERN.matcher(cleaned).matches();
    }

    // Kiểm tra tên hợp lệ (cho tên món ăn/khách hàng)
    public static boolean isValidName(String name) {
        return !isEmpty(name) && 
               name.length() >= 2 && 
               name.length() <= 100 && 
               NAME_PATTERN.matcher(name).matches();
    }

    // Kiểm tra địa chỉ hợp lệ
    public static boolean isValidAddress(String address) {
        return !isEmpty(address) && 
               address.length() >= 5 && 
               address.length() <= 200 && 
               ADDRESS_PATTERN.matcher(address).matches();
    }

    // Kiểm tra giá tiền hợp lệ (lớn hơn 0)
    public static boolean isValidPrice(double price) {
        return price > 0;
    }

    // Kiểm tra số lượng hợp lệ (nguyên dương)
    public static boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }
}