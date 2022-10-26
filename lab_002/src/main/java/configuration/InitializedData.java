package configuration;

import classes.Pokemon;
import classes.Trainer;
import classes.Type;
import classes.enums.Gender;
import classes.enums.MatchUp;
import classes.enums.Rarity;
import digest.Sha256Utility;
import lombok.SneakyThrows;
import service.PokemonService;
import service.TrainerService;
import service.TypeService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class InitializedData {

    private final TrainerService trainerService;
    private final TypeService typeService;
    private final PokemonService pokemonService;

    @Inject
    public InitializedData(TrainerService trainerService, TypeService typeService, PokemonService pokemonService){
        this.trainerService = trainerService;
        this.typeService = typeService;
        this.pokemonService = pokemonService;
    }

    public void contextInitialized(@Observes @Initialized(ApplicationScoped.class) Object init)
    {
        init();
    }

    private synchronized void init(){

        // TYPES
        List<Pokemon> emptyPokemonList = new ArrayList<Pokemon>();
        Type type1 = Type.builder()
                .typeName("Ogien")
                .multiplier(48)
                .combatList(MatchUp.weak)
                .pokemons(emptyPokemonList)
                .icon(getResourceAsByteArray("/trainerPictures/profile1.jpeg"))
                .build();
        Type type2 = Type.builder()
                .typeName("Ziemia")
                .multiplier(23)
                .combatList(MatchUp.neutral)
                .pokemons(emptyPokemonList)
                .icon(getResourceAsByteArray("/trainerPictures/profile2.jpeg"))
                .build();
        Type type3 = Type.builder()
                .typeName("Woda")
                .multiplier(34.5)
                .combatList(MatchUp.strong)
                .pokemons(emptyPokemonList)
                .icon(getResourceAsByteArray("/trainerPictures/profile3.jpeg"))
                .build();
        typeService.create(type1);
        typeService.create(type2);
        typeService.create(type3);

        //  POKEMON
        Pokemon pokemon = Pokemon.builder()
                .name("Charizard")
                .specialAbility("SMASH")
                .power(2137.0)
                .rarity(Rarity.epic)
                .type(type1)
                .photo(getResourceAsByteArray("/trainerPictures/profile3.jpeg"))
                .build();
        Pokemon pokemon2 = Pokemon.builder()
                .name("pikachu")
                .specialAbility("electrocute")
                .power(650.0)
                .rarity(Rarity.legendary)
                .type(type1)
                .photo(getResourceAsByteArray("/trainerPictures/profile2.jpeg"))
                .build();
        Pokemon pokemon3 = Pokemon.builder()
                .name("squirtle")
                .specialAbility("water_punch")
                .power(220.0)
                .rarity(Rarity.common)
                .type(type2)
                .photo(getResourceAsByteArray("/trainerPictures/profile1.jpeg"))
                .build();
        Pokemon pokemon4 = Pokemon.builder()
                .name("turtwig")
                .specialAbility("ground_hog")
                .power(120.0)
                .rarity(Rarity.uncommon)
                .type(type3)
                .photo(getResourceAsByteArray("/trainerPictures/profile4.jpeg"))
                .build();

        pokemonService.create(pokemon);
        pokemonService.create(pokemon2);
        pokemonService.create(pokemon3);
        pokemonService.create(pokemon4);


        //  TRAINERS
        Trainer jakub = Trainer.builder()
                .login("MADJAKUB")
                .age(48)
                .gender(Gender.male)
                .creationDate(LocalDate.now())
                .password(Sha256Utility.hash("Haslo"))
                .profilePicture(getResourceAsByteArray("/trainerPictures/profile1.jpeg"))
                .build();

        Trainer lukasz = Trainer.builder()
                .login("MasnyLukasz")
                .age(27)
                .gender(Gender.male)
                .creationDate(LocalDate.now())
                .password(Sha256Utility.hash("trytytyty"))
                .profilePicture(getResourceAsByteArray("/trainerPictures/profile2.jpeg"))
                .build();

        Trainer lukasz2 = Trainer.builder()
                .login("RobertKubica")
                .age(19)
                .gender(Gender.male)
                .creationDate(LocalDate.now())
                .password(Sha256Utility.hash("POMOCY123"))
                .profilePicture(getResourceAsByteArray("/trainerPictures/profile3.jpeg"))
                .build();

        Trainer ada = Trainer.builder()
                .login("dobrylogin")
                .age(19)
                .gender(Gender.female)
                .creationDate(LocalDate.now())
                .password(Sha256Utility.hash("hasloDobre"))
                .profilePicture(getResourceAsByteArray("/trainerPictures/profile4.jpeg"))
                .build();

        trainerService.create(jakub);
        trainerService.create(lukasz);
        trainerService.create(lukasz2);
        trainerService.create(ada);
    }

    @SneakyThrows
    private byte[] getResourceAsByteArray(String name){
        try (InputStream is = this.getClass().getResourceAsStream(name)) {
            return is.readAllBytes();
        }
    }

}
