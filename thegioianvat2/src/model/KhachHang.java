package model;

public class KhachHang {
    private int maKH;
    private String tenKH;
    private String soDienThoai;
    private String diaChi;
    private Integer loaiKH;


    public KhachHang(int maKH, String tenKH, String soDienThoai, String diaChi, Integer loaiKH) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.loaiKH = loaiKH;
    }


    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
    public Integer getLoaiKH() {
        return loaiKH;
    }

    public void setLoaiKH(Integer loaiKH) {
        this.loaiKH = loaiKH;
    }


    @Override
    public String toString() {
        return tenKH;
    }
    
    public String getLoaiKHString() {
        if (loaiKH == null) return "Vãng lai";
        switch (loaiKH) {
            case 1: return "Thân thiết";
            case 2: return "VIP";
            default: return "Vãng lai";
        }
    }
        public String getThongTinGiamGia() {
        if (loaiKH == null) return "Không giảm giá";
        switch (loaiKH) {
            case 1: return "Giảm 5% (Thân thiết)";
            case 2: return "Giảm 10% (VIP)";
            default: return "Không giảm giá";
        }
    }

}
