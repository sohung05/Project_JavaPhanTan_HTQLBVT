package service.impl;

import entity.DonTreoDat;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import service.IDonTreoService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.List;

public class DonTreoServiceImpl extends UnicastRemoteObject implements IDonTreoService {
    private EntityManager em;

    public DonTreoServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public void themDonTreo(DonTreoDat don) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Tự động tạo mã đơn treo nếu chưa có
            if (don.getMaDonTreo() == null || don.getMaDonTreo().isEmpty()) {
                String timestamp = java.time.format.DateTimeFormatter.ofPattern("yyMMddHHmmss").format(LocalDateTime.now());
                don.setMaDonTreo("DT" + timestamp);
            }
            
            System.out.println("DEBUG: Đang lưu đơn treo: " + don.getMaDonTreo());
            // Sử dụng merge thay vì persist để xử lý các thực thể đã tách rời (LichTrinh, ChoNgoi)
            em.merge(don);
            tx.commit();
            System.out.println("DEBUG: Đã lưu đơn treo thành công: " + don.getMaDonTreo());
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("DEBUG: Lỗi khi lưu đơn treo: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Lỗi khi thêm đơn treo: " + e.getMessage());
        }
    }

    @Override
    public List<DonTreoDat> layDanhSachDonTreo() throws RemoteException {
        System.out.println("DEBUG: Đang lấy danh sách đơn treo...");
        xoaDonHetHan();
        
        // Xóa cache để đảm bảo lấy dữ liệu mới nhất từ DB
        em.clear(); 
        
        List<DonTreoDat> ds = em.createQuery("SELECT d FROM DonTreoDat d", DonTreoDat.class).getResultList();
        System.out.println("DEBUG: Số lượng đơn treo lấy được: " + ds.size());
        return ds;
    }

    @Override
    public DonTreoDat layDonTreo(String maDon) throws RemoteException {
        return em.find(DonTreoDat.class, maDon);
    }

    @Override
    public boolean xoaDonTreo(String maDon) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            DonTreoDat don = em.find(DonTreoDat.class, maDon);
            if (don != null) {
                em.remove(don);
                tx.commit();
                return true;
            }
            tx.commit();
            return false;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<DonTreoDat> layDonTreoTheoCCCD(String cccd) throws RemoteException {
        TypedQuery<DonTreoDat> query = em.createQuery(
            "SELECT d FROM DonTreoDat d WHERE d.cccdNguoiDat LIKE :cccd", DonTreoDat.class);
        query.setParameter("cccd", "%" + cccd + "%");
        return query.getResultList();
    }

    @Override
    public List<DonTreoDat> layDonTreoTheoSDT(String sdt) throws RemoteException {
        TypedQuery<DonTreoDat> query = em.createQuery(
            "SELECT d FROM DonTreoDat d WHERE d.sdtNguoiDat LIKE :sdt", DonTreoDat.class);
        query.setParameter("sdt", "%" + sdt + "%");
        return query.getResultList();
    }

    @Override
    public void xoaDonHetHan() throws RemoteException {
        LocalDateTime thoiGianGioiHan = LocalDateTime.now().minusMinutes(15);
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<DonTreoDat> hetHan = em.createQuery(
                "SELECT d FROM DonTreoDat d WHERE d.ngayLap < :limit", DonTreoDat.class)
                .setParameter("limit", thoiGianGioiHan)
                .getResultList();
            
            for (DonTreoDat d : hetHan) {
                em.remove(d);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public List<String> layDanhSachMaGheDangTreo(String maLichTrinh) throws RemoteException {
        xoaDonHetHan();
        em.clear();
        String jpql = "SELECT v.choNgoi.maChoNgoi FROM DonTreoDat d JOIN d.danhSachVe v " +
                      "WHERE v.lichTrinh.maLichTrinh = :maLT";
        return em.createQuery(jpql, String.class)
                 .setParameter("maLT", maLichTrinh)
                 .getResultList();
    }
}
