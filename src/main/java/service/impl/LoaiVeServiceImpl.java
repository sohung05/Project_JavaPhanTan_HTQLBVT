package service.impl;

import entity.LoaiVe;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import service.ILoaiVeService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class LoaiVeServiceImpl extends UnicastRemoteObject implements ILoaiVeService {
    private EntityManager em;

    public LoaiVeServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public List<LoaiVe> findAll() throws RemoteException {
        return em.createQuery("SELECT lv FROM LoaiVe lv", LoaiVe.class).getResultList();
    }

    @Override
    public LoaiVe findByMaLoaiVe(String maLoaiVe) throws RemoteException {
        return em.find(LoaiVe.class, maLoaiVe);
    }

    @Override
    public LoaiVe findByTenLoaiVe(String tenLoaiVe) throws RemoteException {
        List<LoaiVe> list = em.createQuery("SELECT lv FROM LoaiVe lv WHERE lv.tenLoaiVe = :ten", LoaiVe.class)
                .setParameter("ten", tenLoaiVe)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public boolean insert(LoaiVe lv) throws RemoteException {
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

    @Override
    public boolean update(LoaiVe lv) throws RemoteException {
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

    @Override
    public boolean delete(String maLoaiVe) throws RemoteException {
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
