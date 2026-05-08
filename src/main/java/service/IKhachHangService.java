package service;

import entity.KhachHang;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IKhachHangService extends Remote {
    List<KhachHang> getAll() throws RemoteException;
    boolean them(KhachHang kh) throws RemoteException;
    boolean sua(KhachHang kh) throws RemoteException;
    boolean exists(String maKH) throws RemoteException;
    KhachHang findByCCCD(String cccd) throws RemoteException;
    List<KhachHang> timKiem(String cccd, String hoTen, String email, String sdt, String doiTuong) throws RemoteException;
    List<KhachHang> getAllKhachHangAndHanhKhach() throws RemoteException;
}
