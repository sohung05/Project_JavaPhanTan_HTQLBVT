package app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import service.IDashboardService;
import service.IHoaDonService;
import service.IKhachHangService;
import service.ILichTrinhService;
import service.INhanVienService;
import service.ITaiKhoanService;
import service.impl.DashboardServiceImpl;
import service.impl.HoaDonServiceImpl;
import service.impl.KhachHangServiceImpl;
import service.impl.LichTrinhServiceImpl;
import service.impl.NhanVienServiceImpl;
import service.impl.TaiKhoanServiceImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String[] args) {
        try {
            // 1. Khởi tạo EntityManager từ Persistence Unit
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("mssql-pu");
            EntityManager em = emf.createEntityManager();

            // 2. Khởi tạo các Service Implementation
            IKhachHangService khachHangService = new KhachHangServiceImpl(em);
            IHoaDonService hoaDonService = new HoaDonServiceImpl(em);
            ILichTrinhService lichTrinhService = new LichTrinhServiceImpl(em);
            INhanVienService nhanVienService = new NhanVienServiceImpl(em);
            IDashboardService dashboardService = new DashboardServiceImpl();
            ITaiKhoanService taiKhoanService = new TaiKhoanServiceImpl();

            // 3. Tạo RMI Registry tại cổng 1099
            LocateRegistry.createRegistry(1099);

            // 4. Đăng ký các dịch vụ vào Registry
            Naming.rebind("rmi://localhost:1099/KhachHangService", khachHangService);
            Naming.rebind("rmi://localhost:1099/HoaDonService", hoaDonService);
            Naming.rebind("rmi://localhost:1099/LichTrinhService", lichTrinhService);
            Naming.rebind("rmi://localhost:1099/NhanVienService", nhanVienService);
            Naming.rebind("rmi://localhost:1099/DashboardService", dashboardService);
            Naming.rebind("rmi://localhost:1099/TaiKhoanService", taiKhoanService);

            System.out.println("-------------------------------------------");
            System.out.println("RMI Server is running on port 1099...");
            System.out.println("Services bound successfully!");
            System.out.println("- KhachHangService");
            System.out.println("- HoaDonService");
            System.out.println("- LichTrinhService");
            System.out.println("- NhanVienService");
            System.out.println("- DashboardService");
            System.out.println("- TaiKhoanService");
            System.out.println("-------------------------------------------");

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
