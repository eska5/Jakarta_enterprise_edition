package dto.Trainer;

import classes.Trainer;
import classes.enums.Gender;
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
public class GetTrainersResponse {

    @Singular
    private List<String> trainers;

    public static Function<Collection<classes.Trainer>, GetTrainersResponse> entityToDtoMapper(){
        return trainers -> {
            GetTrainersResponse.GetTrainersResponseBuilder response = GetTrainersResponse.builder();
            trainers.stream()
                    .map(Trainer::getLogin)
                    .forEach(response::trainer);
            return response.build();
        };
    }
}
