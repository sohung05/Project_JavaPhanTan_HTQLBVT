package entity;

import lombok.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BangGioGaId implements Serializable {
    private String tuyen; // Matches field name in BangGioGa (tuyen)
    private String ga;    // Matches field name in BangGioGa (ga)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BangGioGaId that = (BangGioGaId) o;
        return Objects.equals(tuyen, that.tuyen) && Objects.equals(ga, that.ga);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tuyen, ga);
    }
}
