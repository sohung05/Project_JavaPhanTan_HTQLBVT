package service;

import entity.HoaDon;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface IThongKeService extends Remote {
    // Thống kê doanh thu
    List<HoaDon> loadHoaDonTheoThangNam(int thang, int nam) throws RemoteException;
    double getTongDoanhThu(int thang, int nam) throws RemoteException;
    int getTongSoVe(int thang, int nam) throws RemoteException;
    int getTongLuongKhach(int thang, int nam) throws RemoteException;
    int getTongSoChuyen(int thang, int nam) throws RemoteException;

    // Thống kê lượt vé
    int getTongLuotKhach(int thang, int nam) throws RemoteException;
    int getTongSoTuyen(int thang, int nam) throws RemoteException;
    List<Object[]> getTuyenNhieuNhatTrongThang(int thang, int nam) throws RemoteException;
    Map<String, Integer> getTiLeKhachHangTheoDoiTuong(int thang, int nam) throws RemoteException;
    Map<Integer, Integer> getSoVeTheoNgay(int thang, int nam) throws RemoteException;

    // Thống kê theo ga
    Map<String, Integer> getSoVeTheoGa(int thang, int nam) throws RemoteException;
    Map<String, Double> getDoanhThuTheoGa(int thang, int nam) throws RemoteException;
}
