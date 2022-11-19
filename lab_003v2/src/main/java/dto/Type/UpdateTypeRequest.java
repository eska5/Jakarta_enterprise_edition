package dto.Type;

import classes.Pokemon;
import classes.Trainer;
import classes.Type;
import classes.enums.MatchUp;
import dto.Trainer.GetTrainerResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode
public class UpdateTypeRequest {
    private String typeName;
    private double multiplier;
    private MatchUp combatList;

//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    private byte[] icon;

    public static Function<UpdateTypeRequest , Type> dtoToEntityUpdater(String typeName, List<Pokemon> pokemons) {
        return request -> Type.builder()
                .typeName(request.getTypeName())
                .multiplier(request.getMultiplier())
                .combatList(request.getCombatList())
                .pokemons(pokemons)
                .typeName(typeName)
                .build();
//                type.setIcon(request.getIcon());

    }

}
