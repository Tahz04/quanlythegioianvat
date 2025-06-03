package dao;

import db.MySQLConnection;
import model.MonAn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonAnDAO {

    public List<MonAn> getAll() {
        List<MonAn> list = new ArrayList<>();
        String sql = "SELECT * FROM monan";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MonAn monAn = new MonAn();
                monAn.setMaMA(rs.getInt("maMA"));
                monAn.setTenMA(rs.getString("tenMA"));
                monAn.setMaDM(rs.getInt("maDM"));
                monAn.setDonGia(rs.getBigDecimal("donGia"));
                monAn.setSoLuong(rs.getInt("soLuong"));
                list.add(monAn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public MonAn getById(int maMA) {
        String sql = "SELECT * FROM monan WHERE maMA = ?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maMA);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MonAn monAn = new MonAn();
                    monAn.setMaMA(rs.getInt("maMA"));
                    monAn.setTenMA(rs.getString("tenMA"));
                    monAn.setMaDM(rs.getInt("maDM"));
                    monAn.setDonGia(rs.getBigDecimal("donGia"));
                    monAn.setSoLuong(rs.getInt("soLuong"));
                    return monAn;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(MonAn monAn) {
        String sql = "INSERT INTO monan (tenMA, maDM, donGia, soLuong) VALUES (?, ?, ?, ?)"; 
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { 

            ps.setString(1, monAn.getTenMA());
            if (monAn.getMaDM() != null) {
                ps.setInt(2, monAn.getMaDM());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setBigDecimal(3, monAn.getDonGia());
            ps.setInt(4, monAn.getSoLuong());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        monAn.setMaMA(rs.getInt(1)); // Lấy ID tự sinh
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean update(MonAn monAn) {
        String sql = "UPDATE monan SET tenMA = ?, maDM = ?, donGia = ?, soLuong = ? WHERE maMA = ?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, monAn.getTenMA());
            if (monAn.getMaDM() != null) {
                ps.setInt(2, monAn.getMaDM());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setBigDecimal(3, monAn.getDonGia());
            ps.setInt(4, monAn.getSoLuong());
            ps.setInt(5, monAn.getMaMA());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maMA) {
        String sql = "DELETE FROM monan WHERE maMA = ?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maMA);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getTenDanhMucById(Integer maDM) {
        if (maDM == null) return "";
        String sql = "SELECT tenDM FROM danhmuc WHERE maDM = ?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maDM);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tenDM");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getTenMonAnById(int maMA) {
        MonAn monAn = getById(maMA);
        return monAn != null ? monAn.getTenMA() : "";
    }
}