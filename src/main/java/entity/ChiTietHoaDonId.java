package entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietHoaDonId implements Serializable {
    private String hoaDon;
    private String ve;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietHoaDonId that = (ChiTietHoaDonId) o;
        return Objects.equals(hoaDon, that.hoaDon) && Objects.equals(ve, that.ve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hoaDon, ve);
    }
}
