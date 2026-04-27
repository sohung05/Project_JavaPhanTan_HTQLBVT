package entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "KhuyenMai")
public class KhuyenMai implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "maKhuyenMai", length = 20)
    private String maKhuyenMai;

    @Column(columnDefinition = "nvarchar(100)")
    private String tenKhuyenMai;

    @Column(length = 10)
    private String loaiKhuyenMai;

    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;
    private boolean trangThai;
    @Transient
    private int soVe;
    @Transient
    private double chietKhau;

    @Transient
    private String doiTuongApDung;

    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, String loaiKhuyenMai, LocalDateTime thoiGianBatDau, LocalDateTime thoiGianKetThuc, boolean trangThai) {
        this.maKhuyenMai = maKhuyenMai;
        this.tenKhuyenMai = tenKhuyenMai;
        this.loaiKhuyenMai = loaiKhuyenMai;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.trangThai = trangThai;
    }

    public static String taoMaKhuyenMaiTheoNgay(java.util.Date ngay, int index) {
        String dateStr = new java.text.SimpleDateFormat("ddMMyyyy").format(ngay);
        return String.format("KM%s%02d", dateStr, index % 100);
    }
}
