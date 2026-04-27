package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "LoaiTau")
public class LoaiTau implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "maLoaiTau", length = 20)
    private String maLoaiTau;

    @Column(columnDefinition = "nvarchar(100)")
    private String tenLoaiTau;
}
