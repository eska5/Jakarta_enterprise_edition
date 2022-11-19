package classes;

import classes.enums.MatchUp;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "types")
public class Type implements Serializable {
    @Id
    private String typeName;
    private double multiplier;
    private MatchUp combatList;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "type", cascade = CascadeType.REMOVE)
    private List <Pokemon> pokemons;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] icon;
}



