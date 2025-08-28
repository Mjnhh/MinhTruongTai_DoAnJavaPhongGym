package dao;

import model.LichTapPT;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// DAO cho bảng Lịch Tập PT
public class LichTapPTDAO {

    // Lấy tất cả lịch PT
    public List<LichTapPT> getAll() {
        List<LichTapPT> list = new ArrayList<>();
        String sql = "SELECT * FROM LichTapPT ORDER BY NgayTap DESC, GioBatDau DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy lịch tập PT: " + e.getMessage());
        }
        return list;
    }

    // Lấy theo ID
    public LichTapPT getById(int id) {
        String sql = "SELECT * FROM LichTapPT WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy lịch PT theo ID: " + e.getMessage());
        }
        return null;
    }

    // Thêm lịch PT
    public boolean insert(LichTapPT lt) {
        String sql = "INSERT INTO LichTapPT (MaHoiVien, MaHLV, NgayTap, GioBatDau, GioKetThuc, TrangThai) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lt.getMaHoiVien());
            ps.setString(2, lt.getMaHLV());
            ps.setDate(3, lt.getNgayTap() != null ? new java.sql.Date(lt.getNgayTap().getTime()) : null);
            ps.setTime(4, lt.getGioBatDau());
            ps.setTime(5, lt.getGioKetThuc());
            ps.setString(6, lt.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm lịch PT: " + e.getMessage());
            return false;
        }
    }

    // Cập nhật lịch PT
    public boolean update(LichTapPT lt) {
        String sql = "UPDATE LichTapPT SET MaHoiVien = ?, MaHLV = ?, NgayTap = ?, GioBatDau = ?, GioKetThuc = ?, TrangThai = ? " +
                     "WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lt.getMaHoiVien());
            ps.setString(2, lt.getMaHLV());
            ps.setDate(3, lt.getNgayTap() != null ? new java.sql.Date(lt.getNgayTap().getTime()) : null);
            ps.setTime(4, lt.getGioBatDau());
            ps.setTime(5, lt.getGioKetThuc());
            ps.setString(6, lt.getTrangThai());
            ps.setInt(7, lt.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật lịch PT: " + e.getMessage());
            return false;
        }
    }

    // Xóa lịch PT
    public boolean delete(int id) {
        String sql = "DELETE FROM LichTapPT WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa lịch PT: " + e.getMessage());
            return false;
        }
    }

    // Lấy theo HLV
    public List<LichTapPT> getByHLV(String maHLV) {
        List<LichTapPT> list = new ArrayList<>();
        String sql = "SELECT * FROM LichTapPT WHERE MaHLV = ? ORDER BY NgayTap DESC, GioBatDau DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHLV);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy lịch PT theo HLV: " + e.getMessage());
        }
        return list;
    }

    // Lấy theo hội viên
    public List<LichTapPT> getByMember(String maHoiVien) {
        List<LichTapPT> list = new ArrayList<>();
        String sql = "SELECT * FROM LichTapPT WHERE MaHoiVien = ? ORDER BY NgayTap DESC, GioBatDau DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHoiVien);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy lịch PT theo hội viên: " + e.getMessage());
        }
        return list;
    }

    // Lấy theo ngày
    public List<LichTapPT> getByDate(Date ngay) {
        List<LichTapPT> list = new ArrayList<>();
        String sql = "SELECT * FROM LichTapPT WHERE NgayTap = ? ORDER BY GioBatDau";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(ngay.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy lịch PT theo ngày: " + e.getMessage());
        }
        return list;
    }

    // Lấy theo khoảng thời gian
    public List<LichTapPT> getByDateRange(Date tuNgay, Date denNgay) {
        List<LichTapPT> list = new ArrayList<>();
        String sql = "SELECT * FROM LichTapPT WHERE NgayTap BETWEEN ? AND ? ORDER BY NgayTap DESC, GioBatDau DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
            ps.setDate(2, new java.sql.Date(denNgay.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy lịch PT theo khoảng thời gian: " + e.getMessage());
        }
        return list;
    }

    // Đếm theo trạng thái
    public int countByStatus(String trangThai) {
        String sql = "SELECT COUNT(*) FROM LichTapPT WHERE TrangThai = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Lỗi khi đếm lịch PT theo trạng thái: " + e.getMessage());
        }
        return 0;
    }

    // Mapping ResultSet -> LichTapPT
    private LichTapPT map(ResultSet rs) throws SQLException {
        LichTapPT lt = new LichTapPT();
        lt.setId(rs.getInt("ID"));
        lt.setMaHoiVien(rs.getString("MaHoiVien"));
        lt.setMaHLV(rs.getString("MaHLV"));
        lt.setNgayTap(rs.getDate("NgayTap"));
        lt.setGioBatDau(rs.getTime("GioBatDau"));
        lt.setGioKetThuc(rs.getTime("GioKetThuc"));
        lt.setTrangThai(rs.getString("TrangThai"));
        return lt;
    }
}


