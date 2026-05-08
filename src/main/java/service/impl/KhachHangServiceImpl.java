package service.impl;

import entity.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import service.IKhachHangService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class KhachHangServiceImpl extends UnicastRemoteObject implements IKhachHangService {
    private EntityManager em;

    public KhachHangServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public List<KhachHang> getAll() throws RemoteException {
        return em.createQuery("SELECT kh FROM KhachHang kh", KhachHang.class).getResultList();
    }

    @Override
    public boolean them(KhachHang kh) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(kh);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean sua(KhachHang kh) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(kh);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean exists(String maKH) throws RemoteException {
        return em.find(KhachHang.class, maKH) != null;
    }

    @Override
    public KhachHang findByCCCD(String cccd) throws RemoteException {
        TypedQuery<KhachHang> query = em.createQuery("SELECT kh FROM KhachHang kh WHERE kh.CCCD = :cccd", KhachHang.class);
        query.setParameter("cccd", cccd);
        List<KhachHang> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<KhachHang> timKiem(String cccd, String hoTen, String email, String sdt, String doiTuong) throws RemoteException {
        StringBuilder jpql = new StringBuilder("SELECT kh FROM KhachHang kh WHERE 1=1");
        if (cccd != null && !cccd.isEmpty()) jpql.append(" AND kh.CCCD LIKE :cccd");
        if (hoTen != null && !hoTen.isEmpty()) jpql.append(" AND kh.hoTen LIKE :hoTen");
        if (email != null && !email.isEmpty()) jpql.append(" AND kh.email LIKE :email");
        if (sdt != null && !sdt.isEmpty()) jpql.append(" AND kh.SDT LIKE :sdt");
        if (doiTuong != null && !doiTuong.isEmpty()) jpql.append(" AND kh.doiTuong LIKE :doiTuong");

        TypedQuery<KhachHang> query = em.createQuery(jpql.toString(), KhachHang.class);
        if (cccd != null && !cccd.isEmpty()) query.setParameter("cccd", "%" + cccd + "%");
        if (hoTen != null && !hoTen.isEmpty()) query.setParameter("hoTen", "%" + hoTen + "%");
        if (email != null && !email.isEmpty()) query.setParameter("email", "%" + email + "%");
        if (sdt != null && !sdt.isEmpty()) query.setParameter("sdt", "%" + sdt + "%");
        if (doiTuong != null && !doiTuong.isEmpty()) query.setParameter("doiTuong", "%" + doiTuong + "%");

        return query.getResultList();
    }
    @Override
    public List<KhachHang> getAllKhachHangAndHanhKhach() throws RemoteException {
        // Lấy danh sách khách hàng chính thức từ bảng KhachHang
        List<KhachHang> dsKH = em.createQuery("SELECT kh FROM KhachHang kh", KhachHang.class).getResultList();
        
        // Tìm các hành khách trong bảng Ve mà chưa có trong bảng KhachHang (dựa trên CCCD)
        List<Object[]> dsHanhKhachLa = em.createQuery(
            "SELECT DISTINCT v.tenKhachHang, v.soCCCD FROM Ve v " +
            "WHERE v.soCCCD NOT IN (SELECT kh.CCCD FROM KhachHang kh) " +
            "AND v.soCCCD IS NOT NULL", Object[].class).getResultList();
        
        for (Object[] row : dsHanhKhachLa) {
            String ten = (String) row[0];
            String cccd = (String) row[1];
            // Tạo một đối tượng KhachHang tạm thời (Passenger) để hiển thị lên bảng
            KhachHang khTam = new KhachHang("PASSENGER", cccd, ten, "", "", "Hành khách");
            dsKH.add(khTam);
        }
        
        return dsKH;
    }
}
