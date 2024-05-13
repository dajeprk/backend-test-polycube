package kr.co.polycube.backendtest.Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "lotto")
public class Lotto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number1")
    @NotNull()
    private int number1;
    @Column(name = "number2")
    @NotNull()
    private int number2;
    @Column(name = "number3")
    @NotNull()
    private int number3;
    @Column(name = "number4")
    @NotNull()
    private int number4;
    @Column(name = "number5")
    @NotNull()
    private int number5;
    @Column(name = "number6")
    @NotNull()
    private int number6;

    public void setNumbers(List<Integer> lottoNumbers) {
        this.number1 = lottoNumbers.get(0);
        this.number2 = lottoNumbers.get(1);
        this.number3 = lottoNumbers.get(2);
        this.number4 = lottoNumbers.get(3);
        this.number5 = lottoNumbers.get(4);
        this.number6 = lottoNumbers.get(5);
    }

    public List<Integer> getNumbers() {
        return Arrays.asList(number1, number2, number3, number4, number5, number6);
    }
}
