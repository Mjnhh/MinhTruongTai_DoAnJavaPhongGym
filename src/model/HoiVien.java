package model;

import java.util.Date;

/**
 * Model class for HoiVien (Member)
 */
public class HoiVien {
    private String maHoiVien;
    private String tenHoiVien;
    private String gioiTinh;
    private Date ngaySinh;
    private String sdt;
    private String diaChi;
    private String email;
    private Date ngayDangKy;
    private String maGoiTap;
    private String tenGoiTap; // For display purpose
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String maHLV;
    private String tenHLV; // For display purpose
    private boolean trangThai;
    private int soBuoiConLai;

    // Constructors
    public HoiVien() {
    }

    public HoiVien(String maHoiVien, String tenHoiVien, String gioiTinh, 
                   Date ngaySinh, String sdt, String diaChi, String email,
                   Date ngayDangKy, String maGoiTap, Date ngayBatDau, 
                   Date ngayKetThuc, String maHLV, boolean trangThai, int soBuoiConLai) {
        this.maHoiVien = maHoiVien;
        this.tenHoiVien = tenHoiVien;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.email = email;
        this.ngayDangKy = ngayDangKy;
        this.maGoiTap = maGoiTap;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.maHLV = maHLV;
        this.trangThai = trangThai;
        this.soBuoiConLai = soBuoiConLai;
    }

    // Getters and Setters
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

    public Date getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(Date ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public String getMaGoiTap() {
        return maGoiTap;
    }

    public void setMaGoiTap(String maGoiTap) {
        this.maGoiTap = maGoiTap;
    }

    public String getTenGoiTap() {
        return tenGoiTap;
    }

    public void setTenGoiTap(String tenGoiTap) {
        this.tenGoiTap = tenGoiTap;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

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

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public int getSoBuoiConLai() {
        return soBuoiConLai;
    }

    public void setSoBuoiConLai(int soBuoiConLai) {
        this.soBuoiConLai = soBuoiConLai;
    }

    // Utility methods
    public String getTrangThaiText() {
        return trangThai ? "Hoạt động" : "Hết hạn";
    }

    public boolean isNearExpiry() {
        if (ngayKetThuc == null) return false;
        Date today = new Date();
        long diff = ngayKetThuc.getTime() - today.getTime();
        long days = diff / (24 * 60 * 60 * 1000);
        return days <= 7 && days >= 0; // Cảnh báo khi còn 7 ngày
    }

    @Override
    public String toString() {
        return maHoiVien + " - " + tenHoiVien;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HoiVien hoiVien = (HoiVien) obj;
        return maHoiVien != null ? maHoiVien.equals(hoiVien.maHoiVien) : hoiVien.maHoiVien == null;
    }

    @Override
    public int hashCode() {
        return maHoiVien != null ? maHoiVien.hashCode() : 0;
    }
}