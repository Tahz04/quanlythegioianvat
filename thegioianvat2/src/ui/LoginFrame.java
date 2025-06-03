package ui;

import dao.TaiKhoanDAO;
import db.MySQLConnection;
import model.TaiKhoan;
import util.DialogUtil;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;

    public LoginFrame() {
        setTitle("Đăng nhập - Thế Giới Ăn Vặt");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(null);

        JLabel lblTitle = new JLabel("Đăng nhập hệ thống");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBounds(110, 10, 200, 30);
        panel.add(lblTitle);

        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setBounds(50, 60, 100, 25);
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(160, 60, 180, 25);
        panel.add(txtUsername);

        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setBounds(50, 100, 100, 25);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(160, 100, 180, 25);
        panel.add(txtPassword);

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setBounds(90, 150, 100, 30);
        panel.add(btnLogin);

        btnExit = new JButton("Thoát");
        btnExit.setBounds(210, 150, 100, 30);
        panel.add(btnExit);

        add(panel);

        btnLogin.addActionListener(e -> login());
        btnExit.addActionListener(e -> System.exit(0));
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            DialogUtil.showError(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!");
            return;
        }

        try (Connection conn = MySQLConnection.getConnection()) {
            TaiKhoanDAO dao = new TaiKhoanDAO(conn);
            if (!dao.kiemTraDangNhap(username, password)) {
                DialogUtil.showError(this, "Tên đăng nhập hoặc mật khẩu không đúng!");
                return;
            }
            String vaiTro = dao.layVaiTro(username);
            String hoTen = dao.layHoTen(username);
            TaiKhoan tk = new TaiKhoan(username, password,hoTen, vaiTro);
            DialogUtil.showSuccess(this, "Đăng nhập thành công!");
            new MainFrame(tk).setVisible(true);
            this.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtil.showError(this, "Lỗi kết nối cơ sở dữ liệu!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
