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
@Table(name = "ChoNgoi")
public class ChoNgoi implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "maChoNgoi", length = 20)
    private String maChoNgoi;

    @Transient
    private String tenChoNgoi;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "maToa")
    private Toa toa;

    @Column(columnDefinition = "nvarchar(255)")
    private String moTa;

    private int viTri;
    private double gia;
}
