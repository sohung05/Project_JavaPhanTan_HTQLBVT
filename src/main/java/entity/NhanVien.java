package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "NhanVien")
public class NhanVien implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "maNhanVien", length = 20)
    private String maNhanVien;

    @Column(columnDefinition = "nvarchar(20)")
    private String CCCD;

    @Column(columnDefinition = "nvarchar(100)")
    private String hoTen;

    @Column(length = 15)
    private String SDT;

    @Column(length = 100)
    private String email;

    @Column(columnDefinition = "nvarchar(255)")
    private String diaChi;

    private int chucVu;
    private boolean trangThai;
    private LocalDate ngaySinh;
    private LocalDate ngayVaoLam;

    @Column(columnDefinition = "nvarchar(10)")
    private String gioiTinh;
}