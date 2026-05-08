package dao;

import entity.LichSuInVe;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.EntityManagerFactoryUtil;

import java.util.ArrayList;
import java.util.List;

public class LichSuInVe_DAO {
    private EntityManager em;

    public LichSuInVe_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    /**
     * Lưu một bản ghi lịch sử in mới
     */
    public boolean insert(LichSuInVe ls) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(ls);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy lịch sử in của một vé cụ thể
     */
    public List<LichSuInVe> findByMaVe(String maVe) {
        try {
            em.clear();
            return em.createQuery("SELECT ls FROM LichSuInVe ls WHERE ls.ve.maVe = :maVe ORDER BY ls.thoiGianIn DESC", LichSuInVe.class)
                    .setParameter("maVe", maVe)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Đếm số lần đã in của một vé (không tính lần in tự động sau khi bán)
     */
    public int countPrintTimes(String maVe) {
        try {
            em.clear();
            // ⚡ Chỉ đếm những lần in không phải tự động sau khi bán
            Long count = em.createQuery("SELECT COUNT(ls) FROM LichSuInVe ls WHERE ls.ve.maVe = :maVe AND ls.loaiIn != 'In sau bán (Tự động)'", Long.class)
                    .setParameter("maVe", maVe)
                    .getSingleResult();
            return count.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Lấy lần in gần nhất của một vé
     */
    public LichSuInVe findLastPrint(String maVe) {
        try {
            em.clear();
            List<LichSuInVe> list = em.createQuery("SELECT ls FROM LichSuInVe ls WHERE ls.ve.maVe = :maVe ORDER BY ls.thoiGianIn DESC", LichSuInVe.class)
                    .setParameter("maVe", maVe)
                    .setMaxResults(1)
                    .getResultList();
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
