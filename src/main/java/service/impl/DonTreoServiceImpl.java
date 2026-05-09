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
            System.out.println("🚀 SERVER: Đang thực hiện xóa cứng đơn treo: " + maDon);
            
            // 1. Xóa các vé tạm trước
            int v = em.createNativeQuery("DELETE FROM ThongTinVeTam WHERE maDonTreo = :maDon")
                .setParameter("maDon", maDon)
                .executeUpdate();
            
            // 2. Xóa đơn treo
            int d = em.createNativeQuery("DELETE FROM DonTreoDat WHERE maDonTreo = :maDon")
                .setParameter("maDon", maDon)
                .executeUpdate();
            
            tx.commit();
            System.out.println("✅ SERVER: Kết quả xóa đơn [" + maDon + "]: " + d + " đơn, " + v + " vé tạm.");
            return d > 0;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("❌ SERVER LỖI KHI XÓA ĐƠN [" + maDon + "]: " + e.getMessage());
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
    public void xoaDonHetHan() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // ⚡ Dùng Native SQL để xóa sạch và nhanh
            LocalDateTime limit = LocalDateTime.now().minusMinutes(15);
            
            // 1. Tìm các mã đơn quá hạn
            List<String> expiredMaDons = em.createNativeQuery(
                "SELECT maDonTreo FROM DonTreoDat WHERE ngayLap < :limit")
                .setParameter("limit", limit)
                .getResultList();
            
            if (!expiredMaDons.isEmpty()) {
                for (String maDon : expiredMaDons) {
                    em.createNativeQuery("DELETE FROM ThongTinVeTam WHERE maDonTreo = :maDon")
                        .setParameter("maDon", maDon)
                        .executeUpdate();
                    em.createNativeQuery("DELETE FROM DonTreoDat WHERE maDonTreo = :maDon")
                        .setParameter("maDon", maDon)
                        .executeUpdate();
                }
                System.out.println("⏰ SERVER: Đã dọn dẹp " + expiredMaDons.size() + " đơn treo quá hạn.");
            }
            
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.err.println("❌ LỖI KHI DỌN DẸP ĐƠN QUÁ HẠN: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public List<String> layDanhSachMaGheDangTreo(String maLichTrinh, String maGaDi, String maGaDen) throws RemoteException {
        xoaDonHetHan();
        EntityManager em = emf.createEntityManager();
        try {
            em.clear(); // ⚡ CLEAR CACHE để lấy dữ liệu mới nhất từ DB
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
