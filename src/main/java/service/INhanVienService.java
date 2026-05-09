package service;

import entity.NhanVien;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface INhanVienService extends Remote {
    List<NhanVien> getAll() throws RemoteException;
    boolean them(NhanVien nv) throws RemoteException;
    boolean sua(NhanVien nv) throws RemoteException;
    NhanVien findById(String maNV) throws RemoteException;
    List<NhanVien> timKiem(String maNV, String cccd, String hoTen, String email, String sdt,
                          String trangThai, String gioiTinh, LocalDate ngaySinh) throws RemoteException;
    String generateMaNhanVien(LocalDate ngayVaoLam, LocalDate ngaySinh) throws RemoteException;
    boolean existsByCCCD(String cccd) throws RemoteException;
}
