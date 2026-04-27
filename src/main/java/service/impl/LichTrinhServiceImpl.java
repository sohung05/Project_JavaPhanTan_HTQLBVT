package service.impl;

import entity.LichTrinh;
import jakarta.persistence.EntityManager;
import service.ILichTrinhService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class LichTrinhServiceImpl extends UnicastRemoteObject implements ILichTrinhService {
    private EntityManager em;

    public LichTrinhServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public List<LichTrinh> getAll() throws RemoteException {
        return em.createQuery("SELECT lt FROM LichTrinh lt", LichTrinh.class).getResultList();
    }

    @Override
    public LichTrinh findById(String maLT) throws RemoteException {
        return em.find(LichTrinh.class, maLT);
    }
}
