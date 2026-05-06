package service;

import entity.Ga;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IGaService extends Remote {
    List<Ga> findAll() throws RemoteException;
    Ga findByMaGa(String maGa) throws RemoteException;
    List<Ga> findByTenGa(String tenGa) throws RemoteException;
    boolean insert(Ga ga) throws RemoteException;
    boolean update(Ga ga) throws RemoteException;
    boolean delete(String maGa) throws RemoteException;
}
