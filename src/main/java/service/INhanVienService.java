package service;

import entity.NhanVien;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface INhanVienService extends Remote {
    List<NhanVien> getAll() throws RemoteException;
    boolean them(NhanVien nv) throws RemoteException;
    boolean sua(NhanVien nv) throws RemoteException;
    NhanVien findById(String maNV) throws RemoteException;
}
