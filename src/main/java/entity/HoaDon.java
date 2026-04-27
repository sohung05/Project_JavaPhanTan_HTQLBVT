package entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "danhSachChiTiet")
@Builder
@Entity
@Table(name = "HoaDon")
public class HoaDon implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "maHoaDon", length = 20)
    private String maHoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhanVien")
    private NhanVien nhanVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKH")
    private KhachHang khachHang;

    private LocalDateTime gioTao;
    private LocalDateTime ngayTao;
    private double tongTien;
    private boolean trangThai;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ChiTietHoaDon> danhSachChiTiet = new ArrayList<>();

    @Transient
    private String khuyenMai;

    public void themChiTiet(ChiTietHoaDon cthd) {
        danhSachChiTiet.add(cthd);
        cthd.setHoaDon(this);
    }

    public double tinhTongTien() {
        double tong = 0;
        for (ChiTietHoaDon cthd : danhSachChiTiet) {
            tong += cthd.getGiaVe() * cthd.getSoLuong();
        }
        return tong;
    }

    public double tinhThanhTien() {
        double tong = 0;
        for (ChiTietHoaDon cthd : danhSachChiTiet) {
            tong += cthd.tinhThanhTien();
        }
        return tong;
    }

    public double tinhTongGiamGia() {
        double tong = 0;
        for (ChiTietHoaDon cthd : danhSachChiTiet) {
            tong += cthd.getMucGiam() * cthd.getSoLuong();
        }
        return tong;
    }
}
