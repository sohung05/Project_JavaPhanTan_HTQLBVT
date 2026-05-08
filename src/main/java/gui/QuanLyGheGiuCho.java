package gui;

import entity.GheGiuCho;
import service.IDonTreoService;
import utils.ClientContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

/**
 * Quản lý danh sách ghế đang được giữ chỗ (15 phút)
 */
public class QuanLyGheGiuCho {
    private static List<GheGiuCho> danhSachGheGiuCho = new ArrayList<>();
    private static List<String> danhSachMaGheDangTreoRemote = new ArrayList<>(); // Danh sách từ database
    private static Timer timer = new Timer(true); // Daemon thread
    
    /**
     * Làm mới danh sách ghế đang treo từ Database (RMI) có tính đến chặng (Segments)
     */
    public static void refreshDanhSachGheTreo(String maLichTrinh, String maGaDi, String maGaDen) {
        if (maLichTrinh == null || maGaDi == null || maGaDen == null) return;
        try {
            danhSachMaGheDangTreoRemote = ClientContext.getDonTreoService().layDanhSachMaGheDangTreo(maLichTrinh, maGaDi, maGaDen);
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh sách ghế treo từ Server: " + e.getMessage());
        }
    }
    
    /**
     * Thêm ghế vào danh sách giữ chỗ (cũ - không có maLichTrinh, giữ để tương thích)
     */
    public static void themGheGiuCho(String maChoNgoi, String maDonTreo) {
        themGheGiuCho(maChoNgoi, maDonTreo, null, null, null);
    }
    
    /**
     * Thêm ghế vào danh sách giữ chỗ (trung gian - có maLichTrinh)
     */
    public static void themGheGiuCho(String maChoNgoi, String maDonTreo, String maLichTrinh) {
        themGheGiuCho(maChoNgoi, maDonTreo, maLichTrinh, null, null);
    }
    
