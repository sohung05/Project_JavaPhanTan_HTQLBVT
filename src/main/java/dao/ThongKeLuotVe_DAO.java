package dao;

import entity.*;
import jakarta.persistence.EntityManager;
import utils.EntityManagerFactoryUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThongKeLuotVe_DAO {

    private EntityManager em;

    public ThongKeLuotVe_DAO() {
        this.em = new EntityManagerFactoryUtil().getEntityManager();
    }

    public int getTongLuotKhach(int thang, int nam) {
        String sql = """
            SELECT COUNT(ct.maVe) AS tongLuot
            FROM ChiTietHoaDon ct
            JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
            WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
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

    public int getTongSoVe(int thang, int nam) {
        String sql = """
            SELECT COUNT(DISTINCT ct.maVe) AS tongVe
            FROM ChiTietHoaDon ct
            JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
            WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
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

    public int getTongSoTuyen(int thang, int nam) {
        String sql = """
            SELECT COUNT(DISTINCT v.maLichTrinh) AS tongTuyen
            FROM ChiTietHoaDon ct
            JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
            JOIN Ve v ON ct.maVe = v.maVe
            WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
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

    public List<Object[]> getTuyenNhieuNhatTrongThang(int thang, int nam) {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT v.maLichTrinh, COUNT(ct.maVe) AS soLuot
            FROM ChiTietHoaDon ct
            JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
            JOIN Ve v ON ct.maVe = v.maVe
            WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
            GROUP BY v.maLichTrinh
            ORDER BY soLuot DESC
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql)
                .setParameter(1, thang).setParameter(2, nam)
                .getResultList();

            for (Object[] row : results) {
                list.add(new Object[]{row[0], ((Number) row[1]).intValue()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<DoiTuong, Integer> getTiLeKhachHangTheoDoiTuong(int thang, int nam) {
        Map<DoiTuong, Integer> map = new HashMap<>();
        String sql = """
            SELECT kh.doiTuong, SUM(ct.soLuong) AS tong
            FROM ChiTietHoaDon ct
            JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
            JOIN Ve v ON ct.maVe = v.maVe
            JOIN KhachHang kh ON v.maKH = kh.maKH
            WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
            GROUP BY kh.doiTuong
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql)
                .setParameter(1, thang).setParameter(2, nam)
                .getResultList();

            for (Object[] row : results) {
                String doiTuongStr = (String) row[0];
                int tong = ((Number) row[1]).intValue();
                DoiTuong doiTuong = DoiTuong.fromString(doiTuongStr);
                map.put(doiTuong, tong);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Lấy số vé bán theo từng ngày trong tháng
     */
    public Map<Integer, Integer> getSoVeTheoNgay(int thang, int nam) {
        Map<Integer, Integer> map = new HashMap<>();
        String sql = """
            SELECT DAY(hd.ngayTao) AS ngay, COUNT(ct.maVe) AS soVe
            FROM ChiTietHoaDon ct
            JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
            WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1
            GROUP BY DAY(hd.ngayTao)
            ORDER BY ngay
        """;

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createNativeQuery(sql)
                .setParameter(1, thang).setParameter(2, nam)
                .getResultList();

            for (Object[] row : results) {
                int ngay = ((Number) row[0]).intValue();
                int soVe = ((Number) row[1]).intValue();
                map.put(ngay, soVe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public List<HoaDon> loadHoaDonTheoThangNam(int thang, int nam) {
        List<HoaDon> dsHoaDon = new ArrayList<>();

        String sqlHoaDon = "SELECT * FROM HoaDon WHERE MONTH(ngayTao) = ? AND YEAR(ngayTao) = ?";

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> hdResults = em.createNativeQuery(sqlHoaDon)
                .setParameter(1, thang).setParameter(2, nam)
                .getResultList();

            for (Object[] row : hdResults) {
                String maHoaDon = (String) row[0];

                NhanVien nv = new NhanVien();
                nv.setMaNhanVien((String) row[1]);

                KhachHang kh = new KhachHang();
                kh.setMaKH((String) row[2]);

                LocalDateTime gioTao = null;
                if (row[3] != null) {
                    if (row[3] instanceof Timestamp) gioTao = ((Timestamp) row[3]).toLocalDateTime();
                    else if (row[3] instanceof java.sql.Date) gioTao = ((java.sql.Date) row[3]).toLocalDate().atStartOfDay();
                }

                LocalDateTime ngayTao = null;
                if (row[4] != null) {
                    if (row[4] instanceof Timestamp) ngayTao = ((Timestamp) row[4]).toLocalDateTime();
                    else if (row[4] instanceof java.sql.Date) ngayTao = ((java.sql.Date) row[4]).toLocalDate().atStartOfDay();
                }

                boolean trangThai = row[6] != null && (Boolean) row[6];

                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(maHoaDon);
                hd.setNhanVien(nv);
                hd.setKhachHang(kh);
                hd.setGioTao(gioTao);
                hd.setNgayTao(ngayTao);
                hd.setTrangThai(trangThai);

                // Load chi tiết hóa đơn
                String sqlCTHD = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ?";
                @SuppressWarnings("unchecked")
                List<Object[]> ctResults = em.createNativeQuery(sqlCTHD)
                    .setParameter(1, maHoaDon)
                    .getResultList();

                for (Object[] ctRow : ctResults) {
                    ChiTietHoaDon cthd = new ChiTietHoaDon();
                    cthd.setHoaDon(hd);
                    Ve veObj = new Ve();
                    veObj.setMaVe((String) ctRow[1]);
                    cthd.setVe(veObj);
                    cthd.setSoLuong(((Number) ctRow[2]).intValue());
                    cthd.setGiaVe(((Number) ctRow[3]).doubleValue());
                    cthd.setMucGiam(((Number) ctRow[4]).doubleValue());
                    hd.themChiTiet(cthd);
                }

                dsHoaDon.add(hd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dsHoaDon;
    }
}