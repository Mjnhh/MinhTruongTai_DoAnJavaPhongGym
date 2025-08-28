package model;

import java.math.BigDecimal;
import java.util.Date;

// Model huấn luyện viên
public class HuanLuyenVien {
    private String maHLV;
    private String tenHLV;
    private String gioiTinh;
    private Date ngaySinh;
    private String sdt;
    private String diaChi;
    private String email;
    private String chuyenMon;
    private BigDecimal luong;
    private Date ngayVaoLam;
    private boolean trangThai;

    // Khởi tạo
    public HuanLuyenVien() {
    }

    public HuanLuyenVien(String maHLV, String tenHLV, String gioiTinh, 
                        Date ngaySinh, String sdt, String diaChi, String email,
                        String chuyenMon, BigDecimal luong, Date ngayVaoLam, boolean trangThai) {
        this.maHLV = maHLV;
        this.tenHLV = tenHLV;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.email = email;
        this.chuyenMon = chuyenMon;
        this.luong = luong;
        this.ngayVaoLam = ngayVaoLam;
        this.trangThai = trangThai;
    }

    // Getter/Setter
    public String getMaHLV() {
        return maHLV;
    }

    public void setMaHLV(String maHLV) {
        this.maHLV = maHLV;
    }

    public String getTenHLV() {
        return tenHLV;
    }

    public void setTenHLV(String tenHLV) {
        this.tenHLV = tenHLV;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChuyenMon() {
        return chuyenMon;
    }

    public void setChuyenMon(String chuyenMon) {
        this.chuyenMon = chuyenMon;
    }

    public BigDecimal getLuong() {
        return luong;
    }

    public void setLuong(BigDecimal luong) {
        this.luong = luong;
    }

    public Date getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(Date ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    // Tiện ích
    public String getTrangThaiText() {
        return trangThai ? "Đang làm việc" : "Nghỉ việc";
    }

    @Override
    public String toString() {
        return maHLV + " - " + tenHLV;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HuanLuyenVien hlv = (HuanLuyenVien) obj;
        return maHLV != null ? maHLV.equals(hlv.maHLV) : hlv.maHLV == null;
    }

    @Override
    public int hashCode() {
        return maHLV != null ? maHLV.hashCode() : 0;
    }
}