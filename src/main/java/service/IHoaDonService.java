package service;

import entity.HoaDon;
import entity.ChiTietHoaDon;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IHoaDonService extends Remote {
    List<HoaDon> getAll() throws RemoteException;
    boolean them(HoaDon hd) throws RemoteException;
    boolean update(HoaDon hd) throws RemoteException;
    HoaDon findById(String maHD) throws RemoteException;
    HoaDon findByMaHoaDon(String maHD) throws RemoteException;
    List<ChiTietHoaDon> getChiTietByMaHoaDon(String maHD) throws RemoteException;
    boolean removeChiTiet(String maHD, String maVe) throws RemoteException;
}
