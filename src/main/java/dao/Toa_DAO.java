package dao;

import entity.Toa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.EntityManagerFactoryUtil;

import java.util.ArrayList;
import java.util.List;

public class Toa_DAO {
    private EntityManager em;

    public Toa_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<Toa> getToaBySoHieuTau(String soHieuTau) {
        try {
            return em.createQuery("SELECT t FROM Toa t WHERE t.chuyenTau.soHieuTau = :soHieuTau ORDER BY t.soToa", Toa.class)
                    .setParameter("soHieuTau", soHieuTau)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Toa> findAll() {
        try {
            return em.createQuery("SELECT t FROM Toa t ORDER BY t.chuyenTau.soHieuTau, t.soToa", Toa.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Toa findByMaToa(String maToa) {
        try {
            return em.find(Toa.class, maToa);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insert(Toa toa) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(toa);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Toa toa) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(toa);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maToa) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Toa toa = em.find(Toa.class, maToa);
            if (toa != null) {
                em.remove(toa);
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
