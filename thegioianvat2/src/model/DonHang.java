package model;

import dao.KhachHangDAO;
import java.sql.Date;
import java.util.List;

public class DonHang {
    private int maDH;
    private int maKH;
    private Date ngayDat;
    private double tongTien;
    private String ghiChu;
    private List<ChiTietDonHang> chiTietDonHangs;

    public DonHang() {}


    public DonHang(int maDH, int maKH, Date ngayDat, double tongTien, String ghiChu) {
        this.maDH = maDH;
        this.maKH = maKH;
        this.ngayDat = ngayDat;
        this.tongTien = tongTien;
        this.ghiChu = ghiChu;
    }

    // Getters and Setters
    public int getMaDH() { return maDH; }
    public void setMaDH(int maDH) { this.maDH = maDH; }

    public int getMaKH() { return maKH; }
    public void setMaKH(int maKH) { this.maKH = maKH; }

    public Date getNgayDat() { return ngayDat; }
    public void setNgayDat(Date ngayDat) { this.ngayDat = ngayDat; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public List<ChiTietDonHang> getChiTietDonHangs() { return chiTietDonHangs; }
    public void setChiTietDonHangs(List<ChiTietDonHang> chiTietDonHangs) { 
        this.chiTietDonHangs = chiTietDonHangs; 
    }

    public Date getNgayLap() {
        return ngayDat;
    }
    
    public void setNgayLap(Date ngayLap) {
        this.ngayDat = ngayLap;
    }
    
    public String getTenKH() {
        return new KhachHangDAO().getTenKHById(this.maKH);
    }
}