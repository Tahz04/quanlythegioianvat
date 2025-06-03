package dao;

import db.MySQLConnection;
import model.DonHang;
import java.sql.*;
import java.util.*;
import model.ChiTietDonHang;

public class DonHangDAO {
    public List<DonHang> getAll() {
        List<DonHang> list = new ArrayList<>();
        String sql = "SELECT * FROM donhang";
        try (Connection conn = MySQLConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                DonHang dh = new DonHang(
                    rs.getInt("maDH"),
                    rs.getInt("maKH"),
                    rs.getDate("ngayLap"),
                    rs.getDouble("tongTien"),
                    rs.getString("ghiChu")
                );
                list.add(dh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<Map<String, Object>> getDoanhThuTheoThang(int year) {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT MONTH(ngayLap) as thang, SUM(tongTien) as doanhThu " +
                     "FROM donhang " +
                     "WHERE YEAR(ngayLap) = ? " +
                     "GROUP BY MONTH(ngayLap) " +
                     "ORDER BY thang";
        
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, year);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("thang", rs.getInt("thang"));
                    row.put("doanhThu", rs.getDouble("doanhThu"));
                    result.add(row);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn doanh thu theo tháng: " + e.getMessage());
        }
        return result;
    }

    public List<DonHang> search(String keyword) {
        List<DonHang> list = new ArrayList<>();
        String sql = "SELECT d.* FROM donhang d " +
                     "LEFT JOIN khachhang k ON d.maKH = k.maKH " +
                     "WHERE d.maDH LIKE ? OR k.tenKH LIKE ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DonHang dh = new DonHang(
                    rs.getInt("maDH"),
                    rs.getInt("maKH"),
                    rs.getDate("ngayLap"),
                    rs.getDouble("tongTien"),
                    rs.getString("ghiChu")
                );
                list.add(dh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
        
    public boolean insert(DonHang dh, Connection conn) throws SQLException {
        String sql = "INSERT INTO donhang(maKH, ngayLap, tongTien, ghiChu) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, dh.getMaKH());
            ps.setTimestamp(2, new Timestamp(dh.getNgayDat().getTime()));
            ps.setDouble(3, dh.getTongTien());
            ps.setString(4, dh.getGhiChu());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    dh.setMaDH(rs.getInt(1));
                    return true;
                }
                return false;
            }
        }
    }

    public boolean update(DonHang dh, Connection conn) throws SQLException {
        String sql = "UPDATE donhang SET maKH=?, ngayLap=?, tongTien=?, ghiChu=? WHERE maDH=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dh.getMaKH());
            ps.setTimestamp(2, new Timestamp(dh.getNgayDat().getTime()));
            ps.setDouble(3, dh.getTongTien());
            ps.setString(4, dh.getGhiChu());
            ps.setInt(5, dh.getMaDH());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int maDH) {
        String sql = "DELETE FROM donhang WHERE maDH=?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDH);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public DonHang getById(int maDH) {
        DonHang donHang = null;
        String sql = "SELECT * FROM donhang WHERE maDH = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maDH);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    donHang = new DonHang(
                        rs.getInt("maDH"),
                        rs.getInt("maKH"),
                        rs.getDate("ngayLap"),
                        rs.getDouble("tongTien"),
                        rs.getString("ghiChu")
                    );

                    ChiTietDonHangDAO ctdhDAO = new ChiTietDonHangDAO();
                    List<ChiTietDonHang> chiTietList = ctdhDAO.getByDonHang(maDH);
                    donHang.setChiTietDonHangs(chiTietList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return donHang;
    }
    
    public boolean insert(DonHang dh) {
        try (Connection conn = MySQLConnection.getConnection()) {
            return insert(dh, conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean update(DonHang dh) {
        try (Connection conn = MySQLConnection.getConnection()) {
            return update(dh, conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}