    /**
     * Thêm ghế vào danh sách giữ chỗ (mới - có chặng đường)
     */
    public static void themGheGiuCho(String maChoNgoi, String maDonTreo, String maLichTrinh, String maGaDi, String maGaDen) {
        GheGiuCho gheGiuCho = new GheGiuCho(maChoNgoi, maDonTreo, maLichTrinh, maGaDi, maGaDen);
        danhSachGheGiuCho.add(gheGiuCho);
        
        // Tạo task tự động xóa sau 15 phút
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                xoaGheGiuCho(maChoNgoi, maLichTrinh);
                System.out.println("Đã hết hạn giữ chỗ: " + maChoNgoi + " (Lịch trình: " + maLichTrinh + ")");
            }
        }, 15 * 60 * 1000); // 15 phút = 15 * 60 * 1000 milliseconds
    }
    
    /**
     * Kiểm tra ghế có đang được giữ chỗ không (cũ - không check maLichTrinh)
     */
    public static boolean kiemTraGheDangGiuCho(String maChoNgoi) {
        return kiemTraGheDangGiuCho(maChoNgoi, null);
    }
    
    /**
     * Kiểm tra ghế có đang được giữ chỗ không (mới - check cả maLichTrinh)
     */
    public static boolean kiemTraGheDangGiuCho(String maChoNgoi, String maLichTrinh) {
        // 1. Kiểm tra trong danh sách RAM cục bộ trước (ưu tiên vì nhanh)
        xoaGheHetHan();
        
        boolean dangGiuChoLocal = false;
        if (maLichTrinh == null) {
            dangGiuChoLocal = danhSachGheGiuCho.stream()
                .anyMatch(ghe -> ghe.getMaChoNgoi().equals(maChoNgoi) && ghe.conTrongThoiGianGiuCho());
        } else {
            dangGiuChoLocal = danhSachGheGiuCho.stream()
                .anyMatch(ghe -> ghe.getMaChoNgoi().equals(maChoNgoi) 
                    && (ghe.getMaLichTrinh() == null || ghe.getMaLichTrinh().equals(maLichTrinh))
                    && ghe.conTrongThoiGianGiuCho());
        }
        
        if (dangGiuChoLocal) return true;
        
        // 2. Nếu không có trong RAM, kiểm tra trong danh sách từ Database (nếu đã được load)
        if (danhSachMaGheDangTreoRemote != null && danhSachMaGheDangTreoRemote.contains(maChoNgoi)) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Xóa ghế khỏi danh sách giữ chỗ (cũ - không check maLichTrinh)
     */
    public static void xoaGheGiuCho(String maChoNgoi) {
        danhSachGheGiuCho.removeIf(ghe -> ghe.getMaChoNgoi().equals(maChoNgoi));
    }
    
    /**
     * Xóa ghế khỏi danh sách giữ chỗ (mới - check cả maLichTrinh)
     */
    public static void xoaGheGiuCho(String maChoNgoi, String maLichTrinh) {
        if (maLichTrinh == null) {
            xoaGheGiuCho(maChoNgoi);
        } else {
            danhSachGheGiuCho.removeIf(ghe -> ghe.getMaChoNgoi().equals(maChoNgoi) 
                && (ghe.getMaLichTrinh() == null || ghe.getMaLichTrinh().equals(maLichTrinh)));
        }
    }
    
    /**
     * Lấy danh sách mã ghế đang treo từ Remote
     */
    public static List<String> getDanhSachMaGheDangTreoRemote() {
        if (danhSachMaGheDangTreoRemote == null) {
            danhSachMaGheDangTreoRemote = new ArrayList<>();
        }
        return danhSachMaGheDangTreoRemote;
    }

    /**
     * Xóa tất cả ghế của một đơn treo (khỏi cả danh sách RAM và danh sách Remote)
     */
    public static void xoaTatCaGheCuaDonTreo(String maDonTreo) {
        xoaTatCaGheCuaDonTreo(maDonTreo, new ArrayList<>());
    }

    /**
     * Xóa tất cả ghế của một đơn treo kèm theo danh sách mã ghế cụ thể (Tối ưu cho đồng bộ Remote)
     */
    public static void xoaTatCaGheCuaDonTreo(String maDonTreo, List<String> maGheBosung) {
        if (maDonTreo == null) return;
        
        System.out.println("🔍 Đang giải phóng ghế cho đơn: " + maDonTreo);
        
        // 1. Tìm các mã ghế thuộc đơn này trong danh sách RAM cục bộ
        List<String> maGheCanXoa = danhSachGheGiuCho.stream()
                .filter(ghe -> maDonTreo.equals(ghe.getMaDonTreo()))
                .map(GheGiuCho::getMaChoNgoi)
                .collect(Collectors.toCollection(ArrayList::new));
        
        // 2. Thêm các mã ghế bổ sung (lấy từ Database) nếu chưa có
        if (maGheBosung != null) {
            for (String ma : maGheBosung) {
                if (ma != null && !maGheCanXoa.contains(ma)) {
                    maGheCanXoa.add(ma);
                }
            }
        }
        
        // 3. Xóa khỏi danh sách RAM cục bộ
        danhSachGheGiuCho.removeIf(ghe -> maDonTreo.equals(ghe.getMaDonTreo()));
        
        // 4. Xóa khỏi danh sách Remote (đây là bước quan trọng nhất)
        if (danhSachMaGheDangTreoRemote != null && !maGheCanXoa.isEmpty()) {
            boolean removed = danhSachMaGheDangTreoRemote.removeAll(maGheCanXoa);
            System.out.println("♻️ Đã gỡ " + maGheCanXoa.size() + " ghế khỏi Remote cache: " + removed);
        }
        
        System.out.println("🗑️ Tổng cộng đã giải phóng " + maGheCanXoa.size() + " ghế.");
    }
    
    /**
     * Gia hạn thời gian giữ chỗ cho các ghế của đơn treo (thêm 15 phút nữa)
     */
    public static void giaHanGheCuaDonTreo(String maDonTreo) {
        for (GheGiuCho ghe : danhSachGheGiuCho) {
            if (ghe.getMaDonTreo() != null && ghe.getMaDonTreo().equals(maDonTreo)) {
                ghe.giaHanThoiGian(15); // Gia hạn thêm 15 phút
            }
        }
        System.out.println("✅ Đã gia hạn ghế giữ chỗ cho đơn: " + maDonTreo);
    }
    
    /**
     * Xóa các ghế đã hết hạn giữ chỗ
     */
    public static void xoaGheHetHan() {
        danhSachGheGiuCho.removeIf(ghe -> !ghe.conTrongThoiGianGiuCho());
    }
    
    /**
     * Lấy danh sách ghế đang giữ chỗ
     */
    public static List<GheGiuCho> layDanhSachGheGiuCho() {
        xoaGheHetHan();
        return new ArrayList<>(danhSachGheGiuCho);
    }
    
    /**
     * Đếm số ghế đang giữ chỗ
     */
    public static int demSoGheGiuCho() {
        xoaGheHetHan();
        return danhSachGheGiuCho.size();
    }
}

