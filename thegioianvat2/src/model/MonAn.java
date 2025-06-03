package model;

import java.math.BigDecimal;

public class MonAn {
    private Integer maMA;
    private String tenMA;
    private Integer maDM;
    private BigDecimal donGia;
    private int soLuong;

    public MonAn() {
    }

    public MonAn(Integer maMA, String tenMA, Integer maDM, BigDecimal donGia, int soLuong) {
        this.maMA = maMA;
        this.tenMA = tenMA;
        this.maDM = maDM;
        this.donGia = donGia;
        this.soLuong = soLuong;
    }

    public Integer getMaMA() {
        return maMA;
    }

    public void setMaMA(Integer maMA) {
        this.maMA = maMA;
    }

    public String getTenMA() {
        return tenMA;
    }

    public void setTenMA(String tenMA) {
        this.tenMA = tenMA;
    }

    public Integer getMaDM() {
        return maDM;
    }

    public void setMaDM(Integer maDM) {
        this.maDM = maDM;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }


    @Override
    public String toString() {
        return tenMA;
    }
}
