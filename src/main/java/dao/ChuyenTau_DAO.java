package dao;

import entity.ChuyenTau;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.EntityManagerFactoryUtil;

import java.util.ArrayList;
import java.util.List;

public class ChuyenTau_DAO {
    private EntityManager em;

    public ChuyenTau_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<ChuyenTau> findAll() {
        try {
            return em.createQuery("SELECT ct FROM ChuyenTau ct ORDER BY ct.soHieuTau", ChuyenTau.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ChuyenTau findBySoHieuTau(String soHieuTau) {
        try {
            return em.find(ChuyenTau.class, soHieuTau);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insert(ChuyenTau ct) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(ct);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(ChuyenTau ct) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(ct);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String soHieuTau) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ChuyenTau ct = em.find(ChuyenTau.class, soHieuTau);
            if (ct != null) {
                em.remove(ct);
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
