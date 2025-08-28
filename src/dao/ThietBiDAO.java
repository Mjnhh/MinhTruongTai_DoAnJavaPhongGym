package dao;

import model.ThietBi;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DAO cho bảng Thiết Bị
public class ThietBiDAO {
    
    // Lấy tất cả thiết bị
    public List<ThietBi> getAll() {
        List<ThietBi> list = new ArrayList<>();
        String sql = "SELECT * FROM ThietBi ORDER BY MaThietBi";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                ThietBi thietBi = createThietBiFromResultSet(rs);
                list.add(thietBi);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách thiết bị: " + e.getMessage());
        }
        
        return list;
    }
    
    // Tìm thiết bị theo mã
    public ThietBi getById(String maThietBi) {
        String sql = "SELECT * FROM ThietBi WHERE MaThietBi = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maThietBi);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createThietBiFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm thiết bị: " + e.getMessage());
        }
        
        return null;
    }
    
    // Thêm thiết bị mới
    public boolean insert(ThietBi thietBi) {
        String sql = "INSERT INTO ThietBi (MaThietBi, TenThietBi, LoaiThietBi, TinhTrang, " +
                     "NgayMua, GiaMua, NgayBaoTriCuoi, NgayBaoTriTiep, GhiChu) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, thietBi.getMaThietBi());
            ps.setString(2, thietBi.getTenThietBi());
            ps.setString(3, thietBi.getLoaiThietBi());
            ps.setString(4, thietBi.getTinhTrang());
            ps.setDate(5, thietBi.getNgayMua() != null ? new java.sql.Date(thietBi.getNgayMua().getTime()) : null);
            ps.setBigDecimal(6, thietBi.getGiaMua());
            ps.setDate(7, thietBi.getNgayBaoTriCuoi() != null ? new java.sql.Date(thietBi.getNgayBaoTriCuoi().getTime()) : null);
            ps.setDate(8, thietBi.getNgayBaoTriTiep() != null ? new java.sql.Date(thietBi.getNgayBaoTriTiep().getTime()) : null);
            ps.setString(9, thietBi.getGhiChu());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm thiết bị: " + e.getMessage());
            return false;
        }
    }
    
    // Cập nhật thiết bị
    public boolean update(ThietBi thietBi) {
        String sql = "UPDATE ThietBi SET TenThietBi = ?, LoaiThietBi = ?, TinhTrang = ?, " +
                     "NgayMua = ?, GiaMua = ?, NgayBaoTriCuoi = ?, NgayBaoTriTiep = ?, " +
                     "GhiChu = ? WHERE MaThietBi = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, thietBi.getTenThietBi());
            ps.setString(2, thietBi.getLoaiThietBi());
            ps.setString(3, thietBi.getTinhTrang());
            ps.setDate(4, thietBi.getNgayMua() != null ? new java.sql.Date(thietBi.getNgayMua().getTime()) : null);
            ps.setBigDecimal(5, thietBi.getGiaMua());
            ps.setDate(6, thietBi.getNgayBaoTriCuoi() != null ? new java.sql.Date(thietBi.getNgayBaoTriCuoi().getTime()) : null);
            ps.setDate(7, thietBi.getNgayBaoTriTiep() != null ? new java.sql.Date(thietBi.getNgayBaoTriTiep().getTime()) : null);
            ps.setString(8, thietBi.getGhiChu());
            ps.setString(9, thietBi.getMaThietBi());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật thiết bị: " + e.getMessage());
            return false;
        }
    }
    
    // Xóa thiết bị
    public boolean delete(String maThietBi) {
        String sql = "DELETE FROM ThietBi WHERE MaThietBi = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maThietBi);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa thiết bị: " + e.getMessage());
            return false;
        }
    }
    
    // Tìm kiếm thiết bị
    public List<ThietBi> search(String keyword) {
        List<ThietBi> list = new ArrayList<>();
        String sql = "SELECT * FROM ThietBi WHERE MaThietBi LIKE ? OR TenThietBi LIKE ? " +
                     "OR LoaiThietBi LIKE ? ORDER BY MaThietBi";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ThietBi thietBi = createThietBiFromResultSet(rs);
                list.add(thietBi);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm thiết bị: " + e.getMessage());
        }
        
        return list;
    }
    
    // Lấy thiết bị theo tình trạng
    public List<ThietBi> getByStatus(String tinhTrang) {
        List<ThietBi> list = new ArrayList<>();
        String sql = "SELECT * FROM ThietBi WHERE TinhTrang = ? ORDER BY MaThietBi";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tinhTrang);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ThietBi thietBi = createThietBiFromResultSet(rs);
                list.add(thietBi);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thiết bị theo tình trạng: " + e.getMessage());
        }
        
        return list;
    }
    
    // Lấy thiết bị cần bảo trì
    public List<ThietBi> getNeedMaintenance() {
        List<ThietBi> list = new ArrayList<>();
        String sql = "SELECT * FROM ThietBi WHERE NgayBaoTriTiep <= GETDATE() " +
                     "AND TinhTrang != N'Bảo trì' ORDER BY NgayBaoTriTiep";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                ThietBi thietBi = createThietBiFromResultSet(rs);
                list.add(thietBi);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thiết bị cần bảo trì: " + e.getMessage());
        }
        
        return list;
    }
    
    // Cập nhật tình trạng
    public boolean updateStatus(String maThietBi, String tinhTrangMoi) {
        String sql = "UPDATE ThietBi SET TinhTrang = ? WHERE MaThietBi = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tinhTrangMoi);
            ps.setString(2, maThietBi);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật tình trạng thiết bị: " + e.getMessage());
            return false;
        }
    }
    
    // Cập nhật lịch bảo trì
    public boolean updateMaintenance(String maThietBi, java.util.Date ngayBaoTri, java.util.Date ngayBaoTriTiep) {
        String sql = "UPDATE ThietBi SET NgayBaoTriCuoi = ?, NgayBaoTriTiep = ?, TinhTrang = N'Tốt' " +
                     "WHERE MaThietBi = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, new java.sql.Date(ngayBaoTri.getTime()));
            ps.setDate(2, new java.sql.Date(ngayBaoTriTiep.getTime()));
            ps.setString(3, maThietBi);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật lịch bảo trì: " + e.getMessage());
            return false;
        }
    }
    
    // Sinh mã thiết bị kế tiếp dạng TB###
    public String getNextId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaThietBi, 3, 3) AS INT)) FROM ThietBi";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return String.format("TB%03d", maxId + 1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy mã tiếp theo: " + e.getMessage());
        }
        
        return "TB001";
    }
    
    // Kiểm tra thiết bị tồn tại
    public boolean exists(String maThietBi) {
        String sql = "SELECT COUNT(*) FROM ThietBi WHERE MaThietBi = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maThietBi);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra thiết bị: " + e.getMessage());
        }
        
        return false;
    }
    
    // Mapping ResultSet -> ThietBi
    private ThietBi createThietBiFromResultSet(ResultSet rs) throws SQLException {
        ThietBi thietBi = new ThietBi();
        thietBi.setMaThietBi(rs.getString("MaThietBi"));
        thietBi.setTenThietBi(rs.getString("TenThietBi"));
        thietBi.setLoaiThietBi(rs.getString("LoaiThietBi"));
        thietBi.setTinhTrang(rs.getString("TinhTrang"));
        thietBi.setNgayMua(rs.getDate("NgayMua"));
        thietBi.setGiaMua(rs.getBigDecimal("GiaMua"));
        thietBi.setNgayBaoTriCuoi(rs.getDate("NgayBaoTriCuoi"));
        thietBi.setNgayBaoTriTiep(rs.getDate("NgayBaoTriTiep"));
        thietBi.setGhiChu(rs.getString("GhiChu"));
        
        return thietBi;
    }
    
    // Đếm thiết bị theo tình trạng
    public int countByStatus(String tinhTrang) {
        String sql = "SELECT COUNT(*) FROM ThietBi WHERE TinhTrang = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tinhTrang);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đếm thiết bị: " + e.getMessage());
        }
        
        return 0;
    }
}