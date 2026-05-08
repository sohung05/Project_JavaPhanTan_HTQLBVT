package service.impl;

import entity.DonTreoDat;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import service.IDonTreoService;
import utils.EntityManagerFactoryUtil;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DonTreoServiceImpl extends UnicastRemoteObject implements IDonTreoService {
    private EntityManagerFactory emf;

    public DonTreoServiceImpl() throws RemoteException {
        super();
        this.emf = EntityManagerFactoryUtil.getEntityManagerFactory();
    }

    @Override
    public void themDonTreo(DonTreoDat don) throws RemoteException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (don.getMaDonTreo() == null || don.getMaDonTreo().isEmpty()) {
                String timestamp = java.time.format.DateTimeFormatter.ofPattern("yyMMddHHmmss").format(LocalDateTime.now());
                don.setMaDonTreo("DT" + timestamp);
            }
            em.merge(don);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RemoteException("Lỗi khi thêm đơn treo: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public List<DonTreoDat> layDanhSachDonTreo() throws RemoteException {
        xoaDonHetHan();
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT d FROM DonTreoDat d", DonTreoDat.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public DonTreoDat layDonTreo(String maDon) throws RemoteException {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(DonTreoDat.class, maDon);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean xoaDonTreo(String maDon) throws RemoteException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            // ⚡ XỬ LÝ TRỰC TIẾP DƯỚI DATABASE (Dùng Native SQL cho chắc chắn):
            // 1. Xóa các vé tạm bằng lệnh SQL thuần (Bỏ qua rắc rối về Entity lồng nhau)
            em.createNativeQuery("DELETE FROM ThongTinVeTam WHERE maDonTreo = ?")
              .setParameter(1, maDon)
              .executeUpdate();
            
            // 2. Tìm và xóa đơn treo chính
            DonTreoDat don = em.find(DonTreoDat.class, maDon);
            if (don != null) {
                em.remove(don);
            }
            
            tx.commit();
            System.out.println("✅ SERVER: Đã xóa sạch DATA cho đơn: " + maDon);
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            // In lỗi ra console server để debug
            System.err.println("❌ LỖI SERVER KHI XÓA ĐƠN: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public List<DonTreoDat> layDonTreoTheoCCCD(String cccd) throws RemoteException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<DonTreoDat> query = em.createQuery(
                "SELECT d FROM DonTreoDat d WHERE d.cccdNguoiDat LIKE :cccd", DonTreoDat.class);
            query.setParameter("cccd", "%" + cccd + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<DonTreoDat> layDonTreoTheoSDT(String sdt) throws RemoteException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<DonTreoDat> query = em.createQuery(
                "SELECT d FROM DonTreoDat d WHERE d.sdtNguoiDat LIKE :sdt", DonTreoDat.class);
            query.setParameter("sdt", "%" + sdt + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void xoaDonHetHan() throws RemoteException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            LocalDateTime limit = LocalDateTime.now().minusMinutes(15);
            List<DonTreoDat> hetHan = em.createQuery(
                "SELECT d FROM DonTreoDat d WHERE d.ngayLap < :limit", DonTreoDat.class)
                .setParameter("limit", limit)
                .getResultList();
            
            for (DonTreoDat d : hetHan) {
                // Xóa vé tạm trước khi xóa đơn để tránh lỗi ràng buộc
                em.createQuery("DELETE FROM ThongTinVeTam v WHERE v.donTreoDat.maDonTreo = :maDon")
                  .setParameter("maDon", d.getMaDonTreo())
                  .executeUpdate();
                em.remove(d);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public List<String> layDanhSachMaGheDangTreo(String maLichTrinh, String maGaDi, String maGaDen) throws RemoteException {
        xoaDonHetHan();
        EntityManager em = emf.createEntityManager();
        try {
            String sql = """
                SELECT v.maChoNgoi
                FROM ThongTinVeTam v
                JOIN DonTreoDat d ON v.maDonTreo = d.maDonTreo
                JOIN LichTrinh lt ON v.maLichTrinh = lt.maLichTrinh
                JOIN BangGioGa bg_treo_di ON v.maGaDi = bg_treo_di.maGa AND lt.maTuyen = bg_treo_di.maTuyen
                JOIN BangGioGa bg_treo_den ON v.maGaDen = bg_treo_den.maGa AND lt.maTuyen = bg_treo_den.maTuyen
                JOIN BangGioGa bg_cur_di ON ? = bg_cur_di.maGa AND lt.maTuyen = bg_cur_di.maTuyen
                JOIN BangGioGa bg_cur_den ON ? = bg_cur_den.maGa AND lt.maTuyen = bg_cur_den.maTuyen
                WHERE v.maLichTrinh = ? 
                  AND bg_treo_di.stt < bg_cur_den.stt
                  AND bg_treo_den.stt > bg_cur_di.stt
            """;
            @SuppressWarnings("unchecked")
            List<String> results = em.createNativeQuery(sql)
                    .setParameter(1, maGaDi)
                    .setParameter(2, maGaDen)
                    .setParameter(3, maLichTrinh)
                    .getResultList();
            return results;
        } finally {
            em.close();
        }
    }
}
