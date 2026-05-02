package service.impl;

import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import service.ITaiKhoanService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TaiKhoanServiceImpl extends UnicastRemoteObject implements ITaiKhoanService {
    private TaiKhoan_DAO taiKhoanDAO;

    public TaiKhoanServiceImpl() throws RemoteException {
        super();
        this.taiKhoanDAO = new TaiKhoan_DAO();
    }

    @Override
    public TaiKhoan dangNhap(String tenTaiKhoan, String matKhau) throws RemoteException {
        return taiKhoanDAO.dangNhap(tenTaiKhoan, matKhau);
    }
}
