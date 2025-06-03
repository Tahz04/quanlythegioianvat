package controller;

import dao.ChiTietDonHangDAO;
import dao.DonHangDAO;
import dao.KhachHangDAO;
import dao.MonAnDAO;
import dao.ThanhToanDAO;
import db.MySQLConnection;
import model.ChiTietDonHang;
import model.DonHang;
import model.MonAn;
import model.ThanhToan;
import model.KhachHang;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class DonHangController {
    private DonHangDAO dhDAO = new DonHangDAO();
    private ChiTietDonHangDAO ctDAO = new ChiTietDonHangDAO();
    private MonAnDAO monAnDAO = new MonAnDAO();
    private ThanhToanDAO thanhToanDAO = new ThanhToanDAO();

    public List<DonHang> getAll() {
        return dhDAO.getAll();
    }

    public DonHang getById(int maDH) {
        DonHang donHang = dhDAO.getById(maDH);
        if (donHang != null) {
            donHang.setChiTietDonHangs(ctDAO.getByDonHang(maDH));
        }
        return donHang;
    }

    public boolean add(DonHang dh, List<ChiTietDonHang> listCT) {
        Connection conn = null;
        try {
            conn = MySQLConnection.getConnection();
            conn.setAutoCommit(false);

            if (!dhDAO.insert(dh, conn)) {
                conn.rollback();
                return false;
            }

            for (ChiTietDonHang ct : listCT) {
                ct.setMaDH(dh.getMaDH());
                if (!ctDAO.insert(ct, conn)) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { 
                    conn.setAutoCommit(true); 
                    conn.close(); 
                } catch (SQLException e) {}
            }
        }
    }

    public boolean update(DonHang dh, List<ChiTietDonHang> listCT) {
        Connection conn = null;
        try {
            conn = MySQLConnection.getConnection();
            conn.setAutoCommit(false);

            if (!dhDAO.update(dh, conn)) {
                conn.rollback();
                return false;
            }

            if (!ctDAO.deleteByMaDH(dh.getMaDH(), conn)) {
                conn.rollback();
                return false;
            }

            for (ChiTietDonHang ct : listCT) {
                ct.setMaDH(dh.getMaDH());
                if (!ctDAO.insert(ct, conn)) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { 
                    conn.setAutoCommit(true); 
                    conn.close(); 
                } catch (SQLException e) {}
            }
        }
    }

    public boolean delete(int maDH) {
        Connection conn = null;
        try {
            conn = MySQLConnection.getConnection();
            conn.setAutoCommit(false);

            if (!ctDAO.deleteByMaDH(maDH, conn)) {
                conn.rollback();
                return false;
            }

            if (!dhDAO.delete(maDH)) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { 
                    conn.setAutoCommit(true); 
                    conn.close(); 
                } catch (SQLException e) {}
            }
        }
    }

    
    
    public List<ChiTietDonHang> getChiTiet(int maDH) {
        return ctDAO.getByDonHang(maDH);
    }

    public List<DonHang> searchDonHang(String keyword) {
        return dhDAO.search(keyword);
    }

    public String getTenKhachHang(int maKH) {
        KhachHang kh = new KhachHangDAO().findById(maKH);
        return kh != null ? kh.getTenKH() : "Không xác định";
    }

    public double tinhTongTien(List<ChiTietDonHang> chiTietList, int maKH) {
        double tongTien = 0;
        
        // Tính tổng tiền từ chi tiết đơn hàng
        for (ChiTietDonHang ct : chiTietList) {
            MonAn monAn = monAnDAO.getById(ct.getMaMA());
            if (monAn != null) {
                tongTien += ct.getSoLuong() * monAn.getDonGia().doubleValue();
            }
        }
        
        // Áp dụng giảm giá theo loại khách hàng
        KhachHang kh = new KhachHangDAO().findById(maKH);
        if (kh != null && kh.getLoaiKH() != null) {
            switch (kh.getLoaiKH()) {
                case 1: // Khách thân thiết - giảm 5%
                    tongTien *= 0.95;
                    break;
                case 2: // Khách VIP - giảm 10%
                    tongTien *= 0.9;
                    break;
            }
        }
        
        return tongTien;
    }

    public boolean thanhToanDonHang(int maDH, String hinhThuc) {
        Connection conn = null;
        try {
            conn = MySQLConnection.getConnection();
            conn.setAutoCommit(false);

            DonHang dh = dhDAO.getById(maDH);
            if (dh == null) {
                throw new SQLException("Đơn hàng không tồn tại");
            }

            double soTien = dh.getTongTien();
            double daThanhToan = thanhToanDAO.getByMaDH(maDH).stream()
                    .mapToDouble(ThanhToan::getSoTien)
                    .sum();
                    
            if (daThanhToan >= soTien) {
                throw new SQLException("Đơn hàng đã được thanh toán đủ");
            }

            ThanhToan tt = new ThanhToan(maDH, hinhThuc, soTien - daThanhToan);
            if (!thanhToanDAO.themThanhToan(tt, conn)) {
                throw new SQLException("Lỗi khi thêm thanh toán");
            }

            // Thông báo giảm giá nếu có
            KhachHang kh = new KhachHangDAO().findById(dh.getMaKH());
            if (kh != null && kh.getLoaiKH() != null) {
                String loaiKH = kh.getLoaiKH() == 1 ? "thân thiết (5%)" : "VIP (10%)";
                JOptionPane.showMessageDialog(null, 
                    "Khách hàng " + kh.getTenKH() + " là khách " + loaiKH + 
                    "\nĐã được áp dụng giảm giá trong tổng tiền",
                    "Thông tin giảm giá", 
                    JOptionPane.INFORMATION_MESSAGE);
            }

            // Cập nhật ghi chú
            dh.setGhiChu("Đã thanh toán");
            if (!dhDAO.update(dh, conn)) {
                throw new SQLException("Lỗi khi cập nhật ghi chú");
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { 
                    conn.setAutoCommit(true); 
                    conn.close(); 
                } catch (SQLException e) {}
            }
        }
    }
    

    public double getSoTien(int maDH) {
        DonHang dh = dhDAO.getById(maDH);
        if (dh == null) return 0;
        
        double daThanhToan = thanhToanDAO.getByMaDH(maDH).stream()
                .mapToDouble(ThanhToan::getSoTien)
                .sum();
        
        return dh.getTongTien() - daThanhToan;
    }
    
}