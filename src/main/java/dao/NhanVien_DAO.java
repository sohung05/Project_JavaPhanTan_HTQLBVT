package dao;

import entity.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import utils.EntityManagerFactoryUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NhanVien_DAO {
    private EntityManager em;

    public NhanVien_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<NhanVien> getAll() {
        try {
            return em.createQuery("SELECT nv FROM NhanVien nv", NhanVien.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean them(NhanVien nv) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(nv);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean sua(NhanVien nv) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(nv);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public List<NhanVien> timKiem(String maNV, String cccd, String hoTen, String email, String sdt,
                                  String trangThai, String gioiTinh, LocalDate ngaySinh) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT nv FROM NhanVien nv WHERE 1=1");
            if (maNV != null && !maNV.isEmpty()) jpql.append(" AND nv.maNhanVien LIKE :maNV");
            if (cccd != null && !cccd.isEmpty()) jpql.append(" AND nv.CCCD LIKE :cccd");
            if (hoTen != null && !hoTen.isEmpty()) jpql.append(" AND nv.hoTen LIKE :hoTen");
            if (email != null && !email.isEmpty()) jpql.append(" AND nv.email LIKE :email");
            if (sdt != null && !sdt.isEmpty()) jpql.append(" AND nv.SDT LIKE :sdt");
            if (trangThai != null && !trangThai.isEmpty()) {
                jpql.append(" AND nv.trangThai = :trangThai");
            }
            if (gioiTinh != null && !gioiTinh.isEmpty()) jpql.append(" AND nv.gioiTinh = :gioiTinh");
            if (ngaySinh != null) jpql.append(" AND nv.ngaySinh = :ngaySinh");

            TypedQuery<NhanVien> query = em.createQuery(jpql.toString(), NhanVien.class);
            if (maNV != null && !maNV.isEmpty()) query.setParameter("maNV", "%" + maNV + "%");
            if (cccd != null && !cccd.isEmpty()) query.setParameter("cccd", "%" + cccd + "%");
            if (hoTen != null && !hoTen.isEmpty()) query.setParameter("hoTen", "%" + hoTen + "%");
            if (email != null && !email.isEmpty()) query.setParameter("email", "%" + email + "%");
            if (sdt != null && !sdt.isEmpty()) query.setParameter("sdt", "%" + sdt + "%");
            if (trangThai != null && !trangThai.isEmpty()) {
                boolean tt = trangThai.equalsIgnoreCase("Đang làm") || trangThai.equalsIgnoreCase("1");
                query.setParameter("trangThai", tt);
            }
            if (gioiTinh != null && !gioiTinh.isEmpty()) query.setParameter("gioiTinh", gioiTinh);
            if (ngaySinh != null) query.setParameter("ngaySinh", ngaySinh);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public NhanVien getById(String maNhanVien) {
        try {
            return em.find(NhanVien.class, maNhanVien);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String generateMaNhanVien(LocalDate ngayVaoLam, LocalDate ngaySinh) {
        if (ngayVaoLam == null || ngaySinh == null) return null;
        
        String prefix = "NV";
        String namVaoLam = String.valueOf(ngayVaoLam.getYear()).substring(2);
        String namSinh = String.valueOf(ngaySinh.getYear()).substring(2);
        String base = prefix + namVaoLam + namSinh;

        try {
            TypedQuery<String> query = em.createQuery("SELECT MAX(nv.maNhanVien) FROM NhanVien nv WHERE nv.maNhanVien LIKE :base", String.class);
            query.setParameter("base", base + "%");
            String maxMa = query.getSingleResult();
            
            int nextNumber = 1;
            if (maxMa != null && maxMa.length() >= 10) {
                String soThuTuStr = maxMa.substring(maxMa.length() - 4);
                nextNumber = Integer.parseInt(soThuTuStr) + 1;
            }
            return base + String.format("%04d", nextNumber);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}