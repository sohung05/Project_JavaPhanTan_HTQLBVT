package service;

import entity.KhuyenMai;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IKhuyenMaiService extends Remote {
    // Khuyến mãi hóa đơn
    List<KhuyenMai> getTatCaKhuyenMaiHoaDon() throws RemoteException;
    boolean themKhuyenMaiHoaDon(KhuyenMai km, String soVeStr, double chietKhau) throws RemoteException;
    boolean tamNgungTrangThai(String maKhuyenMai, boolean trangThai) throws RemoteException;
    List<KhuyenMai> locKhuyenMaiHoaDon(String keyword, LocalDate startDate, LocalDate endDate) throws RemoteException;
    boolean capNhatKhuyenMaiHoaDon(String maKMCu, String maKMMoi, String ten, Date start, Date end, double chietKhau, String dieuKien) throws RemoteException;
    double getChietKhauHieuLucTheoSoVe(int soLuongVe) throws RemoteException;

    // Khuyến mãi đối tượng
    List<KhuyenMai> getTatCaKhuyenMaiDoiTuong() throws RemoteException;
    boolean themKhuyenMaiDoiTuong(KhuyenMai km, String doiTuong, double chietKhau) throws RemoteException;
    boolean capNhatKhuyenMaiDoiTuong(String maKMCu, String maKMMoi, String ten, Date start, Date end, double chietKhau, String doiTuong) throws RemoteException;
    double getChietKhauHieuLucTheoDoiTuong(String doiTuong) throws RemoteException;
}
