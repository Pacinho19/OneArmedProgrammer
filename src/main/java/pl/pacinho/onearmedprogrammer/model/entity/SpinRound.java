package pl.pacinho.onearmedprogrammer.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import pl.pacinho.onearmedprogrammer.model.enums.RoundStatus;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
public class SpinRound {

    @Id
    @GenericGenerator(name = "spinRoundIdGen", strategy = "increment")
    @GeneratedValue(generator = "spinRoundIdGen")
    private Long id;

    private int number;

    @OneToOne
    @JoinColumn(name = "GAME_ID")
    private Game game;

    @Setter
    @Enumerated(EnumType.STRING)
    private RoundStatus status;

    @Setter
    @Column(precision = 15, scale = 2)
    private BigDecimal winAmount;

    @Setter
    private boolean displayed;

    public SpinRound(Game game, int number) {
        this.setStatus(RoundStatus.IN_PROGRESS);
        this.game = game;
        this.number = number;
        this.displayed = false;
    }
}