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
@Table(name = "ChuyenTau")
public class ChuyenTau implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "soHieuTau", length = 20)
    private String soHieuTau;

    private double tocDo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maLoaiTau")
    private LoaiTau loaiTau;

    private Integer namSanXuat;
}
