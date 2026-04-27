package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "KhachHang")
public class KhachHang implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "maKH", length = 20)
    private String maKH;

    @Column(columnDefinition = "nvarchar(20)")
    private String CCCD;

    @Column(columnDefinition = "nvarchar(100)")
    private String hoTen;

    @Column(length = 100)
    private String email;

    @Column(length = 15)
    private String SDT;

    @Column(columnDefinition = "nvarchar(50)")
    private String doiTuong; // SinhVien, TreEm, NguoiLon, NguoiCaoTuoi
}
