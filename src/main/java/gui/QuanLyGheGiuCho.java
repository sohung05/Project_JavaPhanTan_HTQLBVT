package gui;

import entity.GheGiuCho;
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
    private static Timer timer = new Timer(true); // Daemon thread
    
    /**
     * Thêm ghế vào danh sách giữ chỗ (cũ - không có maLichTrinh, giữ để tương thích)
     */
    public static void themGheGiuCho(String maChoNgoi, String maDonTreo) {
        themGheGiuCho(maChoNgoi, maDonTreo, null);
    }
    
    /**
     * Thêm ghế vào danh sách giữ chỗ (mới - có maLichTrinh cho khứ hồi)
     */
    public static void themGheGiuCho(String maChoNgoi, String maDonTreo, String maLichTrinh) {
        GheGiuCho gheGiuCho = new GheGiuCho(maChoNgoi, maDonTreo, maLichTrinh);
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
        // Xóa các ghế đã hết hạn trước
        xoaGheHetHan();
        
        if (maLichTrinh == null) {
            // Nếu không truyền maLichTrinh, check chỉ maChoNgoi (tương thích ngược)
            return danhSachGheGiuCho.stream()
                .anyMatch(ghe -> ghe.getMaChoNgoi().equals(maChoNgoi) && ghe.conTrongThoiGianGiuCho());
        } else {
            // Nếu có maLichTrinh, check cả 2 (chính xác với khứ hồi)
            return danhSachGheGiuCho.stream()
                .anyMatch(ghe -> ghe.getMaChoNgoi().equals(maChoNgoi) 
                    && (ghe.getMaLichTrinh() == null || ghe.getMaLichTrinh().equals(maLichTrinh))
                    && ghe.conTrongThoiGianGiuCho());
        }
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
     * Xóa tất cả ghế của một đơn treo
     */
    public static void xoaTatCaGheCuaDonTreo(String maDonTreo) {
        danhSachGheGiuCho.removeIf(ghe -> ghe.getMaDonTreo() != null && ghe.getMaDonTreo().equals(maDonTreo));
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

