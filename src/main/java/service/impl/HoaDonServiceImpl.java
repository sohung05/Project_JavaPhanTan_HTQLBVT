package service.impl;

import entity.HoaDon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import service.IHoaDonService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class HoaDonServiceImpl extends UnicastRemoteObject implements IHoaDonService {
    private EntityManager em;

    public HoaDonServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public List<HoaDon> getAll() throws RemoteException {
        return em.createQuery("SELECT hd FROM HoaDon hd", HoaDon.class).getResultList();
    }

    @Override
    public boolean them(HoaDon hd) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(hd);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public HoaDon findById(String maHD) throws RemoteException {
        return em.find(HoaDon.class, maHD);
    }
}
