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
@Table(name = "TaiKhoan")
public class TaiKhoan implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "userName", length = 50)
    private String tenTaiKhoan;

    @Column(name = "passWord", length = 200)
    private String matKhau;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "maNhanVien")
    private NhanVien nhanVien;

    public String getMaNhanVien() {
        return nhanVien != null ? nhanVien.getMaNhanVien() : null;
    }

    public void setMaNhanVien(String maNhanVien) {
        if (this.nhanVien == null) {
            this.nhanVien = new NhanVien();
        }
        this.nhanVien.setMaNhanVien(maNhanVien);
    }
}