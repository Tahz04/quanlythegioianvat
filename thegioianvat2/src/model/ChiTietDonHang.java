package model;

public class ChiTietDonHang {
    private int maDH;
    private int maMA;
    private int soLuong;

    public ChiTietDonHang() {}

    public ChiTietDonHang(int maDH, int maMA, int soLuong) {
        this.maDH = maDH;
        this.maMA = maMA;
        this.soLuong = soLuong;
    }

    // Getter và Setter
    public int getMaDH() { return maDH; }
    public void setMaDH(int maDH) { this.maDH = maDH; }

    public int getMaMA() { return maMA; }
    public void setMaMA(int maMA) { this.maMA = maMA; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
}