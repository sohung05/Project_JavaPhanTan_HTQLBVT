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
@Table(name = "Ga")
public class Ga implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "maGa", length = 20)
    private String maGa;

    @Column(columnDefinition = "nvarchar(100)")
    private String tenGa;

    @Column(columnDefinition = "nvarchar(255)")
    private String viTri;
}
