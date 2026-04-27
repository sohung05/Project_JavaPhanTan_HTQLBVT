package service;

import entity.HoaDon;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IHoaDonService extends Remote {
    List<HoaDon> getAll() throws RemoteException;
    boolean them(HoaDon hd) throws RemoteException;
    HoaDon findById(String maHD) throws RemoteException;
}
