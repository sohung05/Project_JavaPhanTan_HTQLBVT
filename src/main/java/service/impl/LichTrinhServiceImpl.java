package service.impl;

import entity.LichTrinh;
import jakarta.persistence.EntityManager;
import service.ILichTrinhService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class LichTrinhServiceImpl extends UnicastRemoteObject implements ILichTrinhService {
    private EntityManager em;

    public LichTrinhServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public List<LichTrinh> getAll() throws RemoteException {
        return em.createQuery("SELECT lt FROM LichTrinh lt", LichTrinh.class).getResultList();
    }

    @Override
    public LichTrinh findById(String maLT) throws RemoteException {
        return em.find(LichTrinh.class, maLT);
    }

    @Override
    public List<LichTrinh> timLichTrinh(String tenGaDi, String tenGaDen, java.time.LocalDate ngayDi) throws RemoteException {
        System.out.println("🔍 Đang tìm lịch trình: " + tenGaDi + " -> " + tenGaDen + " ngày " + ngayDi);
        try {
            String sql = "SELECT lt.* " +
                         "FROM LichTrinh lt " +
                         "JOIN BangGioGa bg1 ON lt.maTuyen = bg1.maTuyen " +
                         "JOIN Ga g1 ON bg1.maGa = g1.maGa " +
                         "JOIN BangGioGa bg2 ON lt.maTuyen = bg2.maTuyen " +
                         "JOIN Ga g2 ON bg2.maGa = g2.maGa " +
                         "WHERE g1.tenGa LIKE ? " +
                         "  AND g2.tenGa LIKE ? " +
                         "  AND bg1.stt < bg2.stt " +
                         "  AND CAST(lt.gioKhoiHanh AS DATE) = ? " +
                         "  AND lt.trangThai = 1 " +
                         "ORDER BY lt.gioKhoiHanh";

            jakarta.persistence.Query query = em.createNativeQuery(sql, LichTrinh.class);
            query.setParameter(1, "%" + tenGaDi + "%");
            query.setParameter(2, "%" + tenGaDen + "%");
            query.setParameter(3, java.sql.Date.valueOf(ngayDi));

            List<LichTrinh> results = query.getResultList();
            System.out.println("✅ Tìm thấy " + results.size() + " lịch trình phù hợp.");
            return results;
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tìm lịch trình: " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    @Override
    public boolean insert(LichTrinh lt) throws RemoteException {
        jakarta.persistence.EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(lt);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(LichTrinh lt) throws RemoteException {
        jakarta.persistence.EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(lt);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String maLT) throws RemoteException {
        jakarta.persistence.EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            LichTrinh lt = em.find(LichTrinh.class, maLT);
            if (lt != null) {
                lt.setTrangThai(false);
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
