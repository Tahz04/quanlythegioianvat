package ui.dialogs;

import model.TaiKhoan;

import javax.swing.*;
import java.awt.*;

public class TaiKhoanDialog extends JDialog {
    private JTextField txtTenDN, txtMatKhau, txtHoTen, txtVaiTro;
    private boolean saved = false;

    public TaiKhoanDialog(TaiKhoan tk) {
        setTitle(tk == null ? "Thêm tài khoản" : "Sửa tài khoản");
        setSize(300, 200);
        setLayout(new GridLayout(5, 2));  // Changed to 5 rows to accommodate all fields
        setModal(true);

        txtTenDN = new JTextField();
        txtMatKhau = new JTextField();
        txtHoTen = new JTextField();
        txtVaiTro = new JTextField();

        if (tk != null) {
            txtTenDN.setText(tk.getTenDangNhap());
            txtTenDN.setEnabled(false);
            txtMatKhau.setText(tk.getMatKhau());
            txtHoTen.setText(tk.gethoTen());  // Added this line to set full name
            txtVaiTro.setText(tk.getVaiTro());
        }

        add(new JLabel("Tên đăng nhập:"));
        add(txtTenDN);
        add(new JLabel("Mật khẩu:"));
        add(txtMatKhau);
        add(new JLabel("Họ tên:"));
        add(txtHoTen);
        add(new JLabel("Vai trò:"));
        add(txtVaiTro);

        JButton btnLuu = new JButton("Lưu");
        btnLuu.addActionListener(e -> {
            saved = true;
            setVisible(false);
        });
        add(btnLuu);

        JButton btnHuy = new JButton("Huỷ");
        btnHuy.addActionListener(e -> {
            saved = false;
            setVisible(false);
        });
        add(btnHuy);

        setLocationRelativeTo(null);
    }

    public boolean isSaved() {
        return saved;
    }

    public TaiKhoan getTaiKhoan() {
        return new TaiKhoan(
            txtTenDN.getText(), 
            txtMatKhau.getText(),
            txtHoTen.getText(), 
            txtVaiTro.getText()
        );
    }

    public void setVaiTro(String vaiTro) {
        txtVaiTro.setText(vaiTro);
    }
}