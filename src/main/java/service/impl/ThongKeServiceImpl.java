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
        Double res = em.createQuery("SELECT SUM(hd.tongTien) FROM HoaDon hd WHERE MONTH(hd.ngayTao) = :thang AND YEAR(hd.ngayTao) = :nam AND hd.trangThai = true", Double.class)
                .setParameter("thang", thang)
                .setParameter("nam", nam)
                .getSingleResult();
        return res != null ? res : 0;
    }

    @Override
    public int getTongSoVe(int thang, int nam) throws RemoteException {
        Long res = em.createQuery("SELECT COUNT(v) FROM Ve v WHERE MONTH(v.thoiGianLenTau) = :thang AND YEAR(v.thoiGianLenTau) = :nam AND v.trangThai = true", Long.class)
                .setParameter("thang", thang)
                .setParameter("nam", nam)
                .getSingleResult();
        return res != null ? res.intValue() : 0;
    }

    @Override
    public int getTongLuongKhach(int thang, int nam) throws RemoteException {
        Long res = em.createQuery("SELECT COUNT(DISTINCT v.soCCCD) FROM Ve v WHERE MONTH(v.thoiGianLenTau) = :thang AND YEAR(v.thoiGianLenTau) = :nam AND v.trangThai = true", Long.class)
                .setParameter("thang", thang)
                .setParameter("nam", nam)
                .getSingleResult();
        return res != null ? res.intValue() : 0;
    }

    @Override
    public int getTongSoChuyen(int thang, int nam) throws RemoteException {
        Long res = em.createQuery("SELECT COUNT(DISTINCT lt.maLichTrinh) FROM LichTrinh lt WHERE MONTH(lt.gioKhoiHanh) = :thang AND YEAR(lt.gioKhoiHanh) = :nam", Long.class)
                .setParameter("thang", thang)
                .setParameter("nam", nam)
                .getSingleResult();
        return res != null ? res.intValue() : 0;
    }

    @Override
    public int getTongLuotKhach(int thang, int nam) throws RemoteException {
        return getTongSoVe(thang, nam);
    }

    @Override
    public int getTongSoTuyen(int thang, int nam) throws RemoteException {
        Long res = em.createQuery("SELECT COUNT(DISTINCT lt.tuyen.maTuyen) FROM LichTrinh lt WHERE MONTH(lt.gioKhoiHanh) = :thang AND YEAR(lt.gioKhoiHanh) = :nam", Long.class)
                .setParameter("thang", thang)
                .setParameter("nam", nam)
                .getSingleResult();
        return res != null ? res.intValue() : 0;
    }

    @Override
    public List<Object[]> getTuyenNhieuNhatTrongThang(int thang, int nam) throws RemoteException {
        System.out.println("📊 Thống kê tuyến chạy nhiều nhất: " + thang + "/" + nam);
        List<Object[]> results = em.createQuery("SELECT lt.tuyen.maTuyen, COUNT(v) FROM Ve v JOIN v.lichTrinh lt WHERE MONTH(lt.gioKhoiHanh) = :thang AND YEAR(lt.gioKhoiHanh) = :nam AND v.trangThai = true GROUP BY lt.tuyen.maTuyen ORDER BY COUNT(v) DESC", Object[].class)
                .setParameter("thang", thang)
                .setParameter("nam", nam)
                .setMaxResults(5)
                .getResultList();
        System.out.println("✅ Kết quả thống kê tuyến: " + results.size() + " bản ghi.");
        return results;
    }

    @Override
    public Map<String, Integer> getTiLeKhachHangTheoDoiTuong(int thang, int nam) throws RemoteException {
        System.out.println("📊 Thống kê tỷ lệ đối tượng khách hàng: " + thang + "/" + nam);
        List<Object[]> list = em.createQuery("SELECT v.loaiVe.maLoaiVe, COUNT(v) FROM Ve v WHERE MONTH(v.thoiGianLenTau) = :thang AND YEAR(v.thoiGianLenTau) = :nam AND v.trangThai = true GROUP BY v.loaiVe.maLoaiVe", Object[].class)
                .setParameter("thang", thang)
                .setParameter("nam", nam)
                .getResultList();
        Map<String, Integer> res = new HashMap<>();
        for (Object[] obj : list) res.put((String)obj[0], ((Long)obj[1]).intValue());
        System.out.println("✅ Kết quả tỷ lệ đối tượng: " + res.size() + " nhóm.");
        return res;
    }

    @Override
    public Map<Integer, Integer> getSoVeTheoNgay(int thang, int nam) throws RemoteException {
        List<Object[]> list = em.createQuery("SELECT DAY(v.thoiGianLenTau), COUNT(v) FROM Ve v WHERE MONTH(v.thoiGianLenTau) = :thang AND YEAR(v.thoiGianLenTau) = :nam GROUP BY DAY(v.thoiGianLenTau)", Object[].class)
                .setParameter("thang", thang)
                .setParameter("nam", nam)
                .getResultList();
        Map<Integer, Integer> res = new HashMap<>();
        for (Object[] obj : list) res.put((Integer)obj[0], ((Long)obj[1]).intValue());
        return res;
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
