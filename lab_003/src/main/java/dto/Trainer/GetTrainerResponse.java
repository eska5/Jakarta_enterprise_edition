package dto.Trainer;

import classes.Pokemon;
import classes.Trainer;
import classes.enums.Gender;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode
public class GetTrainerResponse {
    private String login;
    private Integer age;
    private LocalDate creationDate;
    private Gender gender;

//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    private byte[] profilePicture;


    public static Function<Trainer, GetTrainerResponse> entityToDtoMapper() {
        return trainer -> GetTrainerResponse.builder()
                .login(trainer.getLogin())
                .age(trainer.getAge())
                .creationDate(trainer.getCreationDate())
                .gender(trainer.getGender())
//                .profilePicture(trainer.getProfilePicture())
                .build();
    }
}