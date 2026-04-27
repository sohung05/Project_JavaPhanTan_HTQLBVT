package entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "ChiTietKhuyenMai")
public class ChiTietKhuyenMai implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maChiTiet")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKhuyenMai")
    private KhuyenMai khuyenMai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maHoaDon")
    private HoaDon hoaDon;

    @Column(columnDefinition = "nvarchar(255)")
    private String dieuKien;

    private double chietKhau;
}
