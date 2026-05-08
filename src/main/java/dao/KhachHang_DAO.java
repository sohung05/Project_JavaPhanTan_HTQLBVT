package dao;

import entity.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import utils.EntityManagerFactoryUtil;

import java.util.ArrayList;
import java.util.List;

public class KhachHang_DAO {
    private EntityManager em;

    public KhachHang_DAO() {
        // Lấy EntityManager từ tiện ích đã tạo
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<KhachHang> getAll() {
        try {
            return em.createQuery("SELECT kh FROM KhachHang kh", KhachHang.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<KhachHang> getAllKhachHangAndHanhKhach() {
        try {
            // Lấy danh sách khách hàng chính thức từ bảng KhachHang
            List<KhachHang> dsKH = em.createQuery("SELECT kh FROM KhachHang kh", KhachHang.class).getResultList();
            
            // Tìm các hành khách trong bảng Ve mà chưa có trong bảng KhachHang (dựa trên CCCD)
            // Lưu ý: Do HanhKhach trong Ve chỉ có Tên và CCCD nên các trường khác sẽ để trống/mặc định
            List<Object[]> dsHanhKhachLa = em.createQuery(
                "SELECT DISTINCT v.tenKhachHang, v.soCCCD FROM Ve v " +
                "WHERE v.soCCCD NOT IN (SELECT kh.CCCD FROM KhachHang kh) " +
                "AND v.soCCCD IS NOT NULL", Object[].class).getResultList();
            
            for (Object[] row : dsHanhKhachLa) {
                String ten = (String) row[0];
                String cccd = (String) row[1];
                // Tạo một đối tượng KhachHang tạm thời để hiển thị
                KhachHang khTam = new KhachHang("PASSENGER", cccd, ten, "", "", "Khách vãng lai");
                dsKH.add(khTam);
            }
            
            return dsKH;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean them(KhachHang kh) {
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

    public boolean sua(KhachHang kh) {
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

    public boolean exists(String maKH) {
        try {
            return em.find(KhachHang.class, maKH) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public KhachHang findByCCCD(String cccd) {
        try {
            TypedQuery<KhachHang> query = em.createQuery("SELECT kh FROM KhachHang kh WHERE kh.CCCD = :cccd", KhachHang.class);
            query.setParameter("cccd", cccd);
            List<KhachHang> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<KhachHang> timKiem(String cccd, String hoTen, String email, String sdt, String doiTuong) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}