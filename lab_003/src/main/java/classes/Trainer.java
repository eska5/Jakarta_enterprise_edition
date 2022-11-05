package classes;

import classes.enums.Gender;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode
public class Trainer implements Serializable {
    private String login;
    private Integer age;
    private LocalDate creationDate;
    @ToString.Exclude
    private String password;
    private Gender gender;
    private List<Pokemon> pokemonList;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] profilePicture;
}

