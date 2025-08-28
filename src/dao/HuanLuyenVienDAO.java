package dao;

import model.HuanLuyenVien;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DAO cho bảng Huấn Luyện Viên
public class HuanLuyenVienDAO {
    
    // Lấy tất cả HLV
    public List<HuanLuyenVien> getAll() {
        List<HuanLuyenVien> list = new ArrayList<>();
        String sql = "SELECT * FROM HuanLuyenVien ORDER BY MaHLV";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                HuanLuyenVien hlv = createHLVFromResultSet(rs);
                list.add(hlv);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách HLV: " + e.getMessage());
        }
        
        return list;
    }
    
    // Lấy HLV đang làm việc (TrangThai = 1)
    public List<HuanLuyenVien> getActive() {
        List<HuanLuyenVien> list = new ArrayList<>();
        String sql = "SELECT * FROM HuanLuyenVien WHERE TrangThai = 1 ORDER BY MaHLV";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                HuanLuyenVien hlv = createHLVFromResultSet(rs);
                list.add(hlv);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy HLV hoạt động: " + e.getMessage());
        }
        
        return list;
    }
    
    // Tìm HLV theo mã
    public HuanLuyenVien getById(String maHLV) {
        String sql = "SELECT * FROM HuanLuyenVien WHERE MaHLV = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHLV);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createHLVFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm HLV: " + e.getMessage());
        }
        
        return null;
    }
    
    // Thêm HLV mới
    public boolean insert(HuanLuyenVien hlv) {
        String sql = "INSERT INTO HuanLuyenVien (MaHLV, TenHLV, GioiTinh, NgaySinh, SDT, " +
                     "DiaChi, Email, ChuyenMon, Luong, NgayVaoLam, TrangThai) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, hlv.getMaHLV());
            ps.setString(2, hlv.getTenHLV());
            ps.setString(3, hlv.getGioiTinh());
            ps.setDate(4, hlv.getNgaySinh() != null ? new java.sql.Date(hlv.getNgaySinh().getTime()) : null);
            ps.setString(5, hlv.getSdt());
            ps.setString(6, hlv.getDiaChi());
            ps.setString(7, hlv.getEmail());
            ps.setString(8, hlv.getChuyenMon());
            ps.setBigDecimal(9, hlv.getLuong());
            ps.setDate(10, hlv.getNgayVaoLam() != null ? new java.sql.Date(hlv.getNgayVaoLam().getTime()) : null);
            ps.setBoolean(11, hlv.isTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm HLV: " + e.getMessage());
            return false;
        }
    }
    
    // Cập nhật HLV
    public boolean update(HuanLuyenVien hlv) {
        String sql = "UPDATE HuanLuyenVien SET TenHLV = ?, GioiTinh = ?, NgaySinh = ?, SDT = ?, " +
                     "DiaChi = ?, Email = ?, ChuyenMon = ?, Luong = ?, NgayVaoLam = ?, TrangThai = ? " +
                     "WHERE MaHLV = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, hlv.getTenHLV());
            ps.setString(2, hlv.getGioiTinh());
            ps.setDate(3, hlv.getNgaySinh() != null ? new java.sql.Date(hlv.getNgaySinh().getTime()) : null);
            ps.setString(4, hlv.getSdt());
            ps.setString(5, hlv.getDiaChi());
            ps.setString(6, hlv.getEmail());
            ps.setString(7, hlv.getChuyenMon());
            ps.setBigDecimal(8, hlv.getLuong());
            ps.setDate(9, hlv.getNgayVaoLam() != null ? new java.sql.Date(hlv.getNgayVaoLam().getTime()) : null);
            ps.setBoolean(10, hlv.isTrangThai());
            ps.setString(11, hlv.getMaHLV());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật HLV: " + e.getMessage());
            return false;
        }
    }
    
    // Xóa (mềm) HLV: TrangThai = 0
    public boolean delete(String maHLV) {
        String sql = "UPDATE HuanLuyenVien SET TrangThai = 0 WHERE MaHLV = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHLV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa HLV: " + e.getMessage());
            return false;
        }
    }
    
    // Tìm kiếm HLV theo mã/tên/SĐT/chuyên môn
    public List<HuanLuyenVien> search(String keyword) {
        List<HuanLuyenVien> list = new ArrayList<>();
        String sql = "SELECT * FROM HuanLuyenVien WHERE MaHLV LIKE ? OR TenHLV LIKE ? " +
                     "OR SDT LIKE ? OR ChuyenMon LIKE ? ORDER BY MaHLV";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HuanLuyenVien hlv = createHLVFromResultSet(rs);
                list.add(hlv);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm HLV: " + e.getMessage());
        }
        
        return list;
    }
    
    // Sinh mã HLV kế tiếp dạng HLV###
    public String getNextId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaHLV, 4, 3) AS INT)) FROM HuanLuyenVien";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return String.format("HLV%03d", maxId + 1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy mã tiếp theo: " + e.getMessage());
        }
        
        return "HLV001";
    }
    
    // Kiểm tra HLV tồn tại
    public boolean exists(String maHLV) {
        String sql = "SELECT COUNT(*) FROM HuanLuyenVien WHERE MaHLV = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHLV);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra HLV: " + e.getMessage());
        }
        
        return false;
    }
    
    // Đếm số HLV theo trạng thái
    public int countByStatus(boolean isActive) {
        String sql = "SELECT COUNT(*) FROM HuanLuyenVien WHERE TrangThai = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isActive);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đếm HLV theo trạng thái: " + e.getMessage());
        }
        return 0;
    }

    // Mapping ResultSet -> HuanLuyenVien
    private HuanLuyenVien createHLVFromResultSet(ResultSet rs) throws SQLException {
        HuanLuyenVien hlv = new HuanLuyenVien();
        hlv.setMaHLV(rs.getString("MaHLV"));
        hlv.setTenHLV(rs.getString("TenHLV"));
        hlv.setGioiTinh(rs.getString("GioiTinh"));
        hlv.setNgaySinh(rs.getDate("NgaySinh"));
        hlv.setSdt(rs.getString("SDT"));
        hlv.setDiaChi(rs.getString("DiaChi"));
        hlv.setEmail(rs.getString("Email"));
        hlv.setChuyenMon(rs.getString("ChuyenMon"));
        hlv.setLuong(rs.getBigDecimal("Luong"));
        hlv.setNgayVaoLam(rs.getDate("NgayVaoLam"));
        hlv.setTrangThai(rs.getBoolean("TrangThai"));
        return hlv;
    }
}