package dao;

import entity.Ga;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.EntityManagerFactoryUtil;

import java.util.ArrayList;
import java.util.List;

public class Ga_DAO {
    private EntityManager em;

    public Ga_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<Ga> findAll() {
        try {
            return em.createQuery("SELECT g FROM Ga g ORDER BY g.tenGa", Ga.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Ga findByMaGa(String maGa) {
        try {
            return em.find(Ga.class, maGa);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Ga> findByTenGa(String tenGa) {
        try {
            return em.createQuery("SELECT g FROM Ga g WHERE g.tenGa LIKE :tenGa ORDER BY g.tenGa", Ga.class)
                    .setParameter("tenGa", "%" + tenGa + "%")
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean insert(Ga ga) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(ga);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Ga ga) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(ga);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maGa) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Ga ga = em.find(Ga.class, maGa);
            if (ga != null) {
                em.remove(ga);
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
