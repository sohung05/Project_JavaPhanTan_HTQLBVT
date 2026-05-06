package service;

import entity.Ve;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public interface IVeService extends Remote {
    List<Ve> findAll() throws RemoteException;
    Ve findByMaVe(String maVe) throws RemoteException;
    boolean insert(Ve ve) throws RemoteException;
    boolean update(Ve ve) throws RemoteException;
    boolean delete(String maVe) throws RemoteException;
    boolean kiemTraGheDaDat(String maChoNgoi, String maLichTrinh) throws RemoteException;
    Set<String> layDanhSachGheDaDat(String maLichTrinh) throws RemoteException;
    List<Ve> searchVe(String keyword) throws RemoteException;
    List<Ve> findByMaHoaDon(String maHoaDon) throws RemoteException;
}
