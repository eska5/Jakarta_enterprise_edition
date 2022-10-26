package dto.Pokemon;

import classes.Pokemon;
import classes.Trainer;
import classes.Type;
import classes.enums.Rarity;
import dto.Trainer.GetTrainerResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode
public class CreatePokemonRequest {
    private String name;
    private String specialAbility;
    private Double power;
    private Rarity rarity;
    private String typeName;

//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    private byte[] photo;

    public static Function<CreatePokemonRequest, Pokemon> dtoToEntityMapper(
            Function<String, Type> typeFunction,
            Supplier<Trainer> trainerSupplier
    ) {
        return request -> Pokemon.builder()
                .name(request.getName())
                .specialAbility(request.getSpecialAbility())
                .power(request.getPower())
                .rarity(request.getRarity())
                .type(typeFunction.apply(request.getTypeName()))
//                .photo(pokemon.getPhoto())
                .build();
    }
}
