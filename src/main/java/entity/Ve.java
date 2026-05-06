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
@Table(name = "Ve")
public class Ve implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "maVe", length = 20)
    private String maVe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "maLoaiVe")
    private LoaiVe loaiVe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "maLichTrinh")
    private LichTrinh lichTrinh;

    @Column(length = 50)
    private String maVach;

    private LocalDateTime thoiGianLenTau;
    private double giaVe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "maKH")
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "maToa")
    private Toa toa;

    private boolean trangThai;

    @Column(columnDefinition = "nvarchar(100)")
    private String tenKhachHang;

    @Column(length = 20)
    private String soCCCD;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "maChoNgoi")
    private ChoNgoi choNgoi;
}
