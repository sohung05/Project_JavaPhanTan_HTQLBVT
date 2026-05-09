package service.impl;

import entity.Ve;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import service.IVeService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VeServiceImpl extends UnicastRemoteObject implements IVeService {
    private EntityManager em;

    public VeServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public List<Ve> findAll() throws RemoteException {
        return em.createQuery("SELECT v FROM Ve v", Ve.class).getResultList();
    }

    @Override
    public Ve findByMaVe(String maVe) throws RemoteException {
        return em.find(Ve.class, maVe);
    }

    @Override
    public boolean insert(Ve ve) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(ve);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Ve ve) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(ve);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String maVe) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Ve ve = em.find(Ve.class, maVe);
            if (ve != null) {
                ve.setTrangThai(false); // Soft delete
                em.merge(ve);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean kiemTraGheDaDat(String maChoNgoi, String maLichTrinh) throws RemoteException {
        Long count = em.createQuery("SELECT COUNT(v) FROM Ve v WHERE v.choNgoi.maChoNgoi = :maChoNgoi AND v.lichTrinh.maLichTrinh = :maLichTrinh AND v.trangThai = true", Long.class)
                .setParameter("maChoNgoi", maChoNgoi)
                .setParameter("maLichTrinh", maLichTrinh)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public Set<String> layDanhSachGheDaDat(String maLichTrinh) throws RemoteException {
        List<String> list = em.createQuery("SELECT v.choNgoi.maChoNgoi FROM Ve v WHERE v.lichTrinh.maLichTrinh = :maLichTrinh AND v.trangThai = true", String.class)
                .setParameter("maLichTrinh", maLichTrinh)
                .getResultList();
        return new HashSet<>(list);
    }

    @Override
    public List<Ve> searchVe(String keyword) throws RemoteException {
        return em.createQuery("SELECT v FROM Ve v WHERE v.maVe LIKE :kw OR v.tenKhachHang LIKE :kw OR v.soCCCD LIKE :kw", Ve.class)
                .setParameter("kw", "%" + keyword + "%")
                .getResultList();
    }

    @Override
    public List<Ve> findByMaHoaDon(String maHoaDon) throws RemoteException {
        // Sử dụng LEFT JOIN FETCH để đảm bảo nếu một số trường thông tin bị thiếu (NULL) thì vẫn trả về danh sách vé
        return em.createQuery(
                "SELECT ct.ve FROM ChiTietHoaDon ct " +
                "LEFT JOIN FETCH ct.ve.lichTrinh " +
                "LEFT JOIN FETCH ct.ve.toa " +
                "LEFT JOIN FETCH ct.ve.choNgoi " +
                "LEFT JOIN FETCH ct.ve.loaiVe " +
                "WHERE ct.hoaDon.maHoaDon = :maHD AND ct.ve.trangThai = true", Ve.class)
                .setParameter("maHD", maHoaDon)
                .getResultList();
    }
}
