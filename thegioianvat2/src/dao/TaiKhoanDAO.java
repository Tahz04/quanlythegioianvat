package dao;

import model.TaiKhoan;
import java.sql.*;
import java.util.*;

public class TaiKhoanDAO {
    private Connection conn;

    public TaiKhoanDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean kiemTraDangNhap(String tenDN, String matKhau) {
        String sql = "SELECT * FROM taikhoan WHERE tenDangNhap=? AND matKhau=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenDN);
            ps.setString(2, matKhau); // TODO: nên hash trước
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String layVaiTro(String tenDN) {
        String sql = "SELECT vaiTro FROM taikhoan WHERE tenDangNhap=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenDN);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("vaiTro");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TaiKhoan> layDanhSachTaiKhoan() {
        List<TaiKhoan> ds = new ArrayList<>();
        String sql = "SELECT * FROM taikhoan";
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                ds.add(new TaiKhoan(
                    rs.getString("tenDangNhap"),
                    rs.getString("matKhau"),
                    rs.getString("hoTen"),
                    rs.getString("vaiTro")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }


    public boolean themTaiKhoan(TaiKhoan tk) {
        String sql = "INSERT INTO taikhoan (tenDangNhap, matKhau, hoTen, vaiTro) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getTenDangNhap());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.gethoTen());  // Thêm hoTen
            ps.setString(4, tk.getVaiTro());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatTaiKhoan(TaiKhoan tk) {
        String sql = "UPDATE taikhoan SET matKhau=?, hoTen=?, vaiTro=? WHERE tenDangNhap=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getMatKhau());
            ps.setString(2, tk.gethoTen());  // Thêm hoTen
            ps.setString(3, tk.getVaiTro());
            ps.setString(4, tk.getTenDangNhap());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaTaiKhoan(String tenDN) {
        String sql = "DELETE FROM taikhoan WHERE tenDangNhap=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenDN);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String layHoTen(String tenDN) {
    String sql = "SELECT hoTen FROM taikhoan WHERE tenDangNhap=?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, tenDN);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getString("hoTen");
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
}
