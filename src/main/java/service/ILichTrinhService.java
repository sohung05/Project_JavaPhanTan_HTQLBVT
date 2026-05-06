package service;

import entity.LichTrinh;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import java.time.LocalDate;
import java.util.List;

public interface ILichTrinhService extends Remote {
    List<LichTrinh> getAll() throws RemoteException;
    LichTrinh findById(String maLT) throws RemoteException;
    List<LichTrinh> timLichTrinh(String tenGaDi, String tenGaDen, LocalDate ngayDi) throws RemoteException;
    boolean insert(LichTrinh lt) throws RemoteException;
    boolean update(LichTrinh lt) throws RemoteException;
    boolean delete(String maLT) throws RemoteException;
}
