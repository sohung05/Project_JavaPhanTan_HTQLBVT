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
@Table(name = "LichTrinh")
public class LichTrinh implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "maLichTrinh", length = 20)
    private String maLichTrinh;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "soHieuTau")
    private ChuyenTau chuyenTau;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "maTuyen")
    private Tuyen tuyen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "maGaDi")
    private Ga gaDi;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "maGaDen")
    private Ga gaDen;

    private LocalDateTime gioKhoiHanh;
    @Column(name = "gioDenDuKien")
    private LocalDateTime gioDen;
    private boolean trangThai;
}
