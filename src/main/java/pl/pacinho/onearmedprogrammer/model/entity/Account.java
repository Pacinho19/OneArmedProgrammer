package pl.pacinho.onearmedprogrammer.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
public class Account {

    @Id
    @GenericGenerator(name = "accIdGen", strategy = "increment")
    @GeneratedValue(generator = "accIdGen")
    private Long id;

    private String name;

    @Setter
    @Column(name = "PASSWORD")
    private String password;

    @Setter
    @Column(precision = 15, scale = 2)
    private BigDecimal balance;

    public Account(String name, String password, BigDecimal balance) {
        this.name = name;
        this.password = password;
        this.balance = balance;
    }

}
