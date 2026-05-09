package service.impl;

import entity.HoaDon;
import entity.ChiTietHoaDon;
import dao.ChiTietHoaDon_DAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import service.IHoaDonService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class HoaDonServiceImpl extends UnicastRemoteObject implements IHoaDonService {
    private EntityManager em;
    private ChiTietHoaDon_DAO chiTietHoaDonDAO;

    public HoaDonServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
        this.chiTietHoaDonDAO = new ChiTietHoaDon_DAO();
    }

    @Override
    public List<HoaDon> getAll() throws RemoteException {
        return em.createQuery("SELECT hd FROM HoaDon hd", HoaDon.class).getResultList();
    }

    @Override
    public boolean them(HoaDon hd) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            // ⚡ CLEANUP: Xóa mọi đơn treo liên quan đến các ghế trong hóa đơn này
            if (hd.getDanhSachChiTiet() != null) {
                for (ChiTietHoaDon ct : hd.getDanhSachChiTiet()) {
                    if (ct.getVe() != null && ct.getVe().getChoNgoi() != null) {
                        String maCho = ct.getVe().getChoNgoi().getMaChoNgoi();
                        
                        // 1. Tìm các mã đơn treo chứa ghế này
                        List<String> listMaDon = em.createNativeQuery(
                            "SELECT DISTINCT maDonTreo FROM ThongTinVeTam WHERE maChoNgoi = :maCho")
                            .setParameter("maCho", maCho)
                            .getResultList();
                        
                        for (String maDon : listMaDon) {
                            // 2. Xóa các vé tạm của đơn đó
                            em.createNativeQuery("DELETE FROM ThongTinVeTam WHERE maDonTreo = :maDon")
                                .setParameter("maDon", maDon)
                                .executeUpdate();
                            // 3. Xóa đơn treo đó
                            em.createNativeQuery("DELETE FROM DonTreoDat WHERE maDonTreo = :maDon")
                                .setParameter("maDon", maDon)
                                .executeUpdate();
                        }
                    }
                }
            }
            
            em.persist(hd);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(HoaDon hd) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(hd);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public HoaDon findById(String maHD) throws RemoteException {
        return em.find(HoaDon.class, maHD);
    }

    @Override
    public HoaDon findByMaHoaDon(String maHD) throws RemoteException {
        return findById(maHD);
    }

    @Override
    public List<ChiTietHoaDon> getChiTietByMaHoaDon(String maHD) throws RemoteException {
        return chiTietHoaDonDAO.findByMaHoaDon(maHD);
    }

    @Override
    public boolean removeChiTiet(String maHD, String maVe) throws RemoteException {
        return chiTietHoaDonDAO.delete(maHD, maVe);
    }
}
