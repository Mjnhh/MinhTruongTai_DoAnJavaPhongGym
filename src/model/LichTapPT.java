package model;

import java.sql.Time;
import java.util.Date;

// Model lịch tập PT
public class LichTapPT {
    private int id;
    private String maHoiVien;
    private String maHLV;
    private Date ngayTap;
    private Time gioBatDau;
    private Time gioKetThuc;
    private String trangThai; // Đã đặt, Hoàn thành, Hủy

    public LichTapPT() {
    }

    public LichTapPT(int id, String maHoiVien, String maHLV, Date ngayTap,
                     Time gioBatDau, Time gioKetThuc, String trangThai) {
        this.id = id;
        this.maHoiVien = maHoiVien;
        this.maHLV = maHLV;
        this.ngayTap = ngayTap;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaHoiVien() {
        return maHoiVien;
    }

    public void setMaHoiVien(String maHoiVien) {
        this.maHoiVien = maHoiVien;
    }

    public String getMaHLV() {
        return maHLV;
    }

    public void setMaHLV(String maHLV) {
        this.maHLV = maHLV;
    }

    public Date getNgayTap() {
        return ngayTap;
    }

    public void setNgayTap(Date ngayTap) {
        this.ngayTap = ngayTap;
    }

    public Time getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(Time gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public Time getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(Time gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}

