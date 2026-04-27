package dao;

import entity.Tuyen;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.EntityManagerFactoryUtil;

import java.util.ArrayList;
import java.util.List;

public class Tuyen_DAO {
    private EntityManager em;

    public Tuyen_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<Tuyen> findAll() {
        try {
            return em.createQuery("SELECT t FROM Tuyen t ORDER BY t.tenTuyen", Tuyen.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Tuyen findByMaTuyen(String maTuyen) {
        try {
            return em.find(Tuyen.class, maTuyen);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insert(Tuyen tuyen) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(tuyen);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Tuyen tuyen) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tuyen);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maTuyen) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Tuyen tuyen = em.find(Tuyen.class, maTuyen);
            if (tuyen != null) {
                em.remove(tuyen);
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
