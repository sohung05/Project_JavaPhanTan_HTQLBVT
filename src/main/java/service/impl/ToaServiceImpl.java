package service.impl;

import entity.Toa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import service.IToaService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ToaServiceImpl extends UnicastRemoteObject implements IToaService {
    private EntityManager em;

    public ToaServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public List<Toa> getToaBySoHieuTau(String soHieuTau) throws RemoteException {
        return em.createQuery("SELECT t FROM Toa t WHERE t.chuyenTau.soHieuTau = :soHieu", Toa.class)
                .setParameter("soHieu", soHieuTau)
                .getResultList();
    }

    @Override
    public List<Toa> findAll() throws RemoteException {
        return em.createQuery("SELECT t FROM Toa t", Toa.class).getResultList();
    }

    @Override
    public Toa findByMaToa(String maToa) throws RemoteException {
        return em.find(Toa.class, maToa);
    }

    @Override
    public boolean insert(Toa toa) throws RemoteException {
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

    @Override
    public boolean update(Toa toa) throws RemoteException {
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

    @Override
    public boolean delete(String maToa) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Toa toa = em.find(Toa.class, maToa);
            if (toa != null) em.remove(toa);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
}
