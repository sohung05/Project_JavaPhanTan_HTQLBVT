package service.impl;

import jakarta.persistence.EntityManager;
import service.IDashboardService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardServiceImpl extends UnicastRemoteObject implements IDashboardService {
    private EntityManager em;

    public DashboardServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public Map<String, Double> getThongKeTongQuan() throws RemoteException {
        Map<String, Double> res = new HashMap<>();
        Double doanhThu = em.createQuery("SELECT SUM(hd.tongTien) FROM HoaDon hd WHERE hd.trangThai = true", Double.class).getSingleResult();
        Long soVe = em.createQuery("SELECT COUNT(v) FROM Ve v WHERE v.trangThai = true", Long.class).getSingleResult();
        Long soKH = em.createQuery("SELECT COUNT(kh) FROM KhachHang kh", Long.class).getSingleResult();
        
        res.put("doanhThu", doanhThu != null ? doanhThu : 0.0);
        res.put("soVeBan", soVe != null ? soVe.doubleValue() : 0.0);
        res.put("soKH", soKH != null ? soKH.doubleValue() : 0.0);
        
        // Giả lập phần trăm tăng trưởng (hoặc bạn có thể viết query so sánh với tháng trước)
        res.put("ptVeBan", 5.0); 
        res.put("ptKhachHang", 2.0);
        res.put("soVeTra", 0.0);
        return res;
    }

    @Override
    public int getSoKhuyenMaiSapHetHan(int soNgay) throws RemoteException {
        LocalDateTime limit = LocalDateTime.now().plusDays(soNgay);
        Long count = em.createQuery("SELECT COUNT(km) FROM KhuyenMai km WHERE km.thoiGianKetThuc <= :limit AND km.trangThai = true", Long.class)
                .setParameter("limit", limit)
                .getSingleResult();
        return count != null ? count.intValue() : 0;
    }

    @Override
    public Map<Integer, Double> getDoanhThuTheoThang(int nam) throws RemoteException {
        List<Object[]> list = em.createQuery("SELECT MONTH(hd.ngayTao), SUM(hd.tongTien) FROM HoaDon hd WHERE YEAR(hd.ngayTao) = :nam AND hd.trangThai = true GROUP BY MONTH(hd.ngayTao)", Object[].class)
                .setParameter("nam", nam)
                .getResultList();
        Map<Integer, Double> res = new HashMap<>();
        for (Object[] obj : list) res.put((Integer)obj[0], ((Number)obj[1]).doubleValue());
        return res;
    }

    @Override
    public Map<Integer, Integer> getSoVeTheoThang(int nam) throws RemoteException {
        List<Object[]> list = em.createQuery("SELECT MONTH(v.thoiGianLenTau), COUNT(v) FROM Ve v WHERE YEAR(v.thoiGianLenTau) = :nam AND v.trangThai = true GROUP BY MONTH(v.thoiGianLenTau)", Object[].class)
                .setParameter("nam", nam)
                .getResultList();
        Map<Integer, Integer> res = new HashMap<>();
        for (Object[] obj : list) res.put((Integer)obj[0], ((Long)obj[1]).intValue());
        return res;
    }

    @Override
    public Map<String, Double> getThongKeNgay(LocalDate ngay) throws RemoteException {
        System.out.println("📊 Thống kê ngày: " + ngay);
        Map<String, Double> res = new HashMap<>();
        // Sử dụng khoảng thời gian để so sánh LocalDateTime
        Double doanhThu = em.createQuery("SELECT SUM(hd.tongTien) FROM HoaDon hd WHERE hd.ngayTao >= :start AND hd.ngayTao < :end AND hd.trangThai = true", Double.class)
                .setParameter("start", ngay.atStartOfDay())
                .setParameter("end", ngay.plusDays(1).atStartOfDay())
                .getSingleResult();
        Long soVe = em.createQuery("SELECT COUNT(v) FROM Ve v WHERE v.thoiGianLenTau >= :start AND v.thoiGianLenTau < :end AND v.trangThai = true", Long.class)
                .setParameter("start", ngay.atStartOfDay())
                .setParameter("end", ngay.plusDays(1).atStartOfDay())
                .getSingleResult();
        res.put("doanhThu", doanhThu != null ? doanhThu : 0.0);
        res.put("soVe", soVe != null ? soVe.doubleValue() : 0.0);
        res.put("ptKhachHang", 1.0);
        System.out.println("✅ Kết quả thống kê ngày: Doanh thu=" + res.get("doanhThu") + ", Số vé=" + res.get("soVe"));
        return res;
    }

    @Override
    public Map<String, Double> getDoanhThuTheoTuyenTrongThang(int thang, int nam) throws RemoteException {
        System.out.println("📊 Thống kê doanh thu theo tuyến: " + thang + "/" + nam);
        List<Object[]> list = em.createQuery("SELECT lt.tuyen.tenTuyen, SUM(v.giaVe) FROM Ve v JOIN v.lichTrinh lt WHERE MONTH(lt.gioKhoiHanh) = :thang AND YEAR(lt.gioKhoiHanh) = :nam AND v.trangThai = true GROUP BY lt.tuyen.tenTuyen", Object[].class)
                .setParameter("thang", thang)
                .setParameter("nam", nam)
                .getResultList();
        Map<String, Double> res = new HashMap<>();
        for (Object[] obj : list) res.put((String)obj[0], ((Number)obj[1]).doubleValue());
        System.out.println("✅ Kết quả doanh thu theo tuyến: " + res.size() + " tuyến.");
        return res;
    }

    @Override
    public Map<String, Integer> getSoVeTheoTuyen(int ngay, int thang, int top) throws RemoteException {
        int nam = LocalDate.now().getYear();
        System.out.println("📊 Thống kê số vé theo tuyến: " + thang + "/" + nam + " (Top " + top + ")");
        List<Object[]> list = em.createQuery("SELECT lt.tuyen.tenTuyen, COUNT(v) FROM Ve v JOIN v.lichTrinh lt WHERE MONTH(lt.gioKhoiHanh) = :thang AND YEAR(lt.gioKhoiHanh) = :nam AND v.trangThai = true GROUP BY lt.tuyen.tenTuyen ORDER BY COUNT(v) DESC", Object[].class)
                .setParameter("thang", thang)
                .setParameter("nam", nam)
                .setMaxResults(top)
                .getResultList();
        Map<String, Integer> res = new HashMap<>();
        for (Object[] obj : list) res.put((String)obj[0], ((Number)obj[1]).intValue());
        return res;
    }

    @Override
    public Map<String, Integer> getSoChoNgoiConTrongTheoTuyen(LocalDate ngay) throws RemoteException {
        // Logic đơn giản: Tổng số chỗ (giả định 100) - số vé đã đặt
        List<Object[]> list = em.createQuery("SELECT lt.tuyen.tenTuyen, COUNT(v) FROM Ve v JOIN v.lichTrinh lt WHERE CAST(lt.gioKhoiHanh AS LocalDate) = :ngay AND v.trangThai = true GROUP BY lt.tuyen.tenTuyen", Object[].class)
                .setParameter("ngay", ngay)
                .getResultList();
        Map<String, Integer> res = new HashMap<>();
        for (Object[] obj : list) res.put((String)obj[0], 100 - ((Long)obj[1]).intValue());
        return res;
    }

    @Override
    public Map<String, Integer> getSoChoNgoiConTrongTheoTuyen(int ngay, int thang) throws RemoteException {
        List<Object[]> list = em.createQuery("SELECT lt.tuyen.tenTuyen, COUNT(v) FROM Ve v JOIN v.lichTrinh lt WHERE DAY(lt.gioKhoiHanh) = :ngay AND MONTH(lt.gioKhoiHanh) = :thang AND v.trangThai = true GROUP BY lt.tuyen.tenTuyen", Object[].class)
                .setParameter("ngay", ngay)
                .setParameter("thang", thang)
                .getResultList();
        Map<String, Integer> res = new HashMap<>();
        for (Object[] obj : list) res.put((String)obj[0], 100 - ((Long)obj[1]).intValue());
        return res;
    }
}
