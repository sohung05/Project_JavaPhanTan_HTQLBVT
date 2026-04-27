package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectDB {
    private static Connection con = null;
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=HTQLVT;encrypt=false;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASSWORD = "sapassword";

    public static Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                con = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Kết nối SQL Server thành công!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Không tìm thấy JDBC Driver! Hãy kiểm tra xem đã thêm file mssql-jdbc.jar chưa?");
        } catch (SQLException e) {
            System.err.println("❌ Lỗi SQL khi kết nối: " + e.getMessage());
        }
        return con;
    }

    public static void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("🔒 Đã ngắt kết nối SQL Server!");
            } catch (SQLException e) {
                System.err.println("❌ Lỗi khi đóng kết nối!");
                e.printStackTrace();
            }
        }
    }
}
