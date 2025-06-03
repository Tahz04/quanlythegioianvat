package controller;

import dao.DonHangDAO;
import model.DonHang;

import java.util.List;
import java.util.Map;

public class ThongKeController {
    private DonHangDAO donHangDAO;

    public ThongKeController() {
        donHangDAO = new DonHangDAO();
    }

    public double tinhTongDoanhThu(List<DonHang> dsDonHang) {
        double tong = 0;
        if (dsDonHang != null) {
            for (DonHang dh : dsDonHang) {
                tong += dh.getTongTien();
            }
        }
        return tong;
    }

    public List<Map<String, Object>> getDoanhThuTheoThang(int year) {
        try {
            return donHangDAO.getDoanhThuTheoThang(year);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy dữ liệu thống kê theo tháng: " + e.getMessage());
        }
    }
}