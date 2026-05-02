package service.impl;

import dao.Dashboard_DAO;
import service.IDashboardService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.Map;

public class DashboardServiceImpl extends UnicastRemoteObject implements IDashboardService {
    private Dashboard_DAO dashboardDAO;

    public DashboardServiceImpl() throws RemoteException {
        super();
        this.dashboardDAO = new Dashboard_DAO();
    }

    @Override
    public Map<String, Double> getThongKeTongQuan() throws RemoteException {
        return dashboardDAO.getThongKeTongQuan();
    }

    @Override
    public int getSoKhuyenMaiSapHetHan(int soNgay) throws RemoteException {
        return dashboardDAO.getSoKhuyenMaiSapHetHan(soNgay);
    }

    @Override
    public Map<Integer, Double> getDoanhThuTheoThang(int nam) throws RemoteException {
        return dashboardDAO.getDoanhThuTheoThang(nam);
    }

    @Override
    public Map<Integer, Integer> getSoVeTheoThang(int nam) throws RemoteException {
        return dashboardDAO.getSoVeTheoThang(nam);
    }

    @Override
    public Map<String, Double> getThongKeNgay(LocalDate ngay) throws RemoteException {
        return dashboardDAO.getThongKeNgay(ngay);
    }

    @Override
    public Map<String, Double> getDoanhThuTheoTuyenTrongThang(int thang, int nam) throws RemoteException {
        return dashboardDAO.getDoanhThuTheoTuyenTrongThang(thang, nam);
    }

    @Override
    public Map<String, Integer> getSoVeTheoTuyen(int ngay, int thang, int top) throws RemoteException {
        return dashboardDAO.getSoVeTheoTuyen(ngay, thang, top);
    }

    @Override
    public Map<String, Integer> getSoChoNgoiConTrongTheoTuyen(LocalDate ngay) throws RemoteException {
        return dashboardDAO.getSoChoNgoiConTrongTheoTuyen(ngay);
    }

    @Override
    public Map<String, Integer> getSoChoNgoiConTrongTheoTuyen(int ngay, int thang) throws RemoteException {
        return dashboardDAO.getSoChoNgoiConTrongTheoTuyen(ngay, thang);
    }
}
