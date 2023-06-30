package pl.pacinho.onearmedprogrammer.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import pl.pacinho.onearmedprogrammer.model.enums.Sign;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Spin {

    @Id
    @GenericGenerator(name = "spinIdGen", strategy = "increment")
    @GeneratedValue(generator = "spinIdGen")
    private Long id;

    private int idx;

    @OneToOne
    @JoinColumn(name = "SPIN_ROUND_ID")
    private SpinRound spinRound;

    @Enumerated(EnumType.STRING)
    private Sign sign;

    private int signIdx;

    private int section;

    public Spin(int idx, SpinRound spinRound, Sign sign, int signIdx, int section) {
        this.idx = idx;
        this.spinRound = spinRound;
        this.sign = sign;
        this.signIdx = signIdx;
        this.section = section;
    }
}
