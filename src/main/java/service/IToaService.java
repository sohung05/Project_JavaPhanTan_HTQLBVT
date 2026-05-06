package service;

import entity.Toa;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IToaService extends Remote {
    List<Toa> getToaBySoHieuTau(String soHieuTau) throws RemoteException;
    List<Toa> findAll() throws RemoteException;
    Toa findByMaToa(String maToa) throws RemoteException;
    boolean insert(Toa toa) throws RemoteException;
    boolean update(Toa toa) throws RemoteException;
    boolean delete(String maToa) throws RemoteException;
}
