package model;

import java.sql.Time;
import java.util.Date;

/**
 * Model class for ChamCong (Attendance)
 */
public class ChamCong {
    private int id;
    private String maHoiVien;
    private String tenHoiVien; // For display purpose
    private Date ngayTap;
    private Time gioVao;
    private Time gioRa;
    private String ghiChu;

    // Constructors
    public ChamCong() {
    }

    public ChamCong(int id, String maHoiVien, Date ngayTap, 
                    Time gioVao, Time gioRa, String ghiChu) {
        this.id = id;
        this.maHoiVien = maHoiVien;
        this.ngayTap = ngayTap;
        this.gioVao = gioVao;
        this.gioRa = gioRa;
        this.ghiChu = ghiChu;
    }

    // Getters and Setters
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

    public String getTenHoiVien() {
        return tenHoiVien;
    }

    public void setTenHoiVien(String tenHoiVien) {
        this.tenHoiVien = tenHoiVien;
    }

    public Date getNgayTap() {
        return ngayTap;
    }

    public void setNgayTap(Date ngayTap) {
        this.ngayTap = ngayTap;
    }

    public Time getGioVao() {
        return gioVao;
    }

    public void setGioVao(Time gioVao) {
        this.gioVao = gioVao;
    }

    public Time getGioRa() {
        return gioRa;
    }

    public void setGioRa(Time gioRa) {
        this.gioRa = gioRa;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    // Utility methods
    public boolean isCheckedOut() {
        return gioRa != null;
    }

    public long getThoiGianTap() {
        if (gioVao != null && gioRa != null) {
            return gioRa.getTime() - gioVao.getTime();
        }
        return 0;
    }

    @Override
    public String toString() {
        return maHoiVien + " - " + ngayTap;
    }
}