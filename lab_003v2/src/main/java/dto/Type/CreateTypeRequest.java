package dto.Type;

import classes.Pokemon;
import classes.Trainer;
import classes.Type;
import classes.enums.MatchUp;
import dto.Trainer.GetTrainerResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
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
public class CreateTypeRequest {
    private String typeName;
    private double multiplier;
    private MatchUp combatList;
    private List<Pokemon> pokemons;

//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    private byte[] icon;

    public static Function<CreateTypeRequest, Type> dtoToEntityMapper() {
        return type -> Type.builder()
                .typeName(type.getTypeName())
                .multiplier(type.getMultiplier())
                .combatList(type.getCombatList())
                .pokemons(new ArrayList<>())
//                .icon(type.getIcon())
                .build();
    }

}
