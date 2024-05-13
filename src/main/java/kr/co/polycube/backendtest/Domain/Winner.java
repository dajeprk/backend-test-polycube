package kr.co.polycube.backendtest.Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Winner {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "lotto_id")
    @NotNull()
    private Long lotto_id;

    @Column(name = "rank")
    @NotNull()
    private int rank;
}
