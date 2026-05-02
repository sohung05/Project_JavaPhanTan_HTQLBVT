package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Map;

public interface IDashboardService extends Remote {
    Map<String, Double> getThongKeTongQuan() throws RemoteException;
    int getSoKhuyenMaiSapHetHan(int soNgay) throws RemoteException;
    Map<Integer, Double> getDoanhThuTheoThang(int nam) throws RemoteException;
    Map<Integer, Integer> getSoVeTheoThang(int nam) throws RemoteException;
    Map<String, Double> getThongKeNgay(LocalDate ngay) throws RemoteException;
    Map<String, Double> getDoanhThuTheoTuyenTrongThang(int thang, int nam) throws RemoteException;
    Map<String, Integer> getSoVeTheoTuyen(int ngay, int thang, int top) throws RemoteException;
    Map<String, Integer> getSoChoNgoiConTrongTheoTuyen(LocalDate ngay) throws RemoteException;
    Map<String, Integer> getSoChoNgoiConTrongTheoTuyen(int ngay, int thang) throws RemoteException;
}
