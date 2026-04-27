package app;

import jakarta.persistence.EntityManager;
import service.IKhachHangService;
import service.INhanVienService;
import service.IHoaDonService;
import service.ILichTrinhService;
import service.impl.KhachHangServiceImpl;
import service.impl.NhanVienServiceImpl;
import service.impl.HoaDonServiceImpl;
import service.impl.LichTrinhServiceImpl;
import utils.EntityManagerFactoryUtil;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String[] args) {
        try {
            EntityManagerFactoryUtil util = new EntityManagerFactoryUtil();
            EntityManager em = util.getEntityManager();

            LocateRegistry.createRegistry(1099);

            IKhachHangService khachHangService = new KhachHangServiceImpl(em);
            INhanVienService nhanVienService = new NhanVienServiceImpl(em);
            IHoaDonService hoaDonService = new HoaDonServiceImpl(em);
            ILichTrinhService lichTrinhService = new LichTrinhServiceImpl(em);

            Naming.rebind("rmi://localhost:1099/KhachHangService", khachHangService);
            Naming.rebind("rmi://localhost:1099/NhanVienService", nhanVienService);
            Naming.rebind("rmi://localhost:1099/HoaDonService", hoaDonService);
            Naming.rebind("rmi://localhost:1099/LichTrinhService", lichTrinhService);

            System.out.println("✅ RMI Server is running...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
