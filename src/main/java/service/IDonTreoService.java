package service;

import entity.DonTreoDat;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IDonTreoService extends Remote {
    void themDonTreo(DonTreoDat don) throws RemoteException;
    List<DonTreoDat> layDanhSachDonTreo() throws RemoteException;
    DonTreoDat layDonTreo(String maDon) throws RemoteException;
    boolean xoaDonTreo(String maDon) throws RemoteException;
    List<DonTreoDat> layDonTreoTheoCCCD(String cccd) throws RemoteException;
    List<DonTreoDat> layDonTreoTheoSDT(String sdt) throws RemoteException;
    void xoaDonHetHan() throws RemoteException;
    List<String> layDanhSachMaGheDangTreo(String maLichTrinh, String maGaDi, String maGaDen) throws RemoteException;
}
