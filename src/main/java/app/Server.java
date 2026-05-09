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
import service.IGaService;
import service.IVeService;
import service.ILoaiVeService;
import service.IToaService;
import service.IChoNgoiService;
import service.IThongKeService;
import service.IKhuyenMaiService;
import service.IDonTreoService;
import service.impl.DashboardServiceImpl;
import service.impl.HoaDonServiceImpl;
import service.impl.KhachHangServiceImpl;
import service.impl.LichTrinhServiceImpl;
import service.impl.NhanVienServiceImpl;
import service.impl.TaiKhoanServiceImpl;
import service.impl.GaServiceImpl;
import service.impl.VeServiceImpl;
import service.impl.LoaiVeServiceImpl;
import service.impl.ToaServiceImpl;
import service.impl.ChoNgoiServiceImpl;
import service.impl.ThongKeServiceImpl;
import service.impl.KhuyenMaiServiceImpl;
import service.impl.DonTreoServiceImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String[] args) {
        try {
            // 0. Cấu hình IP cho RMI Server trong mạng LAN
            System.setProperty("java.rmi.server.hostname", "172.20.10.5");

            // 1. Khởi tạo EntityManager từ Persistence Unit
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("mssql-pu");
            EntityManager em = emf.createEntityManager();

            // 2. Khởi tạo các Service Implementation
            IKhachHangService khachHangService = new KhachHangServiceImpl(em);
            IHoaDonService hoaDonService = new HoaDonServiceImpl(em);
            ILichTrinhService lichTrinhService = new LichTrinhServiceImpl(em);
            INhanVienService nhanVienService = new NhanVienServiceImpl(em);
            IDashboardService dashboardService = new DashboardServiceImpl(em);
            ITaiKhoanService taiKhoanService = new TaiKhoanServiceImpl(em);
            IGaService gaService = new GaServiceImpl(em);
            IVeService veService = new VeServiceImpl(em);
            ILoaiVeService loaiVeService = new LoaiVeServiceImpl(em);
            IToaService toaService = new ToaServiceImpl(em);
            IChoNgoiService choNgoiService = new ChoNgoiServiceImpl(em);
            IThongKeService thongKeService = new ThongKeServiceImpl(em);
            IKhuyenMaiService khuyenMaiService = new KhuyenMaiServiceImpl(em);
            IDonTreoService donTreoService = new DonTreoServiceImpl();

            // 3. Tạo RMI Registry tại cổng 1099
            LocateRegistry.createRegistry(1099);

            // 4. Đăng ký các dịch vụ vào Registry
            Naming.rebind("rmi://172.20.10.5:1099/KhachHangService", khachHangService);
            Naming.rebind("rmi://172.20.10.5:1099/HoaDonService", hoaDonService);
            Naming.rebind("rmi://172.20.10.5:1099/LichTrinhService", lichTrinhService);
            Naming.rebind("rmi://172.20.10.5:1099/NhanVienService", nhanVienService);
            Naming.rebind("rmi://172.20.10.5:1099/DashboardService", dashboardService);
            Naming.rebind("rmi://172.20.10.5:1099/TaiKhoanService", taiKhoanService);
            Naming.rebind("rmi://172.20.10.5:1099/GaService", gaService);
            Naming.rebind("rmi://172.20.10.5:1099/VeService", veService);
            Naming.rebind("rmi://172.20.10.5:1099/LoaiVeService", loaiVeService);
            Naming.rebind("rmi://172.20.10.5:1099/ToaService", toaService);
            Naming.rebind("rmi://172.20.10.5:1099/ChoNgoiService", choNgoiService);
            Naming.rebind("rmi://172.20.10.5:1099/ThongKeService", thongKeService);
            Naming.rebind("rmi://172.20.10.5:1099/KhuyenMaiService", khuyenMaiService);
            Naming.rebind("rmi://172.20.10.5:1099/DonTreoService", donTreoService);

            System.out.println("-------------------------------------------");
            System.out.println("RMI Server is running on port 1099...");
            System.out.println("Services bound successfully!");
            System.out.println("- KhachHangService");
            System.out.println("- HoaDonService");
            System.out.println("- LichTrinhService");
            System.out.println("- NhanVienService");
            System.out.println("- DashboardService");
            System.out.println("- TaiKhoanService");
            System.out.println("- GaService");
            System.out.println("- VeService");
            System.out.println("- LoaiVeService");
            System.out.println("- ToaService");
            System.out.println("- ChoNgoiService");
            System.out.println("- ThongKeService");
            System.out.println("- KhuyenMaiService");
            System.out.println("- DonTreoService");
            System.out.println("-------------------------------------------");

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
