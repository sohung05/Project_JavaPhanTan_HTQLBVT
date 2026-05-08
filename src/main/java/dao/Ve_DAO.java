package dao;

import entity.Ve;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import utils.EntityManagerFactoryUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ve_DAO {
    private EntityManager em;

    public Ve_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<Ve> findAll() {
        try {
            em.clear();
            return em.createQuery("SELECT v FROM Ve v ORDER BY v.thoiGianLenTau DESC", Ve.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Ve findByMaVe(String maVe) {
        try {
            em.clear();
            return em.find(Ve.class, maVe);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insert(Ve ve) {
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

    public boolean update(Ve ve) {
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

    public boolean delete(String maVe) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Ve ve = em.find(Ve.class, maVe);
            if (ve != null) {
                ve.setTrangThai(false);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Kiểm tra ghế đã đặt dựa trên chặng đường (Segments)
     */
    public boolean kiemTraGheDaDat(String maChoNgoi, String maLichTrinh, String maGaDi, String maGaDen) {
        try {
            String sql = """
                SELECT COUNT(*) 
                FROM Ve v
                JOIN LichTrinh lt ON v.maLichTrinh = lt.maLichTrinh
                JOIN BangGioGa bg_da_ban_di ON v.maGaDi = bg_da_ban_di.maGa AND lt.maTuyen = bg_da_ban_di.maTuyen
                JOIN BangGioGa bg_da_ban_den ON v.maGaDen = bg_da_ban_den.maGa AND lt.maTuyen = bg_da_ban_den.maTuyen
                JOIN BangGioGa bg_dang_chon_di ON ? = bg_dang_chon_di.maGa AND lt.maTuyen = bg_dang_chon_di.maTuyen
                JOIN BangGioGa bg_dang_chon_den ON ? = bg_dang_chon_den.maGa AND lt.maTuyen = bg_dang_chon_den.maTuyen
                WHERE v.maChoNgoi = ? 
                  AND v.maLichTrinh = ? 
                  AND v.trangThai = 1
                  AND bg_da_ban_di.stt < bg_dang_chon_den.stt
                  AND bg_da_ban_den.stt > bg_dang_chon_di.stt
            """;
            
            Number count = (Number) em.createNativeQuery(sql)
                    .setParameter(1, maGaDi)
                    .setParameter(2, maGaDen)
                    .setParameter(3, maChoNgoi)
                    .setParameter(4, maLichTrinh)
                    .getSingleResult();
            
            return count.intValue() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy danh sách ghế đã đặt cho một chặng cụ thể
     */
    public Set<String> layDanhSachGheDaDat(String maLichTrinh, String maGaDi, String maGaDen) {
        try {
            String sql = """
                SELECT v.maChoNgoi
                FROM Ve v
                JOIN LichTrinh lt ON v.maLichTrinh = lt.maLichTrinh
                JOIN BangGioGa bg_da_ban_di ON v.maGaDi = bg_da_ban_di.maGa AND lt.maTuyen = bg_da_ban_di.maTuyen
                JOIN BangGioGa bg_da_ban_den ON v.maGaDen = bg_da_ban_den.maGa AND lt.maTuyen = bg_da_ban_den.maTuyen
                JOIN BangGioGa bg_dang_chon_di ON ? = bg_dang_chon_di.maGa AND lt.maTuyen = bg_dang_chon_di.maTuyen
                JOIN BangGioGa bg_dang_chon_den ON ? = bg_dang_chon_den.maGa AND lt.maTuyen = bg_dang_chon_den.maTuyen
                WHERE v.maLichTrinh = ? 
                  AND v.trangThai = 1
                  AND bg_da_ban_di.stt < bg_dang_chon_den.stt
                  AND bg_da_ban_den.stt > bg_dang_chon_di.stt
            """;
            
            @SuppressWarnings("unchecked")
            List<String> results = em.createNativeQuery(sql)
                    .setParameter(1, maGaDi)
                    .setParameter(2, maGaDen)
                    .setParameter(3, maLichTrinh)
                    .getResultList();
            
            return new HashSet<>(results);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    public List<Ve> searchVe(String keyword) {
        try {
            em.clear();
            String jpql = "SELECT v FROM Ve v WHERE (v.maVe LIKE :keyword OR v.soCCCD LIKE :keyword) AND v.trangThai = true ORDER BY v.thoiGianLenTau DESC";
            return em.createQuery(jpql, Ve.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Ve> findByMaHoaDon(String maHoaDon) {
        try {
            em.clear();
            // Giả sử có bảng ChiTietHoaDon hoặc mối quan hệ trong HoaDon
            // Nếu dùng Native Query cho chắc chắn logic cũ:
            String sql = "SELECT v.* FROM Ve v JOIN ChiTietHoaDon cthd ON v.maVe = cthd.maVe WHERE cthd.maHoaDon = ? AND v.trangThai = 1";
            return em.createNativeQuery(sql, Ve.class)
                    .setParameter(1, maHoaDon)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
