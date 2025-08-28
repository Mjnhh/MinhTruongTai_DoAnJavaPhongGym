/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.HoiVien;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoiVienDAO {
    // Lấy toàn bộ hội viên
    public List<HoiVien> getAll() {
        List<HoiVien> list = new ArrayList<>();
        String sql = "SELECT * FROM HoiVien ORDER BY MaHoiVien";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(createHoiVienFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách hội viên: " + e.getMessage());
        }
        return list;
    }

    // Lấy hội viên theo mã
    public HoiVien getById(String maHoiVien) {
        String sql = "SELECT * FROM HoiVien WHERE MaHoiVien = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHoiVien);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createHoiVienFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy hội viên theo mã: " + e.getMessage());
        }
        return null;
    }

    // Thêm hội viên mới
    public boolean insert(HoiVien hv) {
        String sql = "INSERT INTO HoiVien (MaHoiVien, TenHoiVien, GioiTinh, NgaySinh, SDT, DiaChi, Email, " +
                     "NgayDangKy, MaGoiTap, NgayBatDau, NgayKetThuc, MaHLV, TrangThai, SoBuoiConLai) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hv.getMaHoiVien());
            ps.setString(2, hv.getTenHoiVien());
            ps.setString(3, hv.getGioiTinh());
            ps.setDate(4, hv.getNgaySinh() != null ? new java.sql.Date(hv.getNgaySinh().getTime()) : null);
            ps.setString(5, hv.getSdt());
            ps.setString(6, hv.getDiaChi());
            ps.setString(7, hv.getEmail());
            ps.setDate(8, hv.getNgayDangKy() != null ? new java.sql.Date(hv.getNgayDangKy().getTime()) : null);
            ps.setString(9, hv.getMaGoiTap());
            ps.setDate(10, hv.getNgayBatDau() != null ? new java.sql.Date(hv.getNgayBatDau().getTime()) : null);
            ps.setDate(11, hv.getNgayKetThuc() != null ? new java.sql.Date(hv.getNgayKetThuc().getTime()) : null);
            ps.setString(12, hv.getMaHLV());
            ps.setBoolean(13, hv.isTrangThai());
            ps.setInt(14, hv.getSoBuoiConLai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm hội viên: " + e.getMessage());
            return false;
        }
    }

    // Cập nhật hội viên
    public boolean update(HoiVien hv) {
        String sql = "UPDATE HoiVien SET TenHoiVien = ?, GioiTinh = ?, NgaySinh = ?, SDT = ?, DiaChi = ?, Email = ?, " +
                     "NgayDangKy = ?, MaGoiTap = ?, NgayBatDau = ?, NgayKetThuc = ?, MaHLV = ?, TrangThai = ?, SoBuoiConLai = ? " +
                     "WHERE MaHoiVien = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hv.getTenHoiVien());
            ps.setString(2, hv.getGioiTinh());
            ps.setDate(3, hv.getNgaySinh() != null ? new java.sql.Date(hv.getNgaySinh().getTime()) : null);
            ps.setString(4, hv.getSdt());
            ps.setString(5, hv.getDiaChi());
            ps.setString(6, hv.getEmail());
            ps.setDate(7, hv.getNgayDangKy() != null ? new java.sql.Date(hv.getNgayDangKy().getTime()) : null);
            ps.setString(8, hv.getMaGoiTap());
            ps.setDate(9, hv.getNgayBatDau() != null ? new java.sql.Date(hv.getNgayBatDau().getTime()) : null);
            ps.setDate(10, hv.getNgayKetThuc() != null ? new java.sql.Date(hv.getNgayKetThuc().getTime()) : null);
            ps.setString(11, hv.getMaHLV());
            ps.setBoolean(12, hv.isTrangThai());
            ps.setInt(13, hv.getSoBuoiConLai());
            ps.setString(14, hv.getMaHoiVien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật hội viên: " + e.getMessage());
            return false;
        }
    }

    // Xóa (mềm) hội viên: đặt TrangThai = 0
    public boolean delete(String maHoiVien) {
        String sql = "UPDATE HoiVien SET TrangThai = 0 WHERE MaHoiVien = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHoiVien);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa hội viên: " + e.getMessage());
            return false;
        }
    }

    // Tìm kiếm hội viên theo mã/tên/SĐT
    public List<HoiVien> search(String keyword) {
        List<HoiVien> list = new ArrayList<>();
        String sql = "SELECT * FROM HoiVien WHERE MaHoiVien LIKE ? OR TenHoiVien LIKE ? OR SDT LIKE ? ORDER BY MaHoiVien";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(createHoiVienFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm hội viên: " + e.getMessage());
        }
        return list;
    }

    // Sinh mã hội viên kế tiếp dạng HV###
    public String getNextId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaHoiVien, 3, 3) AS INT)) FROM HoiVien";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return String.format("HV%03d", maxId + 1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy mã hội viên kế tiếp: " + e.getMessage());
        }
        return "HV001";
    }

    // Kiểm tra hội viên có tồn tại
    public boolean exists(String maHoiVien) {
        String sql = "SELECT COUNT(*) FROM HoiVien WHERE MaHoiVien = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHoiVien);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra tồn tại hội viên: " + e.getMessage());
        }
        return false;
    }

    // Mapping ResultSet -> HoiVien
    private HoiVien createHoiVienFromResultSet(ResultSet rs) throws SQLException {
        HoiVien hv = new HoiVien();
        hv.setMaHoiVien(rs.getString("MaHoiVien"));
        hv.setTenHoiVien(rs.getString("TenHoiVien"));
        hv.setGioiTinh(rs.getString("GioiTinh"));
        hv.setNgaySinh(rs.getDate("NgaySinh"));
        hv.setSdt(rs.getString("SDT"));
        hv.setDiaChi(rs.getString("DiaChi"));
        hv.setEmail(rs.getString("Email"));
        hv.setNgayDangKy(rs.getDate("NgayDangKy"));
        hv.setMaGoiTap(rs.getString("MaGoiTap"));
        hv.setNgayBatDau(rs.getDate("NgayBatDau"));
        hv.setNgayKetThuc(rs.getDate("NgayKetThuc"));
        hv.setMaHLV(rs.getString("MaHLV"));
        hv.setTrangThai(rs.getBoolean("TrangThai"));
        hv.setSoBuoiConLai(rs.getInt("SoBuoiConLai"));
        return hv;
    }
}




