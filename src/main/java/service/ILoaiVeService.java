package service;

import entity.LoaiVe;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ILoaiVeService extends Remote {
    List<LoaiVe> findAll() throws RemoteException;
    LoaiVe findByMaLoaiVe(String maLoaiVe) throws RemoteException;
    LoaiVe findByTenLoaiVe(String tenLoaiVe) throws RemoteException;
    boolean insert(LoaiVe lv) throws RemoteException;
    boolean update(LoaiVe lv) throws RemoteException;
    boolean delete(String maLoaiVe) throws RemoteException;
}
