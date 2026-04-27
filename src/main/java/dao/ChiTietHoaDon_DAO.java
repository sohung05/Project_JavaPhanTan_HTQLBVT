package dao;

import entity.ChiTietHoaDon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.EntityManagerFactoryUtil;

import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDon_DAO {
    private EntityManager em;

    public ChiTietHoaDon_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<ChiTietHoaDon> findByMaHoaDon(String maHoaDon) {
        try {
            return em.createQuery("SELECT ct FROM ChiTietHoaDon ct WHERE ct.hoaDon.maHoaDon = :maHoaDon", ChiTietHoaDon.class)
                    .setParameter("maHoaDon", maHoaDon)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean insert(ChiTietHoaDon cthd) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(cthd);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maHoaDon, String maVe) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Tìm CTHD có mã hóa đơn và mã vé tương ứng
            List<ChiTietHoaDon> list = em.createQuery("SELECT ct FROM ChiTietHoaDon ct WHERE ct.hoaDon.maHoaDon = :maHoaDon AND ct.ve.maVe = :maVe", ChiTietHoaDon.class)
                    .setParameter("maHoaDon", maHoaDon)
                    .setParameter("maVe", maVe)
                    .getResultList();
            if (!list.isEmpty()) {
                em.remove(list.get(0));
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteByMaHoaDon(String maHoaDon) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createQuery("DELETE FROM ChiTietHoaDon ct WHERE ct.hoaDon.maHoaDon = :maHoaDon")
                    .setParameter("maHoaDon", maHoaDon)
                    .executeUpdate();
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
}
