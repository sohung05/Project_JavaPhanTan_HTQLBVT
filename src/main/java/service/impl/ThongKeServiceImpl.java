package service.impl;

import entity.HoaDon;
import jakarta.persistence.EntityManager;
import service.IThongKeService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThongKeServiceImpl extends UnicastRemoteObject implements IThongKeService {
    private EntityManager em;

    public ThongKeServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public List<HoaDon> loadHoaDonTheoThangNam(int thang, int nam) throws RemoteException {
        return em.createQuery("SELECT hd FROM HoaDon hd WHERE MONTH(hd.ngayTao) = :thang AND YEAR(hd.ngayTao) = :nam", HoaDon.class)
                .setParameter("thang", thang)
                .setParameter("nam", nam)
                .getResultList();
    }

    @Override
    public double getTongDoanhThu(int thang, int nam) throws RemoteException {
        String sql = "SELECT ISNULL(SUM((cthd.giaVe - cthd.mucGiam) * cthd.soLuong), 0) " +
                     "FROM ChiTietHoaDon cthd " +
                     "JOIN HoaDon hd ON hd.maHoaDon = cthd.maHoaDon " +
                     "JOIN Ve v ON v.maVe = cthd.maVe " +
                     "WHERE hd.trangThai = 1 AND v.trangThai = 1 " +
                     "AND MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ?";
        try {
            Object result = em.createNativeQuery(sql)
                    .setParameter(1, thang)
                    .setParameter(2, nam)
                    .getSingleResult();
            return result != null ? ((Number) result).doubleValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int getTongSoVe(int thang, int nam) throws RemoteException {
        String jpql = "SELECT COUNT(DISTINCT ct.ve.maVe) FROM ChiTietHoaDon ct JOIN ct.hoaDon hd WHERE MONTH(hd.ngayTao) = :thang AND YEAR(hd.ngayTao) = :nam AND hd.trangThai = true";
        Long res = em.createQuery(jpql, Long.class)
                .setParameter("thang", thang)
                .setParameter("nam", nam)
                .getSingleResult();
        return res != null ? res.intValue() : 0;
    }

    @Override
    public int getTongLuongKhach(int thang, int nam) throws RemoteException {
        String jpql = "SELECT COUNT(ct.ve.maVe) FROM ChiTietHoaDon ct JOIN ct.hoaDon hd WHERE MONTH(hd.ngayTao) = :thang AND YEAR(hd.ngayTao) = :nam AND hd.trangThai = true";
        Long res = em.createQuery(jpql, Long.class)
                .setParameter("thang", thang)
                .setParameter("nam", nam)
                .getSingleResult();
        return res != null ? res.intValue() : 0;
    }

    @Override
    public int getTongSoChuyen(int thang, int nam) throws RemoteException {
        String jpql = "SELECT COUNT(DISTINCT ct.ve.lichTrinh.maLichTrinh) FROM ChiTietHoaDon ct JOIN ct.hoaDon hd WHERE MONTH(hd.ngayTao) = :thang AND YEAR(hd.ngayTao) = :nam AND hd.trangThai = true";
        try {
            Long res = em.createQuery(jpql, Long.class)
                    .setParameter("thang", thang)
                    .setParameter("nam", nam)
                    .getSingleResult();
            return res != null ? res.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int getTongLuotKhach(int thang, int nam) throws RemoteException {
        return getTongLuongKhach(thang, nam);
    }

    @Override
    public int getTongSoTuyen(int thang, int nam) throws RemoteException {
        String jpql = "SELECT COUNT(DISTINCT ct.ve.lichTrinh.maLichTrinh) FROM ChiTietHoaDon ct JOIN ct.hoaDon hd WHERE MONTH(hd.ngayTao) = :thang AND YEAR(hd.ngayTao) = :nam AND hd.trangThai = true";
        try {
            Long res = em.createQuery(jpql, Long.class)
                    .setParameter("thang", thang)
                    .setParameter("nam", nam)
                    .getSingleResult();
            return res != null ? res.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Object[]> getTuyenNhieuNhatTrongThang(int thang, int nam) throws RemoteException {
        System.out.println("📊 Thống kê tuyến chạy nhiều nhất: " + thang + "/" + nam);
        try {
            String sql = "SELECT g1.tenGa + ' - ' + g2.tenGa AS tenChuyen, COUNT(ct.maVe) AS soLuot " +
                         "FROM ChiTietHoaDon ct " +
                         "JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon " +
                         "JOIN Ve v ON ct.maVe = v.maVe " +
                         "JOIN Ga g1 ON v.maGaDi = g1.maGa " +
                         "JOIN Ga g2 ON v.maGaDen = g2.maGa " +
                         "WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1 " +
                         "GROUP BY g1.tenGa, g2.tenGa " +
                         "ORDER BY soLuot DESC";
            @SuppressWarnings("unchecked")
            List<Object[]> rawResults = em.createNativeQuery(sql)
                    .setParameter(1, thang)
                    .setParameter(2, nam)
                    .getResultList();
            List<Object[]> results = new java.util.ArrayList<>();
            for (Object[] row : rawResults) {
                results.add(new Object[]{row[0], ((Number) row[1]).intValue()});
            }
            System.out.println("✅ Kết quả thống kê tuyến: " + results.size() + " bản ghi.");
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    @Override
    public Map<String, Integer> getTiLeKhachHangTheoDoiTuong(int thang, int nam) throws RemoteException {
        System.out.println("📊 Thống kê tỷ lệ đối tượng khách hàng: " + thang + "/" + nam);
        try {
            String sql = "SELECT kh.doiTuong, SUM(ct.soLuong) AS tong " +
                         "FROM ChiTietHoaDon ct " +
                         "JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon " +
                         "JOIN Ve v ON ct.maVe = v.maVe " +
                         "JOIN KhachHang kh ON v.maKH = kh.maKH " +
                         "WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1 " +
                         "GROUP BY kh.doiTuong";
            @SuppressWarnings("unchecked")
            List<Object[]> list = em.createNativeQuery(sql)
                    .setParameter(1, thang)
                    .setParameter(2, nam)
                    .getResultList();
            Map<String, Integer> res = new HashMap<>();
            for (Object[] obj : list) {
                String key = obj[0] != null ? obj[0].toString() : "Không xác định";
                res.put(key, ((Number) obj[1]).intValue());
            }
            System.out.println("✅ Kết quả tỷ lệ đối tượng: " + res.size() + " nhóm.");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public Map<Integer, Integer> getSoVeTheoNgay(int thang, int nam) throws RemoteException {
        try {
            String sql = "SELECT DAY(hd.ngayTao) AS ngay, COUNT(ct.maVe) AS soVe " +
                         "FROM ChiTietHoaDon ct " +
                         "JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon " +
                         "WHERE MONTH(hd.ngayTao) = ? AND YEAR(hd.ngayTao) = ? AND hd.trangThai = 1 " +
                         "GROUP BY DAY(hd.ngayTao) " +
                         "ORDER BY ngay";
            @SuppressWarnings("unchecked")
            List<Object[]> list = em.createNativeQuery(sql)
                    .setParameter(1, thang)
                    .setParameter(2, nam)
                    .getResultList();
            Map<Integer, Integer> res = new HashMap<>();
            for (Object[] obj : list) res.put(((Number) obj[0]).intValue(), ((Number) obj[1]).intValue());
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Integer> getSoVeTheoGa(int thang, int nam) throws RemoteException {
        List<Object[]> list = em.createQuery("SELECT lt.gaDen.tenGa, COUNT(v) FROM Ve v JOIN v.lichTrinh lt WHERE MONTH(lt.gioKhoiHanh) = :thang AND YEAR(lt.gioKhoiHanh) = :nam GROUP BY lt.gaDen.tenGa", Object[].class)
                .setParameter("thang", thang)
                .setParameter("nam", nam)
                .getResultList();
        Map<String, Integer> res = new HashMap<>();
        for (Object[] obj : list) res.put((String)obj[0], ((Long)obj[1]).intValue());
        return res;
    }

    @Override
    public Map<String, Double> getDoanhThuTheoGa(int thang, int nam) throws RemoteException {
        List<Object[]> list = em.createQuery("SELECT lt.gaDen.tenGa, SUM(v.giaVe) FROM Ve v JOIN v.lichTrinh lt WHERE MONTH(lt.gioKhoiHanh) = :thang AND YEAR(lt.gioKhoiHanh) = :nam GROUP BY lt.gaDen.tenGa", Object[].class)
                .setParameter("thang", thang)
                .setParameter("nam", nam)
                .getResultList();
        Map<String, Double> res = new HashMap<>();
        for (Object[] obj : list) res.put((String)obj[0], (Double)obj[1]);
        return res;
    }
}
