package pl.pacinho.onearmedprogrammer.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import pl.pacinho.onearmedprogrammer.config.GameConfig;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Game {

    @Id
    @GenericGenerator(name = "accIdGen", strategy = "increment")
    @GeneratedValue(generator = "accIdGen")
    private Long id;

    private String uuid;

    @OneToOne
    @JoinColumn(name = "ACC_ID")
    private Account account;

    @Setter
    @Column(precision = 15, scale = 2)
    private BigDecimal rate;

    @Setter
    private int leftRounds;

    public Game(Account account) {
        this.leftRounds = GameConfig.ROUNDS_COUNT;
        this.account = account;
        this.uuid = UUID.randomUUID().toString();
    }

    public void decrementLeftRounds() {
        this.leftRounds = leftRounds-1;
    }
}
