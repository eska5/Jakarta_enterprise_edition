package dto.Type;

import classes.Trainer;
import classes.Type;
import classes.enums.MatchUp;
import dto.Trainer.GetTrainerResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    public static BiFunction<Type,UpdateTypeRequest , Type> dtoToEntityUpdater(
    ) {
        return (type, request) -> {
                type.setTypeName(request.getTypeName());
                type.setMultiplier(request.getMultiplier());
                type.setCombatList(request.getCombatList());
//                type.setIcon(request.getIcon());
                return type;
        };
    }

}
