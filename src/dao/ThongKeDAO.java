package dao;

import util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DAO cho thống kê và stored procedures
public class ThongKeDAO {

    // Thống kê tổng quan hội viên (SP)
    public Object[] thongKeHoiVien() {
        String call = "EXEC sp_ThongKeHoiVien";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(call);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new Object[]{
                    rs.getInt("TongHoiVien"),
                    rs.getInt("HoiVienHoatDong"),
                    rs.getInt("HoiVienHetHan")
                };
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi gọi sp_ThongKeHoiVien: " + e.getMessage());
        }
        return new Object[]{0,0,0};
    }

    // Doanh thu theo tháng (SP)
    public Object[] doanhThuTheoThang(int thang, int nam) {
        String call = "EXEC sp_DoanhThuTheoThang ?, ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(call)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                        rs.getBigDecimal("TongDoanhThu"),
                        rs.getInt("SoGiaoDich")
                    };
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi gọi sp_DoanhThuTheoThang: " + e.getMessage());
        }
        return new Object[]{BigDecimal.ZERO, 0};
    }

    // Gói tập đăng ký nhiều nhất (TOP N)
    public List<Object[]> goiTapDangKyNhieuNhat(int limit) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT TOP (?) MaGoiTap, COUNT(*) AS LuotDangKy FROM HoiVien " +
                     "WHERE MaGoiTap IS NOT NULL GROUP BY MaGoiTap ORDER BY LuotDangKy DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getString("MaGoiTap"),
                        rs.getInt("LuotDangKy")
                    });
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thống kê gói tập phổ biến: " + e.getMessage());
        }
        return list;
    }
}


