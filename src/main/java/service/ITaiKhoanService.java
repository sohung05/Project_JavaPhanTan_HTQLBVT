package service;

import entity.TaiKhoan;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITaiKhoanService extends Remote {
    TaiKhoan dangNhap(String tenTaiKhoan, String matKhau) throws RemoteException;
}
