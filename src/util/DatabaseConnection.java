package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 */
public class DatabaseConnection {
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyPhongGym;encrypt=true;trustServerCertificate=true";
    private static final String USERNAME = "gymuser"; // Thay đổi theo cấu hình của bạn
    private static final String PASSWORD = "c24"; // Thay đổi theo cấu hình của bạn
    
    private static Connection connection = null;
    
    // Private constructor to prevent instantiation
    private DatabaseConnection() {}
    
    /**
     * Kết nối đến database
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Kết nối database thành công!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy driver SQL Server: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Không tìm thấy driver SQL Server.\nVui lòng thêm thư viện sqljdbc4.jar vào project!", 
                "Lỗi Driver", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối database: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Không thể kết nối đến database.\nVui lòng kiểm tra:\n" +
                "- SQL Server có đang chạy không?\n" +
                "- Thông tin kết nối có đúng không?\n" +
                "- Database QuanLyPhongGym có tồn tại không?\n\n" +
                "Chi tiết lỗi: " + e.getMessage(), 
                "Lỗi Kết Nối", JOptionPane.ERROR_MESSAGE);
        }
        return connection;
    }
    
    /**
     * Đóng kết nối database
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đã đóng kết nối database!");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
        }
    }
    
    /**
     * Kiểm tra kết nối có hoạt động không
     * @return true nếu kết nối OK, false nếu có lỗi
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Lấy thông tin kết nối hiện tại
     * @return String mô tả trạng thái kết nối
     */
    public static String getConnectionInfo() {
        try {
            if (connection != null && !connection.isClosed()) {
                return "Đã kết nối đến: " + connection.getMetaData().getURL();
            } else {
                return "Chưa kết nối hoặc kết nối đã đóng";
            }
        } catch (SQLException e) {
            return "Lỗi khi lấy thông tin kết nối: " + e.getMessage();
        }
    }
}