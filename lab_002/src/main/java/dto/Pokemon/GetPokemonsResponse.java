package dto.Pokemon;

import classes.Pokemon;
import classes.Trainer;
import classes.Type;
import classes.enums.Rarity;
import dto.Trainer.GetTrainersResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode
public class GetPokemonsResponse {
    private String name;
    private Double power;
    private Rarity rarity;
    private Type type;

    @Singular
    private List<Pokemon> pokemons;

    public static Function<Collection<classes.Pokemon>, GetPokemonsResponse> entityToDtoMapper(){
        return pokemons -> {
            GetPokemonsResponse.GetPokemonsResponseBuilder response = GetPokemonsResponse.builder();
            pokemons.stream()
                    .map(pokemon -> Pokemon.builder()
                            .name(pokemon.getName())
                            .power(pokemon.getPower())
                            .rarity(pokemon.getRarity())
                            .type(pokemon.getType())
                            .build())
                    .forEach(response::pokemon);
            return response.build();
        };
    }
}
