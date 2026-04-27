package service;

import entity.LichTrinh;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ILichTrinhService extends Remote {
    List<LichTrinh> getAll() throws RemoteException;
    LichTrinh findById(String maLT) throws RemoteException;
}
