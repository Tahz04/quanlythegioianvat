package dao;

import db.MySQLConnection;
import model.ThanhToan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThanhToanDAO {
    public boolean themThanhToan(ThanhToan tt, Connection conn) throws SQLException {
        String sql = "INSERT INTO thanhtoan(maDH, hinhThuc, soTien, thoiGian) VALUES (?, ?, ?, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, tt.getMaDH());
            ps.setString(2, tt.getHinhThuc());
            ps.setDouble(3, tt.getSoTien());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        tt.setMaTT(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public List<ThanhToan> getByMaDH(int maDH) {
        List<ThanhToan> list = new ArrayList<>();
        String sql = "SELECT * FROM thanhtoan WHERE maDH = ?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDH);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ThanhToan(
                    rs.getInt("maTT"),
                    rs.getInt("maDH"),
                    rs.getString("hinhThuc"),
                    rs.getDouble("soTien"),
                    rs.getTimestamp("thoiGian")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}