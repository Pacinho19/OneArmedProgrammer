package pl.pacinho.onearmedprogrammer.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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
    private int slot1;
    @Setter
    private int slot2;
    @Setter
    private int slot3;

    public Game(Account account) {
        this.account = account;
        this.uuid = UUID.randomUUID().toString();
    }
}
