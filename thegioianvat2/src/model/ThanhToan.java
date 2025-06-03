package model;

import java.util.Date;

public class ThanhToan {
    private int maTT;
    private int maDH;
    private String hinhThuc;
    private double soTien;
    private Date thoiGian;

    public ThanhToan(int maTT, int maDH, String hinhThuc, double soTien, Date thoiGian) {
        this.maTT = maTT;
        this.maDH = maDH;
        this.hinhThuc = hinhThuc;
        this.soTien = soTien;
        this.thoiGian = thoiGian;
    }

    public ThanhToan(int maDH, String hinhThuc, double soTien) {
        this(0, maDH, hinhThuc, soTien, null);
    }

    // Getters & Setters
    public int getMaTT() { return maTT; }
    public void setMaTT(int maTT) { this.maTT = maTT; }

    public int getMaDH() { return maDH; }
    public void setMaDH(int maDH) { this.maDH = maDH; }

    public String getHinhThuc() { return hinhThuc; }
    public void setHinhThuc(String hinhThuc) { this.hinhThuc = hinhThuc; }

    public double getSoTien() { return soTien; }
    public void setSoTien(double soTien) { this.soTien = soTien; }

    public Date getThoiGian() { return thoiGian; }
    public void setThoiGian(Date thoiGian) { this.thoiGian = thoiGian; }
}
