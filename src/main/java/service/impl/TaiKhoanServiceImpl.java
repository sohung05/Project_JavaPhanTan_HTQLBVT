package service.impl;

import entity.TaiKhoan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import service.ITaiKhoanService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class TaiKhoanServiceImpl extends UnicastRemoteObject implements ITaiKhoanService {
    private EntityManager em;

    public TaiKhoanServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public TaiKhoan dangNhap(String tenTaiKhoan, String matKhau) throws RemoteException {
        List<TaiKhoan> list = em.createQuery("SELECT tk FROM TaiKhoan tk WHERE tk.tenTaiKhoan = :user AND tk.matKhau = :pass", TaiKhoan.class)
                .setParameter("user", tenTaiKhoan)
                .setParameter("pass", matKhau)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Object[]> getAll() throws RemoteException {
        return em.createQuery("SELECT tk.nhanVien.maNhanVien, tk.nhanVien.hoTen, tk.tenTaiKhoan, tk.matKhau FROM TaiKhoan tk", Object[].class).getResultList();
    }

    @Override
    public boolean them(TaiKhoan tk) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(tk);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean sua(TaiKhoan tk) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tk);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Object[]> timKiem(String maNV, String tenTaiKhoan, String tenNhanVien) throws RemoteException {
        StringBuilder jpql = new StringBuilder("SELECT tk.nhanVien.maNhanVien, tk.nhanVien.hoTen, tk.tenTaiKhoan, tk.matKhau FROM TaiKhoan tk WHERE 1=1");
        if (maNV != null && !maNV.isEmpty()) jpql.append(" AND tk.nhanVien.maNhanVien LIKE :maNV");
        if (tenTaiKhoan != null && !tenTaiKhoan.isEmpty()) jpql.append(" AND tk.tenTaiKhoan LIKE :tenTK");
        if (tenNhanVien != null && !tenNhanVien.isEmpty()) jpql.append(" AND tk.nhanVien.hoTen LIKE :tenNV");

        jakarta.persistence.TypedQuery<Object[]> query = em.createQuery(jpql.toString(), Object[].class);
        if (maNV != null && !maNV.isEmpty()) query.setParameter("maNV", "%" + maNV + "%");
        if (tenTaiKhoan != null && !tenTaiKhoan.isEmpty()) query.setParameter("tenTK", "%" + tenTaiKhoan + "%");
        if (tenNhanVien != null && !tenNhanVien.isEmpty()) query.setParameter("tenNV", "%" + tenNhanVien + "%");

        return query.getResultList();
    }

    @Override
    public boolean kiemTraTonTaiTheoMaNV(String maNV) throws RemoteException {
        Long count = em.createQuery("SELECT COUNT(tk) FROM TaiKhoan tk WHERE tk.nhanVien.maNhanVien = :maNV", Long.class)
                .setParameter("maNV", maNV)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public boolean kiemTraTonTaiTheoTenTK(String tenTK) throws RemoteException {
        Long count = em.createQuery("SELECT COUNT(tk) FROM TaiKhoan tk WHERE tk.tenTaiKhoan = :tenTK", Long.class)
                .setParameter("tenTK", tenTK)
                .getSingleResult();
        return count > 0;
    }
}
