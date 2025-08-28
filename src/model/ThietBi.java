package model;

import java.math.BigDecimal;
import java.util.Date;

// Model thiết bị
public class ThietBi {
    private String maThietBi;
    private String tenThietBi;
    private String loaiThietBi;
    private String tinhTrang;
    private Date ngayMua;
    private BigDecimal giaMua;
    private Date ngayBaoTriCuoi;
    private Date ngayBaoTriTiep;
    private String ghiChu;

    // Khởi tạo
    public ThietBi() {
    }

    public ThietBi(String maThietBi, String tenThietBi, String loaiThietBi, 
                   String tinhTrang, Date ngayMua, BigDecimal giaMua,
                   Date ngayBaoTriCuoi, Date ngayBaoTriTiep, String ghiChu) {
        this.maThietBi = maThietBi;
        this.tenThietBi = tenThietBi;
        this.loaiThietBi = loaiThietBi;
        this.tinhTrang = tinhTrang;
        this.ngayMua = ngayMua;
        this.giaMua = giaMua;
        this.ngayBaoTriCuoi = ngayBaoTriCuoi;
        this.ngayBaoTriTiep = ngayBaoTriTiep;
        this.ghiChu = ghiChu;
    }

    // Getter/Setter
    public String getMaThietBi() {
        return maThietBi;
    }

    public void setMaThietBi(String maThietBi) {
        this.maThietBi = maThietBi;
    }

    public String getTenThietBi() {
        return tenThietBi;
    }

    public void setTenThietBi(String tenThietBi) {
        this.tenThietBi = tenThietBi;
    }

    public String getLoaiThietBi() {
        return loaiThietBi;
    }

    public void setLoaiThietBi(String loaiThietBi) {
        this.loaiThietBi = loaiThietBi;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public Date getNgayMua() {
        return ngayMua;
    }

    public void setNgayMua(Date ngayMua) {
        this.ngayMua = ngayMua;
    }

    public BigDecimal getGiaMua() {
        return giaMua;
    }

    public void setGiaMua(BigDecimal giaMua) {
        this.giaMua = giaMua;
    }

    public Date getNgayBaoTriCuoi() {
        return ngayBaoTriCuoi;
    }

    public void setNgayBaoTriCuoi(Date ngayBaoTriCuoi) {
        this.ngayBaoTriCuoi = ngayBaoTriCuoi;
    }

    public Date getNgayBaoTriTiep() {
        return ngayBaoTriTiep;
    }

    public void setNgayBaoTriTiep(Date ngayBaoTriTiep) {
        this.ngayBaoTriTiep = ngayBaoTriTiep;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    // Tiện ích
    public boolean canBaoTri() {
        return "Tốt".equals(tinhTrang) || "Hỏng".equals(tinhTrang);
    }

    public boolean isNeedMaintenance() {
        if (ngayBaoTriTiep == null) return false;
        Date today = new Date();
        return today.compareTo(ngayBaoTriTiep) >= 0;
    }

    @Override
    public String toString() {
        return maThietBi + " - " + tenThietBi;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ThietBi thietBi = (ThietBi) obj;
        return maThietBi != null ? maThietBi.equals(thietBi.maThietBi) : thietBi.maThietBi == null;
    }

    @Override
    public int hashCode() {
        return maThietBi != null ? maThietBi.hashCode() : 0;
    }
}