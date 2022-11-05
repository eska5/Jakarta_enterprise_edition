package dto.Type;

import classes.Trainer;
import classes.Type;
import classes.enums.MatchUp;
import dto.Trainer.GetTrainersResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
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
public class GetTypesResponse {
    private String typeName;

    @Singular
    private List<String> types;

    public static Function<Collection<classes.Type>, GetTypesResponse> entityToDtoMapper(){
        return types -> {
            GetTypesResponse.GetTypesResponseBuilder response = GetTypesResponse.builder();
            types.stream()
                    .map(Type::getTypeName)
                    .forEach(response::type);
            return response.build();
        };
    }
}
