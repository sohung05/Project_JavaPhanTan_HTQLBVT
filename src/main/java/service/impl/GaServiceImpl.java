package service.impl;

import entity.Ga;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import service.IGaService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class GaServiceImpl extends UnicastRemoteObject implements IGaService {
    private EntityManager em;

    public GaServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public List<Ga> findAll() throws RemoteException {
        return em.createQuery("SELECT g FROM Ga g", Ga.class).getResultList();
    }

    @Override
    public Ga findByMaGa(String maGa) throws RemoteException {
        return em.find(Ga.class, maGa);
    }

    @Override
    public List<Ga> findByTenGa(String tenGa) throws RemoteException {
        return em.createQuery("SELECT g FROM Ga g WHERE g.tenGa LIKE :ten", Ga.class)
                .setParameter("ten", "%" + tenGa + "%")
                .getResultList();
    }

    @Override
    public boolean insert(Ga ga) throws RemoteException {
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

    @Override
    public boolean update(Ga ga) throws RemoteException {
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

    @Override
    public boolean delete(String maGa) throws RemoteException {
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
