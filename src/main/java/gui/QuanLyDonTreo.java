package gui;

import entity.DonTreoDat;
import service.IDonTreoService;
import utils.ClientContext;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

/**
 * Class static để quản lý danh sách đơn treo thông qua RMI Service
 */
public class QuanLyDonTreo {
    
    private static IDonTreoService getService() {
        return ClientContext.getDonTreoService();
    }
    
    /**
     * Thêm đơn treo mới (Lưu xuống Database qua RMI)
     */
    public static void themDonTreo(DonTreoDat donTreo) {
        try {
            // Tự động set ngày lập và giờ lập nếu chưa có
            if (donTreo.getNgayLap() == null) {
                donTreo.setNgayLap(java.time.LocalDateTime.now());
            }
            if (donTreo.getGioLap() == null) {
                donTreo.setGioLap(java.time.LocalDateTime.now());
            }
            
            getService().themDonTreo(donTreo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi treo đơn: " + e.getMessage(), "Lỗi RMI", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Lấy danh sách tất cả đơn treo từ Database
     */
    public static List<DonTreoDat> layDanhSachDonTreo() {
        try {
            return getService().layDanhSachDonTreo();
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh sách đơn treo qua RMI: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Xóa tất cả đơn đã hết hạn (> 15 phút) trên Database
     */
    public static void xoaDonHetHan() {
        try {
            getService().xoaDonHetHan();
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa đơn hết hạn qua RMI: " + e.getMessage());
        }
    }
    
    /**
     * Xóa đơn treo theo mã trên Database
     */
    public static boolean xoaDonTreo(String maDonTreo) {
        try {
            return getService().xoaDonTreo(maDonTreo);
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa đơn treo qua RMI: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Lấy đơn treo theo mã từ Database
     */
    public static DonTreoDat layDonTreo(String maDonTreo) {
        try {
            return getService().layDonTreo(maDonTreo);
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy đơn treo theo mã qua RMI: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lấy đơn treo theo CCCD từ Database
     */
    public static List<DonTreoDat> layDonTreoTheoCCCD(String cccd) {
        try {
            return getService().layDonTreoTheoCCCD(cccd);
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm đơn treo theo CCCD qua RMI: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Lấy đơn treo theo SĐT từ Database
     */
    public static List<DonTreoDat> layDonTreoTheoSDT(String sdt) {
        try {
            return getService().layDonTreoTheoSDT(sdt);
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm đơn treo theo SDT qua RMI: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Xóa tất cả đơn treo (Không khuyến khích dùng rộng rãi)
     */
    public static void xoaTatCa() {
        // Có thể thêm vào IDonTreoService nếu cần thiết
    }
    
    /**
     * Đếm số lượng đơn treo đang có trong Database
     */
    public static int demSoLuong() {
        try {
            return layDanhSachDonTreo().size();
        } catch (Exception e) {
            return 0;
        }
    }
}
