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
@Table(name = "GheGiuCho")
public class GheGiuCho implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "maChoNgoi", length = 20)
    private String maChoNgoi;

    @Column(length = 20)
    private String maDonTreo;

    @Column(length = 20)
    private String maLichTrinh;

    private LocalDateTime thoiGianGiuCho;

    @Transient
    private static final int PHUT_GIU_CHO = 15;

    public GheGiuCho(String maChoNgoi, String maDonTreo, String maLichTrinh) {
        this.maChoNgoi = maChoNgoi;
        this.maDonTreo = maDonTreo;
        this.maLichTrinh = maLichTrinh;
        this.thoiGianGiuCho = LocalDateTime.now();
    }

    public void giaHanThoiGian(int phut) {
        if (this.thoiGianGiuCho != null) {
            this.thoiGianGiuCho = this.thoiGianGiuCho.plusMinutes(phut);
        } else {
            this.thoiGianGiuCho = LocalDateTime.now().plusMinutes(phut);
        }
    }

    public boolean conTrongThoiGianGiuCho() {
        if (thoiGianGiuCho == null) return false;
        LocalDateTime thoiGianHetHan = thoiGianGiuCho.plusMinutes(PHUT_GIU_CHO);
        return LocalDateTime.now().isBefore(thoiGianHetHan);
    }

    public long getSoGiayConLai() {
        if (thoiGianGiuCho == null) return 0;
        LocalDateTime thoiGianHetHan = thoiGianGiuCho.plusMinutes(PHUT_GIU_CHO);
        long giay = java.time.Duration.between(LocalDateTime.now(), thoiGianHetHan).getSeconds();
        return giay > 0 ? giay : 0;
    }
}
