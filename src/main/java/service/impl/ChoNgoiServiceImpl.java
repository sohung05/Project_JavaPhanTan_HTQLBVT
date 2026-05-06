package service.impl;

import entity.ChoNgoi;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import service.IChoNgoiService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ChoNgoiServiceImpl extends UnicastRemoteObject implements IChoNgoiService {
    private EntityManager em;

    public ChoNgoiServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public List<ChoNgoi> getChoNgoiByMaToa(String maToa) throws RemoteException {
        return em.createQuery("SELECT cn FROM ChoNgoi cn WHERE cn.toa.maToa = :maToa", ChoNgoi.class)
                .setParameter("maToa", maToa)
                .getResultList();
    }

    @Override
    public boolean kiemTraChoNgoiDaDat(String maChoNgoi, String maLichTrinh) throws RemoteException {
        Long count = em.createQuery("SELECT COUNT(v) FROM Ve v WHERE v.choNgoi.maChoNgoi = :maChoNgoi AND v.lichTrinh.maLichTrinh = :maLichTrinh AND v.trangThai = true", Long.class)
                .setParameter("maChoNgoi", maChoNgoi)
                .setParameter("maLichTrinh", maLichTrinh)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public List<ChoNgoi> getChoNgoiTrong(String maToa, String maLichTrinh) throws RemoteException {
        // Lấy tất cả chỗ ngồi của toa, trừ những chỗ đã có vé đặt cho lịch trình này
        return em.createQuery("SELECT cn FROM ChoNgoi cn WHERE cn.toa.maToa = :maToa AND cn.maChoNgoi NOT IN " +
                             "(SELECT v.choNgoi.maChoNgoi FROM Ve v WHERE v.lichTrinh.maLichTrinh = :maLichTrinh AND v.trangThai = true)", ChoNgoi.class)
                .setParameter("maToa", maToa)
                .setParameter("maLichTrinh", maLichTrinh)
                .getResultList();
    }

    @Override
    public List<ChoNgoi> findAll() throws RemoteException {
        return em.createQuery("SELECT cn FROM ChoNgoi cn", ChoNgoi.class).getResultList();
    }

    @Override
    public ChoNgoi findByMaChoNgoi(String maChoNgoi) throws RemoteException {
        return em.find(ChoNgoi.class, maChoNgoi);
    }

    @Override
    public boolean insert(ChoNgoi cn) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(cn);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(ChoNgoi cn) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(cn);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String maChoNgoi) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ChoNgoi cn = em.find(ChoNgoi.class, maChoNgoi);
            if (cn != null) em.remove(cn);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
}
