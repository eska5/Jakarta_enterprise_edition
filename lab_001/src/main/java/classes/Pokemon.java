package classes;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode
public class Pokemon {
    private String name;
    private String specialAbility;
    private Double power;
    private Rarity rarity;
    private Type type;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] photo;
}

enum Rarity {
    common, uncommon, rare, epic, legendary
}