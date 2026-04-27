package entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "hoaDon")
@Builder
@Entity
@IdClass(ChiTietHoaDonId.class)
@Table(name = "ChiTietHoaDon")
public class ChiTietHoaDon implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maHoaDon")
    private HoaDon hoaDon;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maVe")
    private Ve ve;

    private int soLuong;
    private double giaVe;
    private double mucGiam;

    public String getMaVe() {
        return ve != null ? ve.getMaVe() : null;
    }

    public double tinhThanhTien() {
        if (soLuong <= 0 || giaVe <= 0)
            return 0;
        double tienSauGiam = giaVe - mucGiam;
        if (tienSauGiam < 0)
            tienSauGiam = 0;
        return tienSauGiam * soLuong;
    }
}