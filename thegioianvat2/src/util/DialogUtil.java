package util;

import javax.swing.*;
import java.awt.*;
import ui.panels.QuanLyMonAnPanel;

public class DialogUtil {

    // Hiển thị hộp thoại thông báo thành công
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public static void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean showConfirm(String message) {
        return JOptionPane.showConfirmDialog(null, message, "Xác nhận", 
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    // Hiển thị hộp thoại cảnh báo lỗi
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    // Hiển thị hộp thoại cảnh báo với nút OK/Cancel, trả về true nếu người dùng chọn OK
    public static boolean showConfirm(Component parent, String message) {
        int option = JOptionPane.showConfirmDialog(parent, message, "Xác nhận", JOptionPane.OK_CANCEL_OPTION);
        return option == JOptionPane.OK_OPTION;
    }

    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
