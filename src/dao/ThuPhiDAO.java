package dao;

import model.ThuPhi;
import util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// DAO cho bảng Thu Phí
public class ThuPhiDAO {
    
    // Lấy tất cả phiếu thu
    public List<ThuPhi> getAll() {
        List<ThuPhi> list = new ArrayList<>();
        String sql = "SELECT tp.*, hv.TenHoiVien FROM ThuPhi tp " +
                     "INNER JOIN HoiVien hv ON tp.MaHoiVien = hv.MaHoiVien " +
                     "ORDER BY tp.NgayThu DESC, tp.MaPhieu DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                ThuPhi thuPhi = createThuPhiFromResultSet(rs);
                list.add(thuPhi);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách thu phí: " + e.getMessage());
        }
        
        return list;
    }
    
    // Tìm phiếu thu theo mã
    public ThuPhi getById(String maPhieu) {
        String sql = "SELECT tp.*, hv.TenHoiVien FROM ThuPhi tp " +
                     "INNER JOIN HoiVien hv ON tp.MaHoiVien = hv.MaHoiVien " +
                     "WHERE tp.MaPhieu = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maPhieu);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createThuPhiFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm phiếu thu: " + e.getMessage());
        }
        
        return null;
    }
    
    // Thêm phiếu thu mới
    public boolean insert(ThuPhi thuPhi) {
        String sql = "INSERT INTO ThuPhi (MaPhieu, MaHoiVien, LoaiPhi, SoTien, NgayThu, " +
                     "PhuongThucTT, NguoiThu, GhiChu) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, thuPhi.getMaPhieu());
            ps.setString(2, thuPhi.getMaHoiVien());
            ps.setString(3, thuPhi.getLoaiPhi());
            ps.setBigDecimal(4, thuPhi.getSoTien());
            ps.setDate(5, thuPhi.getNgayThu() != null ? new java.sql.Date(thuPhi.getNgayThu().getTime()) : null);
            ps.setString(6, thuPhi.getPhuongThucTT());
            ps.setString(7, thuPhi.getNguoiThu());
            ps.setString(8, thuPhi.getGhiChu());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm phiếu thu: " + e.getMessage());
            return false;
        }
    }
    
    // Cập nhật phiếu thu
    public boolean update(ThuPhi thuPhi) {
        String sql = "UPDATE ThuPhi SET MaHoiVien = ?, LoaiPhi = ?, SoTien = ?, NgayThu = ?, " +
                     "PhuongThucTT = ?, NguoiThu = ?, GhiChu = ? WHERE MaPhieu = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, thuPhi.getMaHoiVien());
            ps.setString(2, thuPhi.getLoaiPhi());
            ps.setBigDecimal(3, thuPhi.getSoTien());
            ps.setDate(4, thuPhi.getNgayThu() != null ? new java.sql.Date(thuPhi.getNgayThu().getTime()) : null);
            ps.setString(5, thuPhi.getPhuongThucTT());
            ps.setString(6, thuPhi.getNguoiThu());
            ps.setString(7, thuPhi.getGhiChu());
            ps.setString(8, thuPhi.getMaPhieu());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật phiếu thu: " + e.getMessage());
            return false;
        }
    }
    
    // Xóa phiếu thu
    public boolean delete(String maPhieu) {
        String sql = "DELETE FROM ThuPhi WHERE MaPhieu = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maPhieu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa phiếu thu: " + e.getMessage());
            return false;
        }
    }
    
    // Lấy lịch sử thu phí của 1 hội viên
    public List<ThuPhi> getByMember(String maHoiVien) {
        List<ThuPhi> list = new ArrayList<>();
        String sql = "SELECT tp.*, hv.TenHoiVien FROM ThuPhi tp " +
                     "INNER JOIN HoiVien hv ON tp.MaHoiVien = hv.MaHoiVien " +
                     "WHERE tp.MaHoiVien = ? ORDER BY tp.NgayThu DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHoiVien);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ThuPhi thuPhi = createThuPhiFromResultSet(rs);
                list.add(thuPhi);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy lịch sử thu phí: " + e.getMessage());
        }
        
        return list;
    }
    
    // Doanh thu theo ngày
    public BigDecimal getRevenueByDate(Date ngay) {
        String sql = "SELECT COALESCE(SUM(SoTien), 0) FROM ThuPhi WHERE CAST(NgayThu AS DATE) = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, new java.sql.Date(ngay.getTime()));
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy doanh thu theo ngày: " + e.getMessage());
        }
        
        return BigDecimal.ZERO;
    }
    
    // Doanh thu theo tháng
    public BigDecimal getRevenueByMonth(int thang, int nam) {
        String sql = "SELECT COALESCE(SUM(SoTien), 0) FROM ThuPhi " +
                     "WHERE MONTH(NgayThu) = ? AND YEAR(NgayThu) = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy doanh thu theo tháng: " + e.getMessage());
        }
        
        return BigDecimal.ZERO;
    }
    
    // Doanh thu theo khoảng thời gian
    public BigDecimal getRevenueByDateRange(Date tuNgay, Date denNgay) {
        String sql = "SELECT COALESCE(SUM(SoTien), 0) FROM ThuPhi WHERE NgayThu BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
            ps.setDate(2, new java.sql.Date(denNgay.getTime()));
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy doanh thu theo khoảng thời gian: " + e.getMessage());
        }
        
        return BigDecimal.ZERO;
    }
    
    // Danh sách thu phí theo khoảng thời gian
    public List<ThuPhi> getByDateRange(Date tuNgay, Date denNgay) {
        List<ThuPhi> list = new ArrayList<>();
        String sql = "SELECT tp.*, hv.TenHoiVien FROM ThuPhi tp " +
                     "INNER JOIN HoiVien hv ON tp.MaHoiVien = hv.MaHoiVien " +
                     "WHERE tp.NgayThu BETWEEN ? AND ? " +
                     "ORDER BY tp.NgayThu DESC, tp.MaPhieu DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
            ps.setDate(2, new java.sql.Date(denNgay.getTime()));
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ThuPhi thuPhi = createThuPhiFromResultSet(rs);
                list.add(thuPhi);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thu phí theo khoảng thời gian: " + e.getMessage());
        }
        
        return list;
    }
    
    // Thống kê doanh thu theo loại phí
    public List<Object[]> getRevenueByType(Date tuNgay, Date denNgay) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT LoaiPhi, COUNT(*) as SoGiaoDich, SUM(SoTien) as TongTien " +
                     "FROM ThuPhi WHERE NgayThu BETWEEN ? AND ? " +
                     "GROUP BY LoaiPhi ORDER BY TongTien DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
            ps.setDate(2, new java.sql.Date(denNgay.getTime()));
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("LoaiPhi"),
                    rs.getInt("SoGiaoDich"),
                    rs.getBigDecimal("TongTien")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thống kê doanh thu theo loại: " + e.getMessage());
        }
        
        return list;
    }
    
    // Tìm kiếm phiếu thu
    public List<ThuPhi> search(String keyword) {
        List<ThuPhi> list = new ArrayList<>();
        String sql = "SELECT tp.*, hv.TenHoiVien FROM ThuPhi tp " +
                     "INNER JOIN HoiVien hv ON tp.MaHoiVien = hv.MaHoiVien " +
                     "WHERE tp.MaPhieu LIKE ? OR tp.MaHoiVien LIKE ? OR hv.TenHoiVien LIKE ? " +
                     "OR tp.LoaiPhi LIKE ? OR tp.NguoiThu LIKE ? " +
                     "ORDER BY tp.NgayThu DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            ps.setString(5, searchPattern);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ThuPhi thuPhi = createThuPhiFromResultSet(rs);
                list.add(thuPhi);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm phiếu thu: " + e.getMessage());
        }
        
        return list;
    }
    
    // Sinh mã phiếu tiếp theo dạng TP###
    public String getNextId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaPhieu, 3, 3) AS INT)) FROM ThuPhi";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return String.format("TP%03d", maxId + 1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy mã tiếp theo: " + e.getMessage());
        }
        
        return "TP001";
    }
    
    // Kiểm tra phiếu thu tồn tại
    public boolean exists(String maPhieu) {
        String sql = "SELECT COUNT(*) FROM ThuPhi WHERE MaPhieu = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maPhieu);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra phiếu thu: " + e.getMessage());
        }
        
        return false;
    }
    
    // Mapping ResultSet -> ThuPhi
    private ThuPhi createThuPhiFromResultSet(ResultSet rs) throws SQLException {
        ThuPhi thuPhi = new ThuPhi();
        thuPhi.setMaPhieu(rs.getString("MaPhieu"));
        thuPhi.setMaHoiVien(rs.getString("MaHoiVien"));
        thuPhi.setTenHoiVien(rs.getString("TenHoiVien"));
        thuPhi.setLoaiPhi(rs.getString("LoaiPhi"));
        thuPhi.setSoTien(rs.getBigDecimal("SoTien"));
        thuPhi.setNgayThu(rs.getDate("NgayThu"));
        thuPhi.setPhuongThucTT(rs.getString("PhuongThucTT"));
        thuPhi.setNguoiThu(rs.getString("NguoiThu"));
        thuPhi.setGhiChu(rs.getString("GhiChu"));
        
        return thuPhi;
    }
    
    // Top hội viên chi tiêu nhiều nhất
    public List<Object[]> getTopSpenders(int limit) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT TOP (?) tp.MaHoiVien, hv.TenHoiVien, COUNT(*) as SoGiaoDich, SUM(tp.SoTien) as TongChi " +
                     "FROM ThuPhi tp " +
                     "INNER JOIN HoiVien hv ON tp.MaHoiVien = hv.MaHoiVien " +
                     "GROUP BY tp.MaHoiVien, hv.TenHoiVien " +
                     "ORDER BY TongChi DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaHoiVien"),
                    rs.getString("TenHoiVien"),
                    rs.getInt("SoGiaoDich"),
                    rs.getBigDecimal("TongChi")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy top chi tiêu: " + e.getMessage());
        }
        
        return list;
    }
}