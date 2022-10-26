package dto.Pokemon;

import classes.Pokemon;
import classes.Trainer;
import classes.Type;
import classes.enums.Rarity;
import dto.Trainer.GetTrainerResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode
public class UpdatePokemonRequest {
    private String name;
    private String specialAbility;
    private Double power;
    private Rarity rarity;

//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    private byte[] photo;

    public static BiFunction<Pokemon, UpdatePokemonRequest, Pokemon> dtoToEntityUpdater() {
        return (pokemon, request) -> {
                pokemon.setName(request.getName());
                pokemon.setSpecialAbility(request.getSpecialAbility());
                pokemon.setPower(request.getPower());
                pokemon.setRarity(request.getRarity());
                return pokemon;
        };
    }
}
