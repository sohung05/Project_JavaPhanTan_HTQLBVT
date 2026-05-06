package service;

import entity.ChoNgoi;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IChoNgoiService extends Remote {
    List<ChoNgoi> getChoNgoiByMaToa(String maToa) throws RemoteException;
    boolean kiemTraChoNgoiDaDat(String maChoNgoi, String maLichTrinh) throws RemoteException;
    List<ChoNgoi> getChoNgoiTrong(String maToa, String maLichTrinh) throws RemoteException;
    List<ChoNgoi> findAll() throws RemoteException;
    ChoNgoi findByMaChoNgoi(String maChoNgoi) throws RemoteException;
    boolean insert(ChoNgoi cn) throws RemoteException;
    boolean update(ChoNgoi cn) throws RemoteException;
    boolean delete(String maChoNgoi) throws RemoteException;
}
