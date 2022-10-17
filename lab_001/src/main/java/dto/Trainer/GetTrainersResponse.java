package dto.Trainer;

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
    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString(callSuper = true)
    @EqualsAndHashCode
    public static class Trainer {
        private String login;
        private Integer age;
        private Gender gender;
    }

    @Singular
    private List<Trainer> trainers;

    public static Function<Collection<classes.Trainer>, GetTrainersResponse> entityToDtoMapper(){
        return trainers -> {
            GetTrainersResponse.GetTrainersResponseBuilder response = GetTrainersResponse.builder();
            trainers.stream()
                    .map(trainer -> Trainer.builder()
                            .login(trainer.getLogin())
                            .age(trainer.getAge())
                            .gender(trainer.getGender())
                            .build())
                    .forEach(response::trainer);
            return response.build();
        };
    }
}
