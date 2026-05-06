package service;

import entity.TaiKhoan;
import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.List;

public interface ITaiKhoanService extends Remote {
    TaiKhoan dangNhap(String tenTaiKhoan, String matKhau) throws RemoteException;
    List<Object[]> getAll() throws RemoteException;
    boolean them(TaiKhoan tk) throws RemoteException;
    boolean sua(TaiKhoan tk) throws RemoteException;
    List<Object[]> timKiem(String maNV, String tenTaiKhoan, String tenNhanVien) throws RemoteException;
    boolean kiemTraTonTaiTheoMaNV(String maNV) throws RemoteException;
}
