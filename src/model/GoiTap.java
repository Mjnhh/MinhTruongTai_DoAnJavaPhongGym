package model;

import java.math.BigDecimal;

/**
 * Model class for GoiTap (Package/Plan)
 */
public class GoiTap {
    private String maGoiTap;
    private String tenGoiTap;
    private String moTa;
    private BigDecimal giaTien;
    private int thoiHan; // số tháng
    private int soBuoi; // số buổi tập trong gói
    private boolean trangThai;

    // Constructors
    public GoiTap() {
    }

    public GoiTap(String maGoiTap, String tenGoiTap, String moTa, 
                  BigDecimal giaTien, int thoiHan, int soBuoi, boolean trangThai) {
        this.maGoiTap = maGoiTap;
        this.tenGoiTap = tenGoiTap;
        this.moTa = moTa;
        this.giaTien = giaTien;
        this.thoiHan = thoiHan;
        this.soBuoi = soBuoi;
        this.trangThai = trangThai;
    }

    // Getters and Setters
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

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public BigDecimal getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(BigDecimal giaTien) {
        this.giaTien = giaTien;
    }

    public int getThoiHan() {
        return thoiHan;
    }

    public void setThoiHan(int thoiHan) {
        this.thoiHan = thoiHan;
    }

    public int getSoBuoi() {
        return soBuoi;
    }

    public void setSoBuoi(int soBuoi) {
        this.soBuoi = soBuoi;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return tenGoiTap;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GoiTap goiTap = (GoiTap) obj;
        return maGoiTap != null ? maGoiTap.equals(goiTap.maGoiTap) : goiTap.maGoiTap == null;
    }

    @Override
    public int hashCode() {
        return maGoiTap != null ? maGoiTap.hashCode() : 0;
    }
}