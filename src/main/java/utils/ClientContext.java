package utils;

import service.*;

import java.rmi.Naming;

/**
 * Quản lý các kết nối RMI dịch vụ từ Client tới Server
 */
public class ClientContext {
    private static IKhachHangService khachHangService;
    private static IHoaDonService hoaDonService;
    private static ILichTrinhService lichTrinhService;
    private static INhanVienService nhanVienService;
    private static IDashboardService dashboardService;
    private static ITaiKhoanService taiKhoanService;
    private static IDonTreoService donTreoService;

    private static String serverIP = "localhost";
    private static int serverPort = 1099;

    public static void init() throws Exception {
        String baseAddr = "rmi://" + serverIP + ":" + serverPort + "/";
        
        System.out.println("Connecting to RMI Services at " + baseAddr + "...");
        
        khachHangService = (IKhachHangService) Naming.lookup(baseAddr + "KhachHangService");
        hoaDonService = (IHoaDonService) Naming.lookup(baseAddr + "HoaDonService");
        lichTrinhService = (ILichTrinhService) Naming.lookup(baseAddr + "LichTrinhService");
        nhanVienService = (INhanVienService) Naming.lookup(baseAddr + "NhanVienService");
        dashboardService = (IDashboardService) Naming.lookup(baseAddr + "DashboardService");
        taiKhoanService = (ITaiKhoanService) Naming.lookup(baseAddr + "TaiKhoanService");
        donTreoService = (IDonTreoService) Naming.lookup(baseAddr + "DonTreoService");
        
        System.out.println("✅ All RMI Services connected!");
    }

    public static IKhachHangService getKhachHangService() { return khachHangService; }
    public static IHoaDonService getHoaDonService() { return hoaDonService; }
    public static ILichTrinhService getLichTrinhService() { return lichTrinhService; }
    public static INhanVienService getNhanVienService() { return nhanVienService; }
    public static IDashboardService getDashboardService() { return dashboardService; }
    public static ITaiKhoanService getTaiKhoanService() { return taiKhoanService; }
    public static IDonTreoService getDonTreoService() { return donTreoService; }
}
