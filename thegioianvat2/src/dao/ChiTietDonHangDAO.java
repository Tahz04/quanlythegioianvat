package dao;

import db.MySQLConnection;
import model.ChiTietDonHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietDonHangDAO {
    public List<ChiTietDonHang> getByDonHang(int maDH) {
        List<ChiTietDonHang> list = new ArrayList<>();
        String sql = "SELECT * FROM chitietdonhang WHERE maDH=?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDH);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ChiTietDonHang(
                    rs.getInt("maDH"),
                    rs.getInt("maMA"),
                    rs.getInt("soLuong")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(ChiTietDonHang ct, Connection conn) throws SQLException {
        String sql = "INSERT INTO chitietdonhang (maDH, maMA, soLuong) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getMaDH());
            ps.setInt(2, ct.getMaMA());
            ps.setInt(3, ct.getSoLuong());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteByMaDH(int maDH, Connection conn) throws SQLException {
        String sql = "DELETE FROM chitietdonhang WHERE maDH=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDH);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteByMaDH(int maDH) throws SQLException {
        try (Connection conn = MySQLConnection.getConnection()) {
            return deleteByMaDH(maDH, conn);
        }
    }

    public boolean insert(ChiTietDonHang ct) throws SQLException {
        try (Connection conn = MySQLConnection.getConnection()) {
            return insert(ct, conn);
        }
    }
}