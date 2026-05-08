package dao;

import jakarta.persistence.EntityManager;
import utils.EntityManagerFactoryUtil;

import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Dashboard_DAO {

    private EntityManager em;

    public Dashboard_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public Map<String, Double> getThongKeTongQuan() {
        Map<String, Double> data = new HashMap<>();

        try {
            LocalDate now = LocalDate.now();
            int thang = now.getMonthValue();
            int nam = now.getYear();

            int thangTruoc = thang == 1 ? 12 : thang - 1;
            int namTruoc = thang == 1 ? nam - 1 : nam;

            double thangNay = getDoanhThuMotThang(thang, nam);
            double thangTruocDT = getDoanhThuMotThang(thangTruoc, namTruoc);

            double phanTram = 0;
            if (thangTruocDT != 0) {
                phanTram = ((thangNay - thangTruocDT) / thangTruocDT) * 100;
            }

            double ptVeBan = getPhanTramVeBanSoVoiThangTruoc(thang, nam);
            data.put("ptVeBan", ptVeBan);
            data.put("doanhThu", phanTram);
            data.put("tongDoanhThu", thangNay);

            // Tổng số vé trong tháng hiện tại
            Object soVeResult = em.createNativeQuery("""
                SELECT COUNT(DISTINCT v.maVe) AS soVe
                FROM Ve v
                JOIN ChiTietHoaDon ct ON v.maVe = ct.maVe
                JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
                WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ?
            """).setParameter(1, thang).setParameter(2, nam).getSingleResult();
            data.put("soVe", soVeResult != null ? ((Number) soVeResult).doubleValue() : 0);

            // Vé đã bán
            Object veBanResult = em.createNativeQuery("""
                SELECT COUNT(DISTINCT v.maVe) AS veBan
                FROM Ve v
                JOIN ChiTietHoaDon ct ON v.maVe = ct.maVe
                JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
                WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND v.trangThai = 1
            """).setParameter(1, thang).setParameter(2, nam).getSingleResult();
            data.put("soVeBan", veBanResult != null ? ((Number) veBanResult).doubleValue() : 0);

            // Vé đã trả/hủy
            Object veTraResult = em.createNativeQuery("""
                SELECT COUNT(DISTINCT v.maVe) AS veTra
                FROM Ve v
                JOIN ChiTietHoaDon ct ON v.maVe = ct.maVe
                JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
                WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND v.trangThai = 0
            """).setParameter(1, thang).setParameter(2, nam).getSingleResult();
            data.put("soVeTra", veTraResult != null ? ((Number) veTraResult).doubleValue() : 0);

            // Tổng số khách hàng
            Object khResult = em.createNativeQuery("""
                SELECT COUNT(DISTINCT v.maKH) AS khachHang
                FROM Ve v
                JOIN ChiTietHoaDon ct ON v.maVe = ct.maVe
                JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
                WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ?
            """).setParameter(1, thang).setParameter(2, nam).getSingleResult();
            data.put("khachHang", khResult != null ? ((Number) khResult).doubleValue() : 0);

            // Tổng số tuyến
            Object tuyenResult = em.createNativeQuery("""
                SELECT COUNT(DISTINCT v.maLichTrinh) AS tuyen
                FROM Ve v
                JOIN ChiTietHoaDon ct ON v.maVe = ct.maVe
                JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
                WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ?
            """).setParameter(1, thang).setParameter(2, nam).getSingleResult();
            data.put("tuyen", tuyenResult != null ? ((Number) tuyenResult).doubleValue() : 0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public Map<Integer, Double> getDoanhThuTheoThang(int nam) {
        Map<Integer, Double> data = new LinkedHashMap<>();
        String sql = """
            SELECT MONTH(hd.ngayTao) AS Thang,
                   ISNULL(SUM((cthd.giaVe - cthd.mucGiam) * cthd.soLuong), 0) AS DoanhThu
            FROM HoaDon hd
            JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
            JOIN Ve v ON v.maVe = cthd.maVe
            WHERE YEAR(hd.ngayTao) = ? AND hd.trangThai = 1 AND v.trangThai = 1
            GROUP BY MONTH(hd.ngayTao)
            ORDER BY Thang
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql)
                .setParameter(1, nam).getResultList();
            for (Object[] row : results) {
                data.put(((Number) row[0]).intValue(), ((Number) row[1]).doubleValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public double getDoanhThuMotThang(int thang, int nam) {
        String sql = """
            SELECT ISNULL(SUM((cthd.giaVe - cthd.mucGiam) * cthd.soLuong), 0) AS DoanhThu
            FROM HoaDon hd
            JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
            JOIN Ve v ON v.maVe = cthd.maVe
            WHERE hd.trangThai = 1 AND v.trangThai = 1
              AND MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ?
        """;

        try {
            Object result = em.createNativeQuery(sql)
                .setParameter(1, thang).setParameter(2, nam)
                .getSingleResult();
            return result != null ? ((Number) result).doubleValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Map<Integer, Integer> getSoVeTheoThang(int nam) {
        Map<Integer, Integer> data = new LinkedHashMap<>();
        String sql = """
            SELECT MONTH(hd.ngayTao) AS Thang, COUNT(DISTINCT v.maVe) AS SoVe
            FROM HoaDon hd
            JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
            JOIN Ve v ON cthd.maVe = v.maVe
            WHERE YEAR(hd.ngayTao) = ? AND hd.trangThai = 1 AND v.trangThai = 1
            GROUP BY MONTH(hd.ngayTao)
            ORDER BY Thang
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql)
                .setParameter(1, nam).getResultList();
            for (Object[] row : results) {
                data.put(((Number) row[0]).intValue(), ((Number) row[1]).intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public int getSoVeBanMotThang(int thang, int nam) {
        String sql = """
            SELECT COUNT(DISTINCT v.maVe)
            FROM Ve v
            JOIN ChiTietHoaDon ct ON ct.maVe = v.maVe
            JOIN HoaDon hd ON hd.maHoaDon = ct.maHoaDon
            WHERE hd.trangThai = 1 AND v.trangThai = 1
              AND MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ?
        """;

        try {
            Object result = em.createNativeQuery(sql)
                .setParameter(1, thang).setParameter(2, nam)
                .getSingleResult();
            return result != null ? ((Number) result).intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getPhanTramVeBanSoVoiThangTruoc(int thang, int nam) {
        int thangNay = getSoVeBanMotThang(thang, nam);
        int thangTruoc = thang == 1 ? 12 : thang - 1;
        int namTruoc = thang == 1 ? nam - 1 : nam;
        int thangTruocVe = getSoVeBanMotThang(thangTruoc, namTruoc);

        if (thangTruocVe == 0) return 0;
        return ((double) (thangNay - thangTruocVe) / thangTruocVe) * 100;
    }

    public Map<String, Double> getDoanhThuTheoTuyenTrongThang(int month, int year) {
        Map<String, Double> data = new LinkedHashMap<>();
        String sql = """
            SELECT g1.tenGa + ' - ' + g2.tenGa AS tenChuyen, SUM((cthd.giaVe - cthd.mucGiam) * cthd.soLuong) AS doanhThu
            FROM Ve v
            JOIN ChiTietHoaDon cthd ON v.maVe = cthd.maVe
            JOIN HoaDon hd ON cthd.maHoaDon = hd.maHoaDon
            JOIN Ga g1 ON v.maGaDi = g1.maGa
            JOIN Ga g2 ON v.maGaDen = g2.maGa
            WHERE hd.trangThai = 1 AND v.trangThai = 1
              AND MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ?
            GROUP BY g1.tenGa, g2.tenGa
            ORDER BY doanhThu DESC
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql)
                .setParameter(1, month)
                .setParameter(2, year)
                .getResultList();
            for (Object[] row : results) {
                data.put((String) row[0], ((Number) row[1]).doubleValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public Map<String, Double> getThongKeNgay(LocalDate today) {
        Map<String, Double> thongKe = new LinkedHashMap<>();
        String sql = """
            SELECT COUNT(DISTINCT hd.maKH) AS soKhachMoi,
                   SUM((cthd.giaVe - cthd.mucGiam) * cthd.soLuong) AS doanhThu,
                   COUNT(v.maVe) AS soVeBan
            FROM HoaDon hd
            JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon
            JOIN Ve v ON cthd.maVe = v.maVe
            WHERE CAST(hd.ngayTao AS DATE) = ? 
              AND hd.trangThai = 1 AND v.trangThai = 1
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql)
                .setParameter(1, java.sql.Date.valueOf(today))
                .getResultList();

            if (!results.isEmpty()) {
                Object[] row = results.get(0);
                thongKe.put("ptKhachHang", row[0] != null ? ((Number) row[0]).doubleValue() : 0);
                thongKe.put("doanhThu", row[1] != null ? ((Number) row[1]).doubleValue() : 0);
                thongKe.put("soVeBan", row[2] != null ? ((Number) row[2]).doubleValue() : 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thongKe;
    }

    public Map<String, Integer> getSoChoNgoiConTrongTheoTuyen() {
        return getSoChoNgoiConTrongTheoTuyen((LocalDate) null);
    }

    public Map<String, Integer> getSoChoNgoiConTrongTheoTuyen(LocalDate tuNgay) {
        Map<String, Integer> data = new HashMap<>();
        LocalDate ngayLoc = (tuNgay != null) ? tuNgay : LocalDate.now();

        String sql = """
            SELECT g1.tenGa + ' - ' + g2.tenGa AS tuyen,
                   SUM(sub.tongGhe) - SUM(sub.gheDaBan) AS soChoTrong
            FROM (
                SELECT lt.maLichTrinh, lt.maGaDi, lt.maGaDen,
                       COUNT(DISTINCT c.maChoNgoi) AS tongGhe,
                       COUNT(DISTINCT CASE WHEN v.trangThai = 1 THEN v.maVe END) AS gheDaBan
                FROM LichTrinh lt
                    JOIN ChuyenTau ct ON lt.soHieuTau = ct.soHieuTau
                    JOIN Toa t ON ct.soHieuTau = t.soHieuTau
                    JOIN ChoNgoi c ON t.maToa = c.maToa
                    LEFT JOIN Ve v ON v.maChoNgoi = c.maChoNgoi AND v.maLichTrinh = lt.maLichTrinh
                WHERE lt.gioKhoiHanh >= ?
                GROUP BY lt.maLichTrinh, lt.maGaDi, lt.maGaDen
            ) AS sub
                JOIN Ga g1 ON sub.maGaDi = g1.maGa
                JOIN Ga g2 ON sub.maGaDen = g2.maGa
            GROUP BY g1.tenGa, g2.tenGa
            ORDER BY soChoTrong DESC
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql)
                .setParameter(1, java.sql.Date.valueOf(ngayLoc))
                .getResultList();

            for (Object[] row : results) {
                data.put((String) row[0], ((Number) row[1]).intValue());
            }

            if (data.isEmpty()) {
                return getSoChoTrongSimple(ngayLoc);
            }

        } catch (Exception e) {
            System.err.println("❌ Lỗi SQL getSoChoNgoiConTrongTheoTuyen: " + e.getMessage());
            return getSoChoTrongSimple(ngayLoc);
        }
        return data;
    }

    public Map<String, Integer> getSoChoNgoiConTrongTheoTuyen(int day, int month) {
        int year = LocalDate.now().getYear();
        LocalDate ngayLoc = LocalDate.of(year, month, day);
        return getSoChoNgoiConTrongTheoTuyen(ngayLoc);
    }

    private Map<String, Integer> getSoChoTrongSimple(LocalDate ngayLoc) {
        Map<String, Integer> data = new HashMap<>();
        String sql = """
            SELECT g1.tenGa + ' - ' + g2.tenGa AS tuyen,
                   COUNT(DISTINCT lt.maLichTrinh) AS soChuyenTau,
                   SUM(CASE WHEN v.maVe IS NULL THEN 1 ELSE 0 END) AS soChoTrong
            FROM LichTrinh lt
                JOIN Ga g1 ON lt.maGaDi = g1.maGa
                JOIN Ga g2 ON lt.maGaDen = g2.maGa
                JOIN ChuyenTau ct ON lt.soHieuTau = ct.soHieuTau
                JOIN Toa t ON ct.soHieuTau = t.soHieuTau
                JOIN ChoNgoi c ON t.maToa = c.maToa
                LEFT JOIN Ve v ON v.maChoNgoi = c.maChoNgoi AND v.maLichTrinh = lt.maLichTrinh AND v.trangThai = 1
            WHERE lt.gioKhoiHanh >= ?
            GROUP BY g1.tenGa, g2.tenGa
            HAVING COUNT(DISTINCT lt.maLichTrinh) > 0
            ORDER BY soChoTrong DESC
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql)
                .setParameter(1, java.sql.Date.valueOf(ngayLoc))
                .getResultList();

            for (Object[] row : results) {
                data.put((String) row[0], ((Number) row[2]).intValue());
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi SQL simple: " + e.getMessage());
        }
        return data;
    }

    public Map<String, Integer> getGheTrongTheoTuyen(LocalDate from, LocalDate to) {
        Map<String, Integer> gheTrongMap = new HashMap<>();
        String sql = """
            SELECT lt.maTuyen, COUNT(cn.maChoNgoi) - COUNT(v.maVe) AS GheTrong
            FROM LichTrinh lt
            JOIN Toa t ON t.soHieuTau = lt.soHieuTau
            JOIN ChoNgoi cn ON cn.maToa = t.maToa
            LEFT JOIN Ve v ON v.maLichTrinh = lt.maLichTrinh AND v.maChoNgoi = cn.maChoNgoi AND v.trangThai = 1
            WHERE lt.gioKhoiHanh BETWEEN ? AND ?
            GROUP BY lt.maTuyen
            ORDER BY lt.maTuyen
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql)
                .setParameter(1, java.sql.Date.valueOf(from))
                .setParameter(2, java.sql.Date.valueOf(to))
                .getResultList();

            for (Object[] row : results) {
                gheTrongMap.put((String) row[0], ((Number) row[1]).intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gheTrongMap;
    }

    public int getSoKhuyenMaiSapHetHan(int soNgay) {
        String sql = """
            SELECT COUNT(*) AS soLuong
            FROM KhuyenMai
            WHERE CAST(thoiGianKetThuc AS DATE) >= CAST(GETDATE() AS DATE)
              AND CAST(thoiGianKetThuc AS DATE) <= DATEADD(DAY, ?, CAST(GETDATE() AS DATE))
              AND trangThai = 1
        """;

        try {
            Object result = em.createNativeQuery(sql)
                .setParameter(1, soNgay)
                .getSingleResult();
            int count = result != null ? ((Number) result).intValue() : 0;
            System.out.println("🎁 Số khuyến mãi sắp hết hạn trong " + soNgay + " ngày: " + count);
            return count;
        } catch (Exception e) {
            System.err.println("❌ Lỗi SQL getSoKhuyenMaiSapHetHan: " + e.getMessage());
            return 0;
        }
    }

    public Map<String, Integer> getSoVeTheoTuyen(int day, int month, int topN) {
        Map<String, Integer> data = new LinkedHashMap<>();
        int year = LocalDate.now().getYear();

        String sql = """
            SELECT TOP (?) g1.tenGa + ' - ' + g2.tenGa AS tuyen, COUNT(v.maVe) AS soVe
            FROM Ve v
                JOIN ChiTietHoaDon cthd ON v.maVe = cthd.maVe
                JOIN HoaDon hd ON cthd.maHoaDon = hd.maHoaDon
                JOIN Ga g1 ON v.maGaDi = g1.maGa
                JOIN Ga g2 ON v.maGaDen = g2.maGa
            WHERE v.trangThai = 1 AND hd.trangThai = 1
              AND MONTH(hd.ngayTao) = ?
              AND YEAR(hd.ngayTao) = ?
            GROUP BY g1.tenGa, g2.tenGa
            ORDER BY soVe DESC
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql)
                .setParameter(1, topN)
                .setParameter(2, month)
                .setParameter(3, year)
                .getResultList();

            for (Object[] row : results) {
                data.put((String) row[0], ((Number) row[1]).intValue());
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi SQL getSoVeTheoTuyen: " + e.getMessage());
        }
        return data;
    }

    public List<Object[]> getTopChuyenTauGheTrong(LocalDate ngay, int topN) {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT TOP (?) 
                lt.soHieuTau,
                g1.tenGa + ' - ' + g2.tenGa AS tuyen,
                sub.tongGhe,
                sub.gheDaBan,
                (sub.tongGhe - sub.gheDaBan) AS gheTrong
            FROM LichTrinh lt
            JOIN (
                SELECT lt2.maLichTrinh,
                       COUNT(DISTINCT c.maChoNgoi) AS tongGhe,
                       COUNT(DISTINCT v.maVe) AS gheDaBan
                FROM LichTrinh lt2
                JOIN ChuyenTau ct ON lt2.soHieuTau = ct.soHieuTau
                JOIN Toa t ON ct.soHieuTau = t.soHieuTau
                JOIN ChoNgoi c ON t.maToa = c.maToa
                LEFT JOIN Ve v ON v.maChoNgoi = c.maChoNgoi AND v.maLichTrinh = lt2.maLichTrinh AND v.trangThai = 1
                GROUP BY lt2.maLichTrinh
            ) sub ON lt.maLichTrinh = sub.maLichTrinh
            JOIN Ga g1 ON lt.maGaDi = g1.maGa
            JOIN Ga g2 ON lt.maGaDen = g2.maGa
            WHERE CAST(lt.gioKhoiHanh AS DATE) = ?
            ORDER BY gheTrong DESC
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql)
                .setParameter(1, topN)
                .setParameter(2, java.sql.Date.valueOf(ngay))
                .getResultList();
            list.addAll(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getSoTauDangChay() {
        String sql = "SELECT COUNT(*) FROM LichTrinh WHERE gioKhoiHanh <= GETDATE() AND gioDenDuKien >= GETDATE() AND trangThai = 1";
        try {
            Object result = em.createNativeQuery(sql).getSingleResult();
            return result != null ? ((Number) result).intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getTenTauDangChay() {
        String sql = "SELECT soHieuTau FROM LichTrinh WHERE gioKhoiHanh <= GETDATE() AND gioDenDuKien >= GETDATE() AND trangThai = 1";
        try {
            @SuppressWarnings("unchecked")
            List<String> results = em.createNativeQuery(sql).getResultList();
            if (results == null || results.isEmpty()) return "Không có";
            return String.join(", ", results);
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi";
        }
    }
}
