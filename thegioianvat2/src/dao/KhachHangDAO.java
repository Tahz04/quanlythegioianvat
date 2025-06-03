package dao;

import db.MySQLConnection;
import model.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM khachhang";

        try (Connection conn = MySQLConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                KhachHang kh = new KhachHang(
                rs.getInt("maKH"),
                rs.getString("tenKH"),
                rs.getString("soDienThoai"),
                rs.getString("diaChi"),
                rs.getObject("loaiKH") != null ? rs.getInt("loaiKH") : null
                );
                kh.setMaKH(rs.getInt("maKH"));
                kh.setTenKH(rs.getString("tenKH"));
                kh.setSoDienThoai(rs.getString("soDienThoai"));
                kh.setDiaChi(rs.getString("diaChi"));
                kh.setLoaiKH(rs.getObject("loaiKH") != null ? rs.getInt("loaiKH") : null);
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO khachhang (tenKH, soDienThoai, diaChi, loaiKH) VALUES (?, ?, ?, ?)";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kh.getTenKH());
            stmt.setString(2, kh.getSoDienThoai());
            stmt.setString(3, kh.getDiaChi());
            if (kh.getLoaiKH() != null) {
                stmt.setInt(4, kh.getLoaiKH());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(KhachHang kh) {
        String sql = "UPDATE khachhang SET tenKH=?, soDienThoai=?, diaChi=?, loaiKH=? WHERE maKH=?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kh.getTenKH());
            stmt.setString(2, kh.getSoDienThoai());
            stmt.setString(3, kh.getDiaChi());
            if (kh.getLoaiKH() != null) {
                stmt.setInt(4, kh.getLoaiKH());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setInt(5, kh.getMaKH());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean delete(int maKH) {
        String sql = "DELETE FROM khachhang WHERE maKH=?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maKH);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    
    public KhachHang findById(int maKH) {
        String sql = "SELECT * FROM khachhang WHERE maKH=?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maKH);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new KhachHang(
                    rs.getInt("maKH"),
                    rs.getString("tenKH"),
                    rs.getString("soDienThoai"),
                    rs.getString("diaChi"),
                    rs.getObject("loaiKH") != null ? rs.getInt("loaiKH") : null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
        public String getTenKHById(int maKH) {
        String sql = "SELECT tenKH FROM khachhang WHERE maKH = ?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maKH);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("tenKH");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Không xác định";
    }
}
