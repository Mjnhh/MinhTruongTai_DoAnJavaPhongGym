package dao;

import model.GoiTap;
import util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
// DAO cho bảng Gói Tập
public class GoiTapDAO {
    
    // Lấy tất cả gói tập
    public List<GoiTap> getAll() {
        List<GoiTap> list = new ArrayList<>();
        String sql = "SELECT * FROM GoiTap ORDER BY MaGoiTap";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                GoiTap goiTap = new GoiTap(
                    rs.getString("MaGoiTap"),
                    rs.getString("TenGoiTap"),
                    rs.getString("MoTa"),
                    rs.getBigDecimal("GiaTien"),
                    rs.getInt("ThoiHan"),
                    rs.getInt("SoBuoi"),
                    rs.getBoolean("TrangThai")
                );
                list.add(goiTap);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách gói tập: " + e.getMessage());
        }
        
        return list;
    }
    
    // Lấy gói tập đang hoạt động
    public List<GoiTap> getActive() {
        List<GoiTap> list = new ArrayList<>();
        String sql = "SELECT * FROM GoiTap WHERE TrangThai = 1 ORDER BY MaGoiTap";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                GoiTap goiTap = new GoiTap(
                    rs.getString("MaGoiTap"),
                    rs.getString("TenGoiTap"),
                    rs.getString("MoTa"),
                    rs.getBigDecimal("GiaTien"),
                    rs.getInt("ThoiHan"),
                    rs.getInt("SoBuoi"),
                    rs.getBoolean("TrangThai")
                );
                list.add(goiTap);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy gói tập hoạt động: " + e.getMessage());
        }
        
        return list;
    }
    
    // Tìm gói tập theo mã
    public GoiTap getById(String maGoiTap) {
        String sql = "SELECT * FROM GoiTap WHERE MaGoiTap = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maGoiTap);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new GoiTap(
                    rs.getString("MaGoiTap"),
                    rs.getString("TenGoiTap"),
                    rs.getString("MoTa"),
                    rs.getBigDecimal("GiaTien"),
                    rs.getInt("ThoiHan"),
                    rs.getInt("SoBuoi"),
                    rs.getBoolean("TrangThai")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm gói tập: " + e.getMessage());
        }
        
        return null;
    }
    
    // Thêm gói tập mới
    public boolean insert(GoiTap goiTap) {
        String sql = "INSERT INTO GoiTap (MaGoiTap, TenGoiTap, MoTa, GiaTien, ThoiHan, SoBuoi, TrangThai) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, goiTap.getMaGoiTap());
            ps.setString(2, goiTap.getTenGoiTap());
            ps.setString(3, goiTap.getMoTa());
            ps.setBigDecimal(4, goiTap.getGiaTien());
            ps.setInt(5, goiTap.getThoiHan());
            ps.setInt(6, goiTap.getSoBuoi());
            ps.setBoolean(7, goiTap.isTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm gói tập: " + e.getMessage());
            return false;
        }
    }
    
    // Cập nhật gói tập
    public boolean update(GoiTap goiTap) {
        String sql = "UPDATE GoiTap SET TenGoiTap = ?, MoTa = ?, GiaTien = ?, " +
                     "ThoiHan = ?, SoBuoi = ?, TrangThai = ? WHERE MaGoiTap = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, goiTap.getTenGoiTap());
            ps.setString(2, goiTap.getMoTa());
            ps.setBigDecimal(3, goiTap.getGiaTien());
            ps.setInt(4, goiTap.getThoiHan());
            ps.setInt(5, goiTap.getSoBuoi());
            ps.setBoolean(6, goiTap.isTrangThai());
            ps.setString(7, goiTap.getMaGoiTap());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật gói tập: " + e.getMessage());
            return false;
        }
    }
    
    // Xóa (mềm) gói tập: TrangThai = 0
    public boolean delete(String maGoiTap) {
        String sql = "UPDATE GoiTap SET TrangThai = 0 WHERE MaGoiTap = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maGoiTap);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa gói tập: " + e.getMessage());
            return false;
        }
    }
    
    // Kiểm tra gói tập tồn tại
    public boolean exists(String maGoiTap) {
        String sql = "SELECT COUNT(*) FROM GoiTap WHERE MaGoiTap = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maGoiTap);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra gói tập: " + e.getMessage());
        }
        
        return false;
    }
    
    // Tìm kiếm theo tên/mô tả
    public List<GoiTap> search(String keyword) {
        List<GoiTap> list = new ArrayList<>();
        String sql = "SELECT * FROM GoiTap WHERE TenGoiTap LIKE ? OR MoTa LIKE ? ORDER BY MaGoiTap";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                GoiTap goiTap = new GoiTap(
                    rs.getString("MaGoiTap"),
                    rs.getString("TenGoiTap"),
                    rs.getString("MoTa"),
                    rs.getBigDecimal("GiaTien"),
                    rs.getInt("ThoiHan"),
                    rs.getInt("SoBuoi"),
                    rs.getBoolean("TrangThai")
                );
                list.add(goiTap);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm gói tập: " + e.getMessage());
        }
        
        return list;
    }
    
    // Sinh mã gói tập kế tiếp dạng GT###, bỏ qua các mã không theo định dạng (ví dụ: GTVIP)
    public String getNextId() {
        String sql = "SELECT MAX(TRY_CAST(SUBSTRING(MaGoiTap, 3, 3) AS INT)) FROM GoiTap WHERE MaGoiTap LIKE 'GT___'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return String.format("GT%03d", maxId + 1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy mã tiếp theo: " + e.getMessage());
        }
        
        return "GT001";
    }
}