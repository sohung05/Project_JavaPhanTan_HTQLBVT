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
@Table(name = "Toa")
public class Toa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "maToa", length = 20)
    private String maToa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "soHieuTau")
    private ChuyenTau chuyenTau;

    private int soToa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maLoaiToa")
    private LoaiToa loaiToa;
}
