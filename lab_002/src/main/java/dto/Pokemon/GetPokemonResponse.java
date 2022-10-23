package dto.Pokemon;

import classes.Pokemon;
import classes.Trainer;
import classes.Type;
import classes.enums.Rarity;
import dto.Trainer.GetTrainerResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.function.Function;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode
public class GetPokemonResponse {
    private String name;
    private String specialAbility;
    private Double power;
    private Rarity rarity;
    private Type type;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] photo;

    public static Function<Pokemon, GetPokemonResponse> entityToDtoMapper() {
        return pokemon -> GetPokemonResponse.builder()
                .name(pokemon.getName())
                .specialAbility(pokemon.getSpecialAbility())
                .power(pokemon.getPower())
                .rarity(pokemon.getRarity())
                .type(pokemon.getType())
                .photo(pokemon.getPhoto())
                .build();
    }
}
