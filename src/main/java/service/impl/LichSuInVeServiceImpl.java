package service.impl;

import dao.LichSuInVe_DAO;
import entity.LichSuInVe;
import jakarta.persistence.EntityManager;
import service.ILichSuInVeService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class LichSuInVeServiceImpl extends UnicastRemoteObject implements ILichSuInVeService {
    private LichSuInVe_DAO lichSuInVeDAO;

    public LichSuInVeServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.lichSuInVeDAO = new LichSuInVe_DAO();
    }

    @Override
    public boolean insert(LichSuInVe ls) throws RemoteException {
        return lichSuInVeDAO.insert(ls);
    }

    @Override
    public List<LichSuInVe> findByMaVe(String maVe) throws RemoteException {
        return lichSuInVeDAO.findByMaVe(maVe);
    }

    @Override
    public int countPrintTimes(String maVe) throws RemoteException {
        return lichSuInVeDAO.countPrintTimes(maVe);
    }

    @Override
    public LichSuInVe findLastPrint(String maVe) throws RemoteException {
        return lichSuInVeDAO.findLastPrint(maVe);
    }
}
