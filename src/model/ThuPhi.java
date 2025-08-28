package model;

import java.math.BigDecimal;
import java.util.Date;

// Model thu phí/hóa đơn
public class ThuPhi {
    private String maPhieu;
    private String maHoiVien;
    private String tenHoiVien; // For display purpose
    private String loaiPhi;
    private BigDecimal soTien;
    private Date ngayThu;
    private String phuongThucTT;
    private String nguoiThu;
    private String ghiChu;

    // Khởi tạo
    public ThuPhi() {
    }

    public ThuPhi(String maPhieu, String maHoiVien, String loaiPhi, 
                  BigDecimal soTien, Date ngayThu, String phuongThucTT,
                  String nguoiThu, String ghiChu) {
        this.maPhieu = maPhieu;
        this.maHoiVien = maHoiVien;
        this.loaiPhi = loaiPhi;
        this.soTien = soTien;
        this.ngayThu = ngayThu;
        this.phuongThucTT = phuongThucTT;
        this.nguoiThu = nguoiThu;
        this.ghiChu = ghiChu;
    }

    // Getter/Setter
    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getMaHoiVien() {
        return maHoiVien;
    }

    public void setMaHoiVien(String maHoiVien) {
        this.maHoiVien = maHoiVien;
    }

    public String getTenHoiVien() {
        return tenHoiVien;
    }

    public void setTenHoiVien(String tenHoiVien) {
        this.tenHoiVien = tenHoiVien;
    }

    public String getLoaiPhi() {
        return loaiPhi;
    }

    public void setLoaiPhi(String loaiPhi) {
        this.loaiPhi = loaiPhi;
    }

    public BigDecimal getSoTien() {
        return soTien;
    }

    public void setSoTien(BigDecimal soTien) {
        this.soTien = soTien;
    }

    public Date getNgayThu() {
        return ngayThu;
    }

    public void setNgayThu(Date ngayThu) {
        this.ngayThu = ngayThu;
    }

    public String getPhuongThucTT() {
        return phuongThucTT;
    }

    public void setPhuongThucTT(String phuongThucTT) {
        this.phuongThucTT = phuongThucTT;
    }

    public String getNguoiThu() {
        return nguoiThu;
    }

    public void setNguoiThu(String nguoiThu) {
        this.nguoiThu = nguoiThu;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public String toString() {
        return maPhieu + " - " + soTien + " VNĐ";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ThuPhi thuPhi = (ThuPhi) obj;
        return maPhieu != null ? maPhieu.equals(thuPhi.maPhieu) : thuPhi.maPhieu == null;
    }

    @Override
    public int hashCode() {
        return maPhieu != null ? maPhieu.hashCode() : 0;
    }
}