package service.impl;

import entity.KhuyenMai;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import service.IKhuyenMaiService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class KhuyenMaiServiceImpl extends UnicastRemoteObject implements IKhuyenMaiService {
    private EntityManager em;

    public KhuyenMaiServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public List<KhuyenMai> getTatCaKhuyenMaiHoaDon() throws RemoteException {
        List<Object[]> list = em.createQuery("SELECT km, ct.chietKhau, ct.dieuKien FROM KhuyenMai km JOIN ChiTietKhuyenMai ct ON ct.khuyenMai = km WHERE km.loaiKhuyenMai = 'KMHD'", Object[].class).getResultList();
        List<KhuyenMai> res = new java.util.ArrayList<>();
        for (Object[] obj : list) {
            KhuyenMai km = (KhuyenMai) obj[0];
            km.setChietKhau(((Number) obj[1]).doubleValue());
            km.setDoiTuongApDung((String) obj[2]);
            res.add(km);
        }
        return res;
    }

    @Override
    public boolean themKhuyenMaiHoaDon(KhuyenMai km, String soVeStr, double chietKhau) throws RemoteException {
        km.setLoaiKhuyenMai("KMHD");
        km.setDoiTuongApDung(soVeStr);
        km.setChietKhau(chietKhau);
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(km);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean tamNgungTrangThai(String maKhuyenMai, boolean trangThai) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            KhuyenMai km = em.find(KhuyenMai.class, maKhuyenMai);
            if (km != null) km.setTrangThai(trangThai);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<KhuyenMai> locKhuyenMaiHoaDon(String keyword, LocalDate startDate, LocalDate endDate) throws RemoteException {
        StringBuilder jpql = new StringBuilder("SELECT km FROM KhuyenMai km WHERE km.loaiKhuyenMai = 'KMHD'");
        if (keyword != null && !keyword.isEmpty()) jpql.append(" AND (km.maKhuyenMai LIKE :kw OR km.tenKhuyenMai LIKE :kw)");
        if (startDate != null) jpql.append(" AND km.ngayBatDau >= :start");
        if (endDate != null) jpql.append(" AND km.ngayKetThuc <= :end");

        jakarta.persistence.TypedQuery<KhuyenMai> query = em.createQuery(jpql.toString(), KhuyenMai.class);
        if (keyword != null && !keyword.isEmpty()) query.setParameter("kw", "%" + keyword + "%");
        if (startDate != null) query.setParameter("start", startDate);
        if (endDate != null) query.setParameter("end", endDate);

        return query.getResultList();
    }

    @Override
    public boolean capNhatKhuyenMaiHoaDon(String maKMCu, String maKMMoi, String ten, Date start, Date end, double chietKhau, String dieuKien) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            KhuyenMai km = em.find(KhuyenMai.class, maKMCu);
            if (km != null) {
                km.setTenKhuyenMai(ten);
                km.setThoiGianBatDau(new java.sql.Timestamp(start.getTime()).toLocalDateTime());
                km.setThoiGianKetThuc(new java.sql.Timestamp(end.getTime()).toLocalDateTime());
                km.setChietKhau(chietKhau);
                km.setDoiTuongApDung(dieuKien);
                em.merge(km);
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
    public double getChietKhauHieuLucTheoSoVe(int soLuongVe) throws RemoteException {
        LocalDateTime now = LocalDateTime.now();
        List<Double> results = em.createQuery("SELECT ct.chietKhau FROM ChiTietKhuyenMai ct WHERE ct.khuyenMai.loaiKhuyenMai = 'KMHD' AND ct.khuyenMai.trangThai = true AND :now BETWEEN ct.khuyenMai.thoiGianBatDau AND ct.khuyenMai.thoiGianKetThuc AND ct.khuyenMai.doiTuongApDung = :qty ORDER BY ct.chietKhau DESC", Double.class)
                .setParameter("now", now)
                .setParameter("qty", soLuongVe)
                .getResultList();
        return results.isEmpty() ? 0 : results.get(0);
    }

    @Override
    public List<KhuyenMai> getTatCaKhuyenMaiDoiTuong() throws RemoteException {
        List<Object[]> list = em.createQuery("SELECT km, ct.chietKhau, ct.dieuKien FROM KhuyenMai km JOIN ChiTietKhuyenMai ct ON ct.khuyenMai = km WHERE km.loaiKhuyenMai = 'KMKH'", Object[].class).getResultList();
        List<KhuyenMai> res = new java.util.ArrayList<>();
        for (Object[] obj : list) {
            KhuyenMai km = (KhuyenMai) obj[0];
            km.setChietKhau(((Number) obj[1]).doubleValue());
            km.setDoiTuongApDung((String) obj[2]);
            res.add(km);
        }
        return res;
    }

    @Override
    public boolean themKhuyenMaiDoiTuong(KhuyenMai km, String doiTuong, double chietKhau) throws RemoteException {
        km.setLoaiKhuyenMai("KMKH");
        km.setDoiTuongApDung(doiTuong);
        km.setChietKhau(chietKhau);
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(km);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean capNhatKhuyenMaiDoiTuong(String maKMCu, String maKMMoi, String ten, Date start, Date end, double chietKhau, String doiTuong) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            KhuyenMai km = em.find(KhuyenMai.class, maKMCu);
            if (km != null) {
                km.setTenKhuyenMai(ten);
                km.setThoiGianBatDau(new java.sql.Timestamp(start.getTime()).toLocalDateTime());
                km.setThoiGianKetThuc(new java.sql.Timestamp(end.getTime()).toLocalDateTime());
                km.setChietKhau(chietKhau);
                km.setDoiTuongApDung(doiTuong);
                em.merge(km);
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
    public double getChietKhauHieuLucTheoDoiTuong(String doiTuong) throws RemoteException {
        LocalDateTime now = LocalDateTime.now();
        List<Double> results = em.createQuery("SELECT ct.chietKhau FROM ChiTietKhuyenMai ct WHERE ct.khuyenMai.loaiKhuyenMai = 'KMKH' AND ct.khuyenMai.trangThai = true AND :now BETWEEN ct.khuyenMai.thoiGianBatDau AND ct.khuyenMai.thoiGianKetThuc AND ct.khuyenMai.doiTuongApDung = :dt ORDER BY ct.chietKhau DESC", Double.class)
                .setParameter("now", now)
                .setParameter("dt", doiTuong)
                .getResultList();
        return results.isEmpty() ? 0 : results.get(0);
    }
}
