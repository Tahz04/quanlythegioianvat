package ui;

import model.TaiKhoan;
import ui.panels.*;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private TaiKhoan taiKhoan;

    public MainFrame(TaiKhoan tk) {
        this.taiKhoan = tk;
        initComponents();
    }

    private void initComponents() {
        setTitle("Thế Giới Ăn Vặt - Người dùng: " + taiKhoan.gethoTen());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Tạo tabbed pane chính
        JTabbedPane tabbedPane = new JTabbedPane();

        // Thêm các tab panel tương ứng
        tabbedPane.addTab("Quản lý Món ăn", new QuanLyMonAnPanel());
        tabbedPane.addTab("Quản lý Đơn hàng", new QuanLyDonHangPanel());
        tabbedPane.addTab("Quản lý Khách hàng", new QuanLyKhachHangPanel());
        tabbedPane.addTab("Quản lý Danh mục", new QuanLyDanhMucPanel());


        // Nếu tài khoản có quyền admin mới hiển thị tab quản lý tài khoản
        if (taiKhoan.getVaiTro() != null && taiKhoan.getVaiTro().equalsIgnoreCase("admin")) {
            tabbedPane.addTab("Thống kê", new ThongKePanel());
            tabbedPane.addTab("Quản lý Tài khoản", new TaiKhoanPanel());

        }

        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
