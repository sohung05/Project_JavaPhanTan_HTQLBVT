package entity;

import jakarta.persistence.*;
import jakarta.persistence.Transient;
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

    @Column(length = 20)
    private String loaiKhuyenMai;

    private java.time.LocalDateTime thoiGianBatDau;
    private java.time.LocalDateTime thoiGianKetThuc;
    private boolean trangThai;
    
    @Transient // Cột này nằm ở bảng ChiTietKhuyenMai, không có trong bảng KhuyenMai
    private double chietKhau;
    
    @Transient // Cột này nằm ở bảng ChiTietKhuyenMai (cột dieuKien), không có trong bảng KhuyenMai
    private String doiTuongApDung; // Đổi lại đúng tên theo Class Diagram

    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, String loaiKhuyenMai, java.time.LocalDateTime thoiGianBatDau, java.time.LocalDateTime thoiGianKetThuc, boolean trangThai) {
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
