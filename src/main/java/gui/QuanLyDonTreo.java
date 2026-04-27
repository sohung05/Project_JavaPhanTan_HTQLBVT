/*
 * Quản lý danh sách đơn treo (Static)
 */
package gui;

import entity.DonTreoDat;
import java.util.ArrayList;
import java.util.List;

/**
 * Class static để quản lý danh sách đơn treo trong session
 * @author PC
 */
public class QuanLyDonTreo {
    
    private static List<DonTreoDat> danhSachDonTreo = new ArrayList<>();
    private static int soThuTu = 1;
    
    /**
     * Thêm đơn treo mới
     */
    public static void themDonTreo(DonTreoDat donTreo) {
        // Tự động tạo mã đơn treo
        if (donTreo.getMaDonTreo() == null || donTreo.getMaDonTreo().isEmpty()) {
            donTreo.setMaDonTreo("DT" + String.format("%03d", soThuTu++));
        }
        danhSachDonTreo.add(donTreo);
    }
    
    /**
     * Lấy danh sách tất cả đơn treo
     */
    public static List<DonTreoDat> layDanhSachDonTreo() {
        // Tự động xóa các đơn hết hạn
        xoaDonHetHan();
        return new ArrayList<>(danhSachDonTreo);
    }
    
    /**
     * Xóa tất cả đơn đã hết hạn (> 15 phút)
     */
    public static void xoaDonHetHan() {
        List<DonTreoDat> donCanXoa = new ArrayList<>();
        for (DonTreoDat don : danhSachDonTreo) {
            if (!don.conTrongThoiHan()) {
                donCanXoa.add(don);
                // Xóa ghế giữ chỗ của đơn hết hạn
                QuanLyGheGiuCho.xoaTatCaGheCuaDonTreo(don.getMaDonTreo());
                System.out.println("DEBUG: Đơn treo hết hạn tự động bị xóa: " + don.getMaDonTreo());
            }
        }
        danhSachDonTreo.removeAll(donCanXoa);
    }
    
    /**
     * Xóa đơn treo theo mã
     */
    public static boolean xoaDonTreo(String maDonTreo) {
        return danhSachDonTreo.removeIf(don -> don.getMaDonTreo().equals(maDonTreo));
    }
    
    /**
     * Lấy đơn treo theo mã
     */
    public static DonTreoDat layDonTreo(String maDonTreo) {
        return danhSachDonTreo.stream()
            .filter(don -> don.getMaDonTreo().equals(maDonTreo))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Lấy đơn treo theo CCCD
     */
    public static List<DonTreoDat> layDonTreoTheoCCCD(String cccd) {
        List<DonTreoDat> ketQua = new ArrayList<>();
        for (DonTreoDat don : danhSachDonTreo) {
            if (don.getCccdNguoiDat() != null && don.getCccdNguoiDat().contains(cccd)) {
                ketQua.add(don);
            }
        }
        return ketQua;
    }
    
    /**
     * Lấy đơn treo theo SĐT
     */
    public static List<DonTreoDat> layDonTreoTheoSDT(String sdt) {
        List<DonTreoDat> ketQua = new ArrayList<>();
        for (DonTreoDat don : danhSachDonTreo) {
            if (don.getSdtNguoiDat() != null && don.getSdtNguoiDat().contains(sdt)) {
                ketQua.add(don);
            }
        }
        return ketQua;
    }
    
    /**
     * Xóa tất cả đơn treo
     */
    public static void xoaTatCa() {
        danhSachDonTreo.clear();
    }
    
    /**
     * Đếm số lượng đơn treo
     */
    public static int demSoLuong() {
        return danhSachDonTreo.size();
    }
}

