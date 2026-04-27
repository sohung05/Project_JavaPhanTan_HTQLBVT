package dao;

import entity.ChoNgoi;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import utils.EntityManagerFactoryUtil;

import java.util.ArrayList;
import java.util.List;

public class ChoNgoi_DAO {
    private EntityManager em;

    public ChoNgoi_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<ChoNgoi> getChoNgoiByMaToa(String maToa) {
        try {
            return em.createQuery("SELECT cn FROM ChoNgoi cn WHERE cn.toa.maToa = :maToa ORDER BY cn.viTri", ChoNgoi.class)
                    .setParameter("maToa", maToa)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean kiemTraChoNgoiDaDat(String maChoNgoi, String maLichTrinh) {
        try {
            String jpql = "SELECT COUNT(v) FROM Ve v WHERE v.choNgoi.maChoNgoi = :maChoNgoi AND v.lichTrinh.maLichTrinh = :maLichTrinh AND v.trangThai = true";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("maChoNgoi", maChoNgoi)
                    .setParameter("maLichTrinh", maLichTrinh)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ChoNgoi> getChoNgoiTrong(String maToa, String maLichTrinh) {
        try {
            // Lấy tất cả ghế của toa, trừ những ghế đã có vé trong lịch trình đó
            String jpql = "SELECT cn FROM ChoNgoi cn WHERE cn.toa.maToa = :maToa " +
                          "AND cn.maChoNgoi NOT IN (" +
                          "  SELECT v.choNgoi.maChoNgoi FROM Ve v " +
                          "  WHERE v.lichTrinh.maLichTrinh = :maLichTrinh AND v.trangThai = true" +
                          ") ORDER BY cn.viTri";
            return em.createQuery(jpql, ChoNgoi.class)
                    .setParameter("maToa", maToa)
                    .setParameter("maLichTrinh", maLichTrinh)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<ChoNgoi> findAll() {
        try {
            return em.createQuery("SELECT cn FROM ChoNgoi cn ORDER BY cn.toa.chuyenTau.soHieuTau, cn.toa.soToa, cn.viTri", ChoNgoi.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ChoNgoi findByMaChoNgoi(String maChoNgoi) {
        try {
            return em.find(ChoNgoi.class, maChoNgoi);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insert(ChoNgoi cn) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(cn);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(ChoNgoi cn) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(cn);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maChoNgoi) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ChoNgoi cn = em.find(ChoNgoi.class, maChoNgoi);
            if (cn != null) {
                em.remove(cn);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
}
