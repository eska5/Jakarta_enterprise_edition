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

    @Singular
    private List<String> pokemons;

    public static Function<Collection<Pokemon>, GetPokemonsResponse> entityToDtoMapper(){
        return pokemons -> {
            GetPokemonsResponse.GetPokemonsResponseBuilder response = GetPokemonsResponse.builder();
            pokemons.stream()
                    .map(Pokemon::getName)
                    .forEach(response::pokemon);
            return response.build();
        };
    }
}
