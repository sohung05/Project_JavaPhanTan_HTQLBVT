package entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "BangGioGa")
@IdClass(BangGioGaId.class)
public class BangGioGa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "maTuyen")
    private Tuyen tuyen;

    @Id
    @ManyToOne
    @JoinColumn(name = "maGa")
    private Ga ga;

    private int stt;
    private double khoangCachTuGaTruoc;
}
