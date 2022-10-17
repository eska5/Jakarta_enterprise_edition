package classes;

import java.util.List;

public class Trainer {
    private String name;
    private Integer age;
    private char[] password;
    private Gender gender;
    private List<Pokemon> pokemonList;
    private byte[] profilePicture;
}

enum Gender {
    male, female, other
}
