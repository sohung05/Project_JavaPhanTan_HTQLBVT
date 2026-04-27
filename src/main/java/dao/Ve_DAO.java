package dao;

import entity.Ve;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import utils.EntityManagerFactoryUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ve_DAO {
    private EntityManager em;

    public Ve_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<Ve> findAll() {
        try {
            return em.createQuery("SELECT v FROM Ve v ORDER BY v.thoiGianLenTau DESC", Ve.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Ve findByMaVe(String maVe) {
        try {
            return em.find(Ve.class, maVe);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insert(Ve ve) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(ve);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Ve ve) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(ve);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maVe) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Ve ve = em.find(Ve.class, maVe);
            if (ve != null) {
                ve.setTrangThai(false);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean kiemTraGheDaDat(String maChoNgoi, String maLichTrinh) {
        try {
            Long count = em.createQuery("SELECT COUNT(v) FROM Ve v WHERE v.choNgoi.maChoNgoi = :maChoNgoi AND v.lichTrinh.maLichTrinh = :maLichTrinh AND v.trangThai = true", Long.class)
                    .setParameter("maChoNgoi", maChoNgoi)
                    .setParameter("maLichTrinh", maLichTrinh)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public Set<String> layDanhSachGheDaDat(String maLichTrinh) {
        try {
            List<String> results = em.createQuery("SELECT v.choNgoi.maChoNgoi FROM Ve v WHERE v.lichTrinh.maLichTrinh = :maLichTrinh AND v.trangThai = true", String.class)
                    .setParameter("maLichTrinh", maLichTrinh)
                    .getResultList();
            return new HashSet<>(results);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    public List<Ve> searchVe(String keyword) {
        try {
            String jpql = "SELECT v FROM Ve v WHERE (v.maVe LIKE :keyword OR v.soCCCD LIKE :keyword) AND v.trangThai = true ORDER BY v.thoiGianLenTau DESC";
            return em.createQuery(jpql, Ve.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Ve> findByMaHoaDon(String maHoaDon) {
        try {
            // Giả sử có bảng ChiTietHoaDon hoặc mối quan hệ trong HoaDon
            // Nếu dùng Native Query cho chắc chắn logic cũ:
            String sql = "SELECT v.* FROM Ve v JOIN ChiTietHoaDon cthd ON v.maVe = cthd.maVe WHERE cthd.maHoaDon = ? AND v.trangThai = 1";
            return em.createNativeQuery(sql, Ve.class)
                    .setParameter(1, maHoaDon)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
