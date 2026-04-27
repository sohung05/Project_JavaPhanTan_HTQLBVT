package service.impl;

import entity.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import service.INhanVienService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class NhanVienServiceImpl extends UnicastRemoteObject implements INhanVienService {
    private EntityManager em;

    public NhanVienServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public List<NhanVien> getAll() throws RemoteException {
        return em.createQuery("SELECT nv FROM NhanVien nv", NhanVien.class).getResultList();
    }

    @Override
    public boolean them(NhanVien nv) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(nv);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean sua(NhanVien nv) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(nv);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public NhanVien findById(String maNV) throws RemoteException {
        return em.find(NhanVien.class, maNV);
    }
}
