package dao;

import model.ChamCong;
import util.DatabaseConnection;

import java.sql.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// DAO cho bảng Chấm Công
public class ChamCongDAO {
    
    // Check-in hội viên
    public boolean checkIn(String maHoiVien) {
        String sql = "INSERT INTO ChamCong (MaHoiVien, NgayTap, GioVao, GhiChu) VALUES (?, CAST(GETDATE() AS DATE), CAST(GETDATE() AS TIME), N'Check-in')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHoiVien);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi check-in: " + e.getMessage());
            return false;
        }
    }
    
    // Check-out hội viên
    public boolean checkOut(String maHoiVien) {
        String sql = "UPDATE ChamCong SET GioRa = CAST(GETDATE() AS TIME), GhiChu = N'Check-out' " +
                     "WHERE MaHoiVien = ? AND NgayTap = CAST(GETDATE() AS DATE) AND GioRa IS NULL";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHoiVien);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi check-out: " + e.getMessage());
            return false;
        }
    }
    
    // Kiểm tra đã check-in hôm nay
    public boolean isCheckedInToday(String maHoiVien) {
        String sql = "SELECT COUNT(*) FROM ChamCong WHERE MaHoiVien = ? AND NgayTap = CAST(GETDATE() AS DATE)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHoiVien);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra check-in: " + e.getMessage());
        }
        
        return false;
    }
    
    // Kiểm tra đã check-out hôm nay
    public boolean isCheckedOutToday(String maHoiVien) {
        String sql = "SELECT COUNT(*) FROM ChamCong WHERE MaHoiVien = ? AND NgayTap = CAST(GETDATE() AS DATE) AND GioRa IS NOT NULL";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHoiVien);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra check-out: " + e.getMessage());
        }
        
        return false;
    }
    
    // Lịch sử chấm công của 1 hội viên
    public List<ChamCong> getByMember(String maHoiVien) {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT cc.*, hv.TenHoiVien FROM ChamCong cc " +
                     "INNER JOIN HoiVien hv ON cc.MaHoiVien = hv.MaHoiVien " +
                     "WHERE cc.MaHoiVien = ? ORDER BY cc.NgayTap DESC, cc.GioVao DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHoiVien);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ChamCong chamCong = createChamCongFromResultSet(rs);
                list.add(chamCong);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy lịch sử chấm công: " + e.getMessage());
        }
        
        return list;
    }
    
    // Chấm công theo ngày
    public List<ChamCong> getByDate(Date ngay) {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT cc.*, hv.TenHoiVien FROM ChamCong cc " +
                     "INNER JOIN HoiVien hv ON cc.MaHoiVien = hv.MaHoiVien " +
                     "WHERE cc.NgayTap = ? ORDER BY cc.GioVao";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, new java.sql.Date(ngay.getTime()));
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ChamCong chamCong = createChamCongFromResultSet(rs);
                list.add(chamCong);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy chấm công theo ngày: " + e.getMessage());
        }
        
        return list;
    }
    
    // Danh sách chấm công hôm nay
    public List<ChamCong> getToday() {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT cc.*, hv.TenHoiVien FROM ChamCong cc " +
                     "INNER JOIN HoiVien hv ON cc.MaHoiVien = hv.MaHoiVien " +
                     "WHERE cc.NgayTap = CAST(GETDATE() AS DATE) " +
                     "ORDER BY cc.GioVao DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                ChamCong chamCong = createChamCongFromResultSet(rs);
                list.add(chamCong);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy chấm công hôm nay: " + e.getMessage());
        }
        
        return list;
    }
    
    // Chấm công theo khoảng thời gian
    public List<ChamCong> getByDateRange(Date tuNgay, Date denNgay) {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT cc.*, hv.TenHoiVien FROM ChamCong cc " +
                     "INNER JOIN HoiVien hv ON cc.MaHoiVien = hv.MaHoiVien " +
                     "WHERE cc.NgayTap BETWEEN ? AND ? " +
                     "ORDER BY cc.NgayTap DESC, cc.GioVao DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
            ps.setDate(2, new java.sql.Date(denNgay.getTime()));
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ChamCong chamCong = createChamCongFromResultSet(rs);
                list.add(chamCong);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy chấm công theo khoảng thời gian: " + e.getMessage());
        }
        
        return list;
    }
    
    // Đếm số buổi tập theo tháng
    public int countSessionsInMonth(String maHoiVien, int thang, int nam) {
        String sql = "SELECT COUNT(*) FROM ChamCong " +
                     "WHERE MaHoiVien = ? AND MONTH(NgayTap) = ? AND YEAR(NgayTap) = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHoiVien);
            ps.setInt(2, thang);
            ps.setInt(3, nam);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đếm buổi tập trong tháng: " + e.getMessage());
        }
        
        return 0;
    }
    
    // Thống kê tần suất tập theo hội viên
    public List<Object[]> getAttendanceStats(Date tuNgay, Date denNgay) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT hv.MaHoiVien, hv.TenHoiVien, COUNT(cc.ID) as SoBuoiTap " +
                     "FROM HoiVien hv " +
                     "LEFT JOIN ChamCong cc ON hv.MaHoiVien = cc.MaHoiVien " +
                     "AND cc.NgayTap BETWEEN ? AND ? " +
                     "WHERE hv.TrangThai = 1 " +
                     "GROUP BY hv.MaHoiVien, hv.TenHoiVien " +
                     "ORDER BY SoBuoiTap DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
            ps.setDate(2, new java.sql.Date(denNgay.getTime()));
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaHoiVien"),
                    rs.getString("TenHoiVien"),
                    rs.getInt("SoBuoiTap")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thống kê tần suất tập: " + e.getMessage());
        }
        
        return list;
    }
    
    // Danh sách hội viên đang có mặt (đã check-in, chưa check-out)
    public List<ChamCong> getCurrentMembersInGym() {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT cc.*, hv.TenHoiVien FROM ChamCong cc " +
                     "INNER JOIN HoiVien hv ON cc.MaHoiVien = hv.MaHoiVien " +
                     "WHERE cc.NgayTap = CAST(GETDATE() AS DATE) AND cc.GioRa IS NULL " +
                     "ORDER BY cc.GioVao DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                ChamCong chamCong = createChamCongFromResultSet(rs);
                list.add(chamCong);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách hội viên hiện tại trong gym: " + e.getMessage());
        }
        
        return list;
    }
    
    // Thống kê theo giờ trong ngày
    public List<Object[]> getHourlyStats(Date ngay) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT DATEPART(HOUR, GioVao) as Gio, COUNT(*) as SoLuot " +
                     "FROM ChamCong " +
                     "WHERE NgayTap = ? " +
                     "GROUP BY DATEPART(HOUR, GioVao) " +
                     "ORDER BY Gio";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, new java.sql.Date(ngay.getTime()));
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("Gio"),
                    rs.getInt("SoLuot")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thống kê theo giờ: " + e.getMessage());
        }
        
        return list;
    }
    
    // Xóa bản ghi chấm công
    public boolean delete(int id) {
        String sql = "DELETE FROM ChamCong WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa bản ghi chấm công: " + e.getMessage());
            return false;
        }
    }
    
    // Cập nhật ghi chú
    public boolean updateNote(int id, String ghiChu) {
        String sql = "UPDATE ChamCong SET GhiChu = ? WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ghiChu);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật ghi chú: " + e.getMessage());
            return false;
        }
    }
    
    // Lấy chấm công theo ID
    public ChamCong getById(int id) {
        String sql = "SELECT cc.*, hv.TenHoiVien FROM ChamCong cc " +
                     "INNER JOIN HoiVien hv ON cc.MaHoiVien = hv.MaHoiVien " +
                     "WHERE cc.ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createChamCongFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy chấm công theo ID: " + e.getMessage());
        }
        
        return null;
    }
    
    // Tìm kiếm theo tên hội viên
    public List<ChamCong> searchByMemberName(String tenHoiVien) {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT cc.*, hv.TenHoiVien FROM ChamCong cc " +
                     "INNER JOIN HoiVien hv ON cc.MaHoiVien = hv.MaHoiVien " +
                     "WHERE hv.TenHoiVien LIKE ? " +
                     "ORDER BY cc.NgayTap DESC, cc.GioVao DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + tenHoiVien + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ChamCong chamCong = createChamCongFromResultSet(rs);
                list.add(chamCong);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm chấm công theo tên: " + e.getMessage());
        }
        
        return list;
    }
    
    // Mapping ResultSet -> ChamCong
    private ChamCong createChamCongFromResultSet(ResultSet rs) throws SQLException {
        ChamCong chamCong = new ChamCong();
        chamCong.setId(rs.getInt("ID"));
        chamCong.setMaHoiVien(rs.getString("MaHoiVien"));
        chamCong.setNgayTap(rs.getDate("NgayTap"));
        
        Time gioVao = rs.getTime("GioVao");
        if (gioVao != null) {
            chamCong.setGioVao(gioVao);
        }
        
        Time gioRa = rs.getTime("GioRa");
        if (gioRa != null) {
            chamCong.setGioRa(gioRa);
        }
        
        chamCong.setGhiChu(rs.getString("GhiChu"));
        
        // Thêm tên hội viên nếu có
        try {
            chamCong.setTenHoiVien(rs.getString("TenHoiVien"));
        } catch (SQLException e) {
            // Không có cột TenHoiVien, bỏ qua
        }
        
        return chamCong;
    }
}