package dao;

import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.Ve;
import jakarta.persistence.EntityManager;
import utils.EntityManagerFactoryUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ThongKeDoanhThu_DAO {

    private EntityManager em;

    public ThongKeDoanhThu_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public List<HoaDon> loadHoaDonTheoThangNam(int thang, int nam) {
        List<HoaDon> ds = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE MONTH(ngayTao) = ? AND YEAR(ngayTao) = ? ORDER BY ngayTao ASC";

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql)
                .setParameter(1, thang)
                .setParameter(2, nam)
                .getResultList();

            for (Object[] row : results) {
                String maHD = (String) row[0];

                NhanVien nv = new NhanVien();
                nv.setMaNhanVien((String) row[1]);

                KhachHang kh = new KhachHang();
                kh.setMaKH((String) row[2]);

                LocalDateTime gioTao = row[3] != null ? ((Timestamp) row[3]).toLocalDateTime() : null;
                LocalDateTime ngayTao = row[4] != null ? ((Timestamp) row[4]).toLocalDateTime() : null;
                boolean trangThai = row[6] != null && (Boolean) row[6];

                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(maHD);
                hd.setNhanVien(nv);
                hd.setKhachHang(kh);
                hd.setGioTao(gioTao);
                hd.setNgayTao(ngayTao);
                hd.setTrangThai(trangThai);

                // Load chi tiết
                hd.setDanhSachChiTiet(loadChiTietHoaDon(maHD));

                ds.add(hd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    private List<ChiTietHoaDon> loadChiTietHoaDon(String maHD) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = """
            SELECT ct.maHoaDon, ct.maVe, ct.soLuong, ct.giaVe, ct.mucGiam, v.trangThai as veTrangThai
            FROM ChiTietHoaDon ct
            LEFT JOIN Ve v ON ct.maVe = v.maVe
            WHERE ct.maHoaDon = ?
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql)
                .setParameter(1, maHD)
                .getResultList();

            for (Object[] row : results) {
                ChiTietHoaDon ct = new ChiTietHoaDon();
                ct.setSoLuong(((Number) row[2]).intValue());
                ct.setGiaVe(((Number) row[3]).doubleValue());
                ct.setMucGiam(((Number) row[4]).doubleValue());

                Ve ve = new Ve();
                ve.setMaVe((String) row[1]);
                ve.setTrangThai(row[5] != null && (Boolean) row[5]);
                ct.setVe(ve);

                list.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public double getTongDoanhThu(int thang, int nam) {
        String sql = """
            SELECT COALESCE(SUM(
                (ct.giaVe - ct.mucGiam) * ct.soLuong
            ), 0) AS doanhThu
            FROM HoaDon hd
            JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon
            WHERE MONTH(hd.ngayTao) = ?
              AND YEAR(hd.ngayTao) = ?
              AND hd.trangThai = 1
              AND ct.soLuong > 0
              AND ct.giaVe >= ct.mucGiam
        """;

        try {
            Object result = em.createNativeQuery(sql)
                .setParameter(1, thang)
                .setParameter(2, nam)
                .getSingleResult();
            return result != null ? ((Number) result).doubleValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTongSoVe(int thang, int nam) {
        String sql = """
            SELECT SUM(ct.soLuong) AS tongVe
            FROM HoaDon hd
            JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon
            WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
        """;

        try {
            Object result = em.createNativeQuery(sql)
                .setParameter(1, thang)
                .setParameter(2, nam)
                .getSingleResult();
            return result != null ? ((Number) result).intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTongLuongKhach(int thang, int nam) {
        String sql = """
            SELECT COUNT(DISTINCT hd.maKH) AS tongKhach
            FROM HoaDon hd
            WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
        """;

        try {
            Object result = em.createNativeQuery(sql)
                .setParameter(1, thang)
                .setParameter(2, nam)
                .getSingleResult();
            return result != null ? ((Number) result).intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTongSoChuyen(int thang, int nam) {
        String sql = """
            SELECT COUNT(DISTINCT v.maLichTrinh) AS tongChuyen
            FROM HoaDon hd
            JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon
            JOIN Ve v ON ct.maVe = v.maVe
            WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
        """;

        try {
            Object result = em.createNativeQuery(sql)
                .setParameter(1, thang)
                .setParameter(2, nam)
                .getSingleResult();
            return result != null ? ((Number) result).intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
