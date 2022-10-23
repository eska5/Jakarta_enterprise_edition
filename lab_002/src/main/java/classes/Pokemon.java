package classes;

import classes.enums.Rarity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode
public class Pokemon implements Serializable {
    private String name;
    private String specialAbility;
    private Double power;
    private Rarity rarity;
    private Type type;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] photo;
}

