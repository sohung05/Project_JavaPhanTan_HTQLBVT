package dao;

import entity.LichTrinh;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import utils.EntityManagerFactoryUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LichTrinh_DAO {
    private EntityManager em;

    public LichTrinh_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<LichTrinh> timLichTrinh(String tenGaDi, String tenGaDen, LocalDate ngayDi) {
        try {
            // Sử dụng Native Query để giữ nguyên logic phức tạp của SQL gốc (hỗ trợ ga trung gian)
            String sql = "SELECT DISTINCT lt.* " +
                         "FROM LichTrinh lt " +
                         "JOIN BangGioGa bggDi ON bggDi.maLichTrinh = lt.maLichTrinh " +
                         "JOIN Ga gaDiThucTe ON gaDiThucTe.maGa = bggDi.maGa " +
                         "JOIN BangGioGa bggDen ON bggDen.maLichTrinh = lt.maLichTrinh " +
                         "JOIN Ga gaDenThucTe ON gaDenThucTe.maGa = bggDen.maGa " +
                         "WHERE gaDiThucTe.tenGa LIKE ? " +
                         "  AND gaDenThucTe.tenGa LIKE ? " +
                         "  AND CAST(lt.gioKhoiHanh AS DATE) = ? " +
                         "  AND lt.trangThai = 1 " +
                         "  AND bggDi.thuTuGa < bggDen.thuTuGa " +
                         "ORDER BY bggDi.gioDen";

            Query query = em.createNativeQuery(sql, LichTrinh.class);
            query.setParameter(1, "%" + tenGaDi + "%");
            query.setParameter(2, "%" + tenGaDen + "%");
            query.setParameter(3, java.sql.Date.valueOf(ngayDi));

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<LichTrinh> findAll() {
        try {
            return em.createQuery("SELECT lt FROM LichTrinh lt ORDER BY lt.gioKhoiHanh DESC", LichTrinh.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public LichTrinh findByMaLichTrinh(String maLichTrinh) {
        try {
            return em.find(LichTrinh.class, maLichTrinh);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insert(LichTrinh lt) {
        EntityTransaction tx = em.getTransaction();
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

    public boolean update(LichTrinh lt) {
        EntityTransaction tx = em.getTransaction();
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

    public boolean delete(String maLichTrinh) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            LichTrinh lt = em.find(LichTrinh.class, maLichTrinh);
            if (lt != null) {
                lt.setTrangThai(false); // Soft delete
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
