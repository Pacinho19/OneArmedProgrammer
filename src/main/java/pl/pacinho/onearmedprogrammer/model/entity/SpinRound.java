package pl.pacinho.onearmedprogrammer.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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

    public SpinRound(Game game, int number) {
        this.game = game;
        this.number = number;
    }
}