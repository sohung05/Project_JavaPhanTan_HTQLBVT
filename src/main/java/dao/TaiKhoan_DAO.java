package dao;

import entity.TaiKhoan;
import entity.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import utils.EntityManagerFactoryUtil;

import java.util.ArrayList;
import java.util.List;

public class TaiKhoan_DAO {
    private EntityManager em;

    public TaiKhoan_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<Object[]> getAll() {
        try {
            List<TaiKhoan> ds = em.createQuery("SELECT tk FROM TaiKhoan tk JOIN FETCH tk.nhanVien", TaiKhoan.class).getResultList();
            List<Object[]> list = new ArrayList<>();
            for (TaiKhoan tk : ds) {
                list.add(new Object[]{
                    tk.getNhanVien().getMaNhanVien(),
                    tk.getNhanVien().getHoTen(),
                    tk.getTenTaiKhoan(),
                    tk.getMatKhau()
                });
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean them(TaiKhoan tk) {
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

    public boolean sua(TaiKhoan tk) {
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

    public List<Object[]> timKiem(String maNV, String tenTaiKhoan, String tenNhanVien) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT tk FROM TaiKhoan tk JOIN tk.nhanVien nv WHERE 1=1");
            if (maNV != null && !maNV.isEmpty()) jpql.append(" AND nv.maNhanVien LIKE :maNV");
            if (tenTaiKhoan != null && !tenTaiKhoan.isEmpty()) jpql.append(" AND tk.tenTaiKhoan LIKE :tenTaiKhoan");
            if (tenNhanVien != null && !tenNhanVien.isEmpty()) jpql.append(" AND nv.hoTen LIKE :tenNhanVien");

            TypedQuery<TaiKhoan> query = em.createQuery(jpql.toString(), TaiKhoan.class);
            if (maNV != null && !maNV.isEmpty()) query.setParameter("maNV", "%" + maNV + "%");
            if (tenTaiKhoan != null && !tenTaiKhoan.isEmpty()) query.setParameter("tenTaiKhoan", "%" + tenTaiKhoan + "%");
            if (tenNhanVien != null && !tenNhanVien.isEmpty()) query.setParameter("tenNhanVien", "%" + tenNhanVien + "%");

            List<TaiKhoan> ds = query.getResultList();
            List<Object[]> list = new ArrayList<>();
            for (TaiKhoan tk : ds) {
                list.add(new Object[]{
                    tk.getNhanVien().getMaNhanVien(),
                    tk.getNhanVien().getHoTen(),
                    tk.getTenTaiKhoan(),
                    tk.getMatKhau()
                });
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean kiemTraTonTaiTheoMaNV(String maNV) {
        try {
            Long count = em.createQuery("SELECT COUNT(tk) FROM TaiKhoan tk WHERE tk.nhanVien.maNhanVien = :maNV", Long.class)
                    .setParameter("maNV", maNV)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public TaiKhoan dangNhap(String tenTaiKhoan, String matKhau) {
        try {
            TypedQuery<TaiKhoan> query = em.createQuery(
                "SELECT tk FROM TaiKhoan tk JOIN FETCH tk.nhanVien WHERE tk.tenTaiKhoan = :ten AND tk.matKhau = :pass", TaiKhoan.class);
            query.setParameter("ten", tenTaiKhoan);
            query.setParameter("pass", matKhau);
            List<TaiKhoan> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean xacThuc(String tenTaiKhoan, String matKhau) {
        return dangNhap(tenTaiKhoan, matKhau) != null;
    }

    public static class NhanVienInfo {
        public String hoTen;
        public int chucVu;

        public NhanVienInfo(String hoTen, int chucVu) {
            this.hoTen = hoTen;
            this.chucVu = chucVu;
        }
    }
}