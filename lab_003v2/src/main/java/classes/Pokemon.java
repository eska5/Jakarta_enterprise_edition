package classes;

import classes.enums.Rarity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "pokemons")
public class Pokemon implements Serializable {
    @Id
    private String name;
    private String specialAbility;
    private Double power;
    private Rarity rarity;

    @ManyToOne
    @JoinColumn(name = "type")
    private Type type;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] photo;
}

