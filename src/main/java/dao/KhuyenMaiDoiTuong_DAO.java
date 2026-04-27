package dao;

import entity.DoiTuong;
import entity.KhuyenMai;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.EntityManagerFactoryUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KhuyenMaiDoiTuong_DAO {

    private EntityManager em;

    public KhuyenMaiDoiTuong_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<Object[]> getDanhSachKhuyenMaiDoiTuong() {
        List<Object[]> list = new ArrayList<>();

        String sql = """
            SELECT
                km.maKhuyenMai,
                km.tenKhuyenMai,
                ctkm.dieuKien AS doiTuong,
                km.thoiGianBatDau,
                km.thoiGianKetThuc,
                ctkm.chietKhau,
                km.trangThai
            FROM KhuyenMai km
            JOIN ChiTietKhuyenMai ctkm ON km.maKhuyenMai = ctkm.maKhuyenMai
            WHERE km.loaiKhuyenMai = 'KMKH'
              AND ctkm.maHoaDon IS NULL
            ORDER BY km.thoiGianBatDau DESC
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql).getResultList();

            for (Object[] row : results) {
                Object[] mapped = {
                    row[0], // maKhuyenMai
                    row[1], // tenKhuyenMai
                    row[2], // doiTuong
                    row[3] != null ? ((Timestamp) row[3]).toLocalDateTime() : null,
                    row[4] != null ? ((Timestamp) row[4]).toLocalDateTime() : null,
                    row[5] != null ? ((Number) row[5]).doubleValue() : 0.0,
                    row[6] != null ? (Boolean) row[6] : false
                };
                list.add(mapped);
            }

            System.out.println("✅ Đã load " + list.size() + " khuyến mãi đối tượng (KMKH).");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themKhuyenMaiDoiTuong(KhuyenMai km, DoiTuong doiTuong, double chietKhau) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // 1. Thêm KhuyenMai
            em.createNativeQuery(
                    "INSERT INTO KhuyenMai(maKhuyenMai, tenKhuyenMai, loaiKhuyenMai, thoiGianBatDau, thoiGianKetThuc, trangThai) VALUES (?, ?, ?, ?, ?, ?)")
                .setParameter(1, km.getMaKhuyenMai())
                .setParameter(2, km.getTenKhuyenMai())
                .setParameter(3, km.getLoaiKhuyenMai())
                .setParameter(4, Timestamp.valueOf(km.getThoiGianBatDau()))
                .setParameter(5, Timestamp.valueOf(km.getThoiGianKetThuc()))
                .setParameter(6, km.isTrangThai())
                .executeUpdate();

            // 2. Thêm ChiTietKhuyenMai
            em.createNativeQuery(
                    "INSERT INTO ChiTietKhuyenMai(maKhuyenMai, dieuKien, chietKhau, maHoaDon) VALUES (?, ?, ?, NULL)")
                .setParameter(1, km.getMaKhuyenMai())
                .setParameter(2, doiTuong.name())
                .setParameter(3, chietKhau)
                .executeUpdate();

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean tamNgungTrangThai(String maKhuyenMai, boolean trangThai) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            int rows = em.createNativeQuery("UPDATE KhuyenMai SET trangThai = ? WHERE maKhuyenMai = ?")
                .setParameter(1, trangThai)
                .setParameter(2, maKhuyenMai)
                .executeUpdate();
            tx.commit();
            return rows > 0;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatKhuyenMaiDoiTuong(String maCu, String ten, Date thoiGianBatDau, Date thoiGianKetThuc, double chietKhau, String dieuKien) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // 1. Update KhuyenMai
            int rows = em.createNativeQuery(
                    "UPDATE KhuyenMai SET tenKhuyenMai = ?, thoiGianBatDau = ?, thoiGianKetThuc = ? WHERE maKhuyenMai = ?")
                .setParameter(1, ten)
                .setParameter(2, new java.sql.Date(thoiGianBatDau.getTime()))
                .setParameter(3, new java.sql.Date(thoiGianKetThuc.getTime()))
                .setParameter(4, maCu)
                .executeUpdate();

            if (rows == 0) {
                System.err.println("⚠️ Không tìm thấy khuyến mãi với mã: " + maCu);
                tx.rollback();
                return false;
            }

            // 2. Update ChiTietKhuyenMai
            em.createNativeQuery(
                    "UPDATE ChiTietKhuyenMai SET chietKhau = ?, dieuKien = ? WHERE maKhuyenMai = ?")
                .setParameter(1, chietKhau)
                .setParameter(2, dieuKien)
                .setParameter(3, maCu)
                .executeUpdate();

            tx.commit();
            System.out.println("✅ Cập nhật khuyến mãi đối tượng thành công: " + maCu);
            return true;

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public List<KhuyenMai> locKhuyenMaiTheoDoiTuong(String doiTuong, LocalDateTime fromDate, LocalDateTime toDate) {
        List<KhuyenMai> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT
                KM.maKhuyenMai,
                KM.tenKhuyenMai,
                CTKM.dieuKien AS doiTuong,
                KM.thoiGianBatDau,
                KM.thoiGianKetThuc,
                CTKM.chietKhau,
                KM.trangThai
            FROM KhuyenMai KM
            JOIN ChiTietKhuyenMai CTKM ON KM.maKhuyenMai = CTKM.maKhuyenMai
        """);

        List<Object> params = new ArrayList<>();
        int paramIndex = 1;
        boolean hasWhere = false;

        if (doiTuong != null && !doiTuong.equalsIgnoreCase("Tất cả")) {
            sql.append(" WHERE LTRIM(RTRIM(CTKM.dieuKien)) COLLATE Latin1_General_CI_AI LIKE ?");
            params.add("%" + doiTuong.trim() + "%");
            hasWhere = true;
        }

        if (fromDate != null) {
            sql.append(hasWhere ? " AND KM.thoiGianBatDau >= ?" : " WHERE KM.thoiGianBatDau >= ?");
            params.add(Timestamp.valueOf(fromDate));
            hasWhere = true;
        }
        if (toDate != null) {
            sql.append(hasWhere ? " AND KM.thoiGianKetThuc <= ?" : " WHERE KM.thoiGianKetThuc <= ?");
            params.add(Timestamp.valueOf(toDate));
        }

        sql.append(" ORDER BY KM.thoiGianBatDau DESC");

        try {
            var query = em.createNativeQuery(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i + 1, params.get(i));
            }

            @SuppressWarnings("unchecked")
            List<Object[]> results = query.getResultList();

            for (Object[] row : results) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKhuyenMai((String) row[0]);
                km.setTenKhuyenMai((String) row[1]);
                km.setLoaiKhuyenMai("KMKH");
                km.setThoiGianBatDau(row[3] != null ? ((Timestamp) row[3]).toLocalDateTime() : null);
                km.setThoiGianKetThuc(row[4] != null ? ((Timestamp) row[4]).toLocalDateTime() : null);
                km.setTrangThai(row[6] != null && (Boolean) row[6]);
                km.setChietKhau(row[5] != null ? ((Number) row[5]).doubleValue() : 0);
                km.setDoiTuongApDung((String) row[2]);
                list.add(km);
            }

            System.out.println("✅ Đã lọc được " + list.size() + " khuyến mãi cho đối tượng: " + doiTuong);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean kiemTraMaTonTai(String ma) {
        try {
            Object result = em.createNativeQuery("SELECT COUNT(*) FROM KhuyenMai WHERE maKhuyenMai = ?")
                .setParameter(1, ma)
                .getSingleResult();
            return ((Number) result).intValue() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy chiết khấu khuyến mãi đang có hiệu lực cho một đối tượng cụ thể
     */
    public double getChietKhauHieuLucTheoDoiTuong(String doiTuong) {
        String sql = """
            SELECT TOP 1 ctkm.chietKhau
            FROM KhuyenMai km
            JOIN ChiTietKhuyenMai ctkm ON km.maKhuyenMai = ctkm.maKhuyenMai
            WHERE km.loaiKhuyenMai = 'KMKH'
              AND ctkm.maHoaDon IS NULL
              AND ctkm.dieuKien = ?
              AND km.trangThai = 1
              AND GETDATE() BETWEEN km.thoiGianBatDau AND km.thoiGianKetThuc
            ORDER BY km.thoiGianBatDau DESC
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object> results = em.createNativeQuery(sql)
                .setParameter(1, doiTuong)
                .getResultList();

            if (!results.isEmpty()) {
                double chietKhau = ((Number) results.get(0)).doubleValue();
                System.out.println("✅ Tìm thấy khuyến mãi cho " + doiTuong + ": " + (chietKhau * 100) + "%");
                return chietKhau;
            } else {
                System.out.println("⚠️ Không tìm thấy khuyến mãi hiệu lực cho " + doiTuong);
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}