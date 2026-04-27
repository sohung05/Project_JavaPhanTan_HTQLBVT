package dao;

import entity.LoaiVe;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.EntityManagerFactoryUtil;

import java.util.ArrayList;
import java.util.List;

public class LoaiVe_DAO {
    private EntityManager em;

    public LoaiVe_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<LoaiVe> findAll() {
        try {
            return em.createQuery("SELECT lv FROM LoaiVe lv ORDER BY lv.tenLoaiVe", LoaiVe.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public LoaiVe findByMaLoaiVe(String maLoaiVe) {
        try {
            return em.find(LoaiVe.class, maLoaiVe);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LoaiVe findByTenLoaiVe(String tenLoaiVe) {
        try {
            List<LoaiVe> list = em.createQuery("SELECT lv FROM LoaiVe lv WHERE lv.tenLoaiVe = :ten", LoaiVe.class)
                    .setParameter("ten", tenLoaiVe)
                    .getResultList();
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insert(LoaiVe lv) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(lv);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(LoaiVe lv) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(lv);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maLoaiVe) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            LoaiVe lv = em.find(LoaiVe.class, maLoaiVe);
            if (lv != null) {
                em.remove(lv);
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
