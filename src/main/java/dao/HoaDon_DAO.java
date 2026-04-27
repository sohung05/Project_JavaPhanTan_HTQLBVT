package dao;

import entity.HoaDon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import utils.EntityManagerFactoryUtil;

import java.util.ArrayList;
import java.util.List;

public class HoaDon_DAO {
    private EntityManager em;

    public HoaDon_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<HoaDon> findAll() {
        try {
            // Sử dụng Native Query để lấy thông tin tổng hợp KM nếu JPQL không hỗ trợ STRING_AGG dễ dàng
            // Tuy nhiên JPA sẽ tự động map các quan hệ nếu được cấu hình đúng.
            // Để giữ nguyên logic STRING_AGG (nếu có bảng KhuyenMai):
            String sql = "SELECT hd.* FROM HoaDon hd ORDER BY hd.ngayTao DESC, hd.gioTao DESC";
            return em.createNativeQuery(sql, HoaDon.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean insertHoaDon(HoaDon hoaDon) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(hoaDon);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTongTien(String maHoaDon, double tongTien) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            HoaDon hd = em.find(HoaDon.class, maHoaDon);
            if (hd != null) {
                hd.setTongTien(tongTien);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean recalculateTongTien(String maHoaDon) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Tính lại tổng tiền từ bảng Ve/ChiTietHoaDon
            String sql = "UPDATE HoaDon SET tongTien = (" +
                         "SELECT ISNULL(SUM(v.giaVe), 0) FROM Ve v JOIN ChiTietHoaDon cthd ON v.maVe = cthd.maVe " +
                         "WHERE cthd.maHoaDon = ? AND v.trangThai = 1) WHERE maHoaDon = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, maHoaDon);
            query.setParameter(2, maHoaDon);
            query.executeUpdate();
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public HoaDon findByMaHoaDon(String maHoaDon) {
        try {
            return em.find(HoaDon.class, maHoaDon);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<HoaDon> searchHoaDon(String keyword) {
        try {
            // Tìm theo mã hóa đơn hoặc CCCD/SĐT khách hàng
            String jpql = "SELECT hd FROM HoaDon hd WHERE hd.maHoaDon LIKE :keyword " +
                          "OR hd.khachHang.CCCD LIKE :keyword OR hd.khachHang.SDT LIKE :keyword " +
                          "ORDER BY hd.ngayTao DESC, hd.gioTao DESC";
            return em.createQuery(jpql, HoaDon.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean capNhatTongTien(String maHoaDon) {
        return recalculateTongTien(maHoaDon);
    }
}
