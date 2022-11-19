package dto.Type;

import classes.Pokemon;
import classes.Trainer;
import classes.Type;
import classes.enums.MatchUp;
import dto.Pokemon.GetPokemonsResponse;
import dto.Trainer.GetTrainerResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode
public class GetTypeResponse {
    private String typeName;
    private double multiplier;
    private MatchUp combatList;
    private GetPokemonsResponse pokemons;

    public static Function<Type, GetTypeResponse> entityToDtoMapper(List<Pokemon> pokemons) {
        return type -> GetTypeResponse.builder()
                .typeName(type.getTypeName())
                .multiplier(type.getMultiplier())
                .combatList(type.getCombatList())
                .pokemons(GetPokemonsResponse.entityToDtoMapper().apply(pokemons))
                .build();
    }

}
