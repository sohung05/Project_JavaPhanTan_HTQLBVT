package dao;

import entity.KhuyenMai;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import utils.EntityManagerFactoryUtil;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KhuyenMaiHoaDon_DAO {

    private EntityManager em;

    public KhuyenMaiHoaDon_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<KhuyenMai> getTatCaKhuyenMaiHoaDon() {
        List<KhuyenMai> list = new ArrayList<>();

        String sql = """
            SELECT
                KM.maKhuyenMai,
                KM.tenKhuyenMai,
                KM.loaiKhuyenMai,
                KM.thoiGianBatDau,
                KM.thoiGianKetThuc,
                KM.trangThai,
                CTKM.dieuKien AS soLuongVe,
                CTKM.chietKhau
            FROM KhuyenMai KM
            JOIN ChiTietKhuyenMai CTKM ON CTKM.maKhuyenMai = KM.maKhuyenMai
            WHERE KM.loaiKhuyenMai = 'KMHD'
              AND CTKM.maHoaDon IS NULL
              AND CTKM.chietKhau > 0
            ORDER BY KM.thoiGianBatDau DESC
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql).getResultList();

            for (Object[] row : results) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKhuyenMai((String) row[0]);
                km.setTenKhuyenMai((String) row[1]);
                km.setLoaiKhuyenMai((String) row[2]);
                km.setThoiGianBatDau(row[3] != null ? ((Timestamp) row[3]).toLocalDateTime() : null);
                km.setThoiGianKetThuc(row[4] != null ? ((Timestamp) row[4]).toLocalDateTime() : null);
                km.setTrangThai(row[5] != null && (Boolean) row[5]);
                km.setDoiTuongApDung((String) row[6]); // dieuKien → doiTuongApDung
                double chietKhau = row[7] != null ? ((Number) row[7]).doubleValue() : 0;
                if (chietKhau > 1) {
                    chietKhau = chietKhau / 100.0;
                }
                km.setChietKhau(chietKhau);

                list.add(km);
            }

            System.out.println("✅ Đã load " + list.size() + " khuyến mãi hóa đơn (KMHD).");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("⚠️ Lỗi khi load danh sách khuyến mãi hóa đơn: " + e.getMessage());
        }

        return list;
    }

    public boolean themKhuyenMaiHoaDon(KhuyenMai km, String soVeKhongDung, double chietKhau) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // 1. Thêm KhuyenMai
            String insertKM = """
                INSERT INTO KhuyenMai(maKhuyenMai, tenKhuyenMai, loaiKhuyenMai,
                                      thoiGianBatDau, thoiGianKetThuc, trangThai)
                VALUES (?, ?, ?, ?, ?, ?)
            """;
            em.createNativeQuery(insertKM)
                .setParameter(1, km.getMaKhuyenMai())
                .setParameter(2, km.getTenKhuyenMai())
                .setParameter(3, km.getLoaiKhuyenMai())
                .setParameter(4, Timestamp.valueOf(km.getThoiGianBatDau()))
                .setParameter(5, Timestamp.valueOf(km.getThoiGianKetThuc()))
                .setParameter(6, km.isTrangThai())
                .executeUpdate();

            // 2. Thêm ChiTietKhuyenMai
            String insertCTKM = """
                INSERT INTO ChiTietKhuyenMai(maHoaDon, maKhuyenMai, dieuKien, chietKhau)
                VALUES (NULL, ?, ?, ?)
            """;
            em.createNativeQuery(insertCTKM)
                .setParameter(1, km.getMaKhuyenMai())
                .setParameter(2, soVeKhongDung)
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

    public List<KhuyenMai> locKhuyenMaiHoaDon(String keyword, LocalDate startDate, LocalDate endDate) {
        List<KhuyenMai> ds = new ArrayList<>();

        String sql = """
            SELECT maKhuyenMai, tenKhuyenMai, loaiKhuyenMai,
                   thoiGianBatDau, thoiGianKetThuc, trangThai
            FROM KhuyenMai
            WHERE tenKhuyenMai LIKE ?
              AND thoiGianBatDau <= ?
              AND thoiGianKetThuc >= ?
            ORDER BY thoiGianBatDau DESC
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql)
                .setParameter(1, "%" + keyword + "%")
                .setParameter(2, Timestamp.valueOf(endDate.atStartOfDay()))
                .setParameter(3, Timestamp.valueOf(startDate.atStartOfDay()))
                .getResultList();

            for (Object[] row : results) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKhuyenMai((String) row[0]);
                km.setTenKhuyenMai((String) row[1]);
                km.setLoaiKhuyenMai((String) row[2]);
                km.setThoiGianBatDau(row[3] != null ? ((Timestamp) row[3]).toLocalDateTime() : null);
                km.setThoiGianKetThuc(row[4] != null ? ((Timestamp) row[4]).toLocalDateTime() : null);
                km.setTrangThai(row[5] != null && (Boolean) row[5]);
                ds.add(km);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    public boolean capNhatKhuyenMaiHoaDon(String maKMCu, String maKMMoi, String ten,
                                          Date thoiGianBatDau, Date thoiGianKetThuc,
                                          double chietKhau, String dieuKien) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // 1. Update KhuyenMai
            int rowsKM = em.createNativeQuery(
                    "UPDATE KhuyenMai SET tenKhuyenMai = ?, thoiGianBatDau = ?, thoiGianKetThuc = ? WHERE maKhuyenMai = ?")
                .setParameter(1, ten)
                .setParameter(2, thoiGianBatDau)
                .setParameter(3, thoiGianKetThuc)
                .setParameter(4, maKMCu)
                .executeUpdate();

            if (rowsKM == 0) {
                System.err.println("⚠️ Không tìm thấy khuyến mãi với mã: " + maKMCu);
                tx.rollback();
                return false;
            }

            // 2. Update ChiTietKhuyenMai
            em.createNativeQuery(
                    "UPDATE ChiTietKhuyenMai SET dieuKien = ?, chietKhau = ? WHERE maKhuyenMai = ?")
                .setParameter(1, dieuKien)
                .setParameter(2, chietKhau)
                .setParameter(3, maKMCu)
                .executeUpdate();

            tx.commit();
            System.out.println("✅ Cập nhật khuyến mãi hóa đơn thành công: " + maKMCu);
            return true;

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy khuyến mãi hóa đơn đang có hiệu lực theo số lượng vé
     */
    public double getChietKhauHieuLucTheoSoVe(int soLuongVe) {
        String sql = """
            SELECT ctkm.chietKhau, ctkm.dieuKien
            FROM KhuyenMai km
            JOIN ChiTietKhuyenMai ctkm ON km.maKhuyenMai = ctkm.maKhuyenMai
            WHERE km.loaiKhuyenMai = 'KMHD'
              AND ctkm.maHoaDon IS NULL
              AND km.trangThai = 1
              AND GETDATE() BETWEEN km.thoiGianBatDau AND km.thoiGianKetThuc
            ORDER BY ctkm.chietKhau DESC
        """;

        double chietKhauMax = 0;
        String dieuKienMax = "";

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql).getResultList();

            for (Object[] row : results) {
                double chietKhau = ((Number) row[0]).doubleValue();
                String dieuKien = (String) row[1];

                if (kiemTraDieuKienSoVe(soLuongVe, dieuKien)) {
                    if (chietKhau > chietKhauMax) {
                        chietKhauMax = chietKhau;
                        dieuKienMax = dieuKien;
                    }
                }
            }

            if (chietKhauMax > 0) {
                System.out.println("✅ Tìm thấy khuyến mãi hóa đơn: " + dieuKienMax + " - " + (chietKhauMax * 100) + "% cho " + soLuongVe + " vé");
                return chietKhauMax;
            } else {
                System.out.println("⚠️ Không tìm thấy khuyến mãi hóa đơn cho " + soLuongVe + " vé");
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Kiểm tra xem số lượng vé có thỏa điều kiện không
     */
    private boolean kiemTraDieuKienSoVe(int soLuongVe, String dieuKien) {
        if (dieuKien == null || dieuKien.isEmpty()) {
            return false;
        }

        dieuKien = dieuKien.toLowerCase().replaceAll("v[eé]", "").trim();

        try {
            if (dieuKien.contains("≥") || dieuKien.contains("từ")) {
                String numStr = dieuKien.replaceAll("[^0-9]", "");
                int min = Integer.parseInt(numStr);
                return soLuongVe >= min;
            }

            if (dieuKien.contains("-")) {
                String[] parts = dieuKien.split("-");
                int min = Integer.parseInt(parts[0].trim());
                int max = Integer.parseInt(parts[1].trim());
                return soLuongVe >= min && soLuongVe <= max;
            }

            int exact = Integer.parseInt(dieuKien);
            return soLuongVe == exact;

        } catch (Exception e) {
            System.err.println("⚠️ Lỗi parse điều kiện: " + dieuKien);
            return false;
        }
    }
}