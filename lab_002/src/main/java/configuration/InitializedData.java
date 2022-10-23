package configuration;

import classes.Trainer;
import classes.Type;
import classes.enums.Gender;
import classes.enums.MatchUp;
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
//
//        Map<String,MatchUp> testMap =Map.of("woda", MatchUp.weak);
//        Map<String,MatchUp> testMap2 =Map.of("woda", MatchUp.weak,"ogien", MatchUp.neutral);
//        Map<String,MatchUp> testMap3 =Map.of("woda", MatchUp.weak, "ziemia", MatchUp.strong,"ogien", MatchUp.neutral);
//        Type type1 = Type.builder()
//                .typeName("Ogien")
//                .multiplier(48)
//                .combatList(testMap)
//                .icon(getResourceAsByteArray("/trainerPictures/profile1.jpeg"))
//                .build();
//        Type type2 = Type.builder()
//                .typeName("Ziemia")
//                .multiplier(23)
//                .combatList(testMap2)
//                .icon(getResourceAsByteArray("/trainerPictures/profile2.jpeg"))
//                .build();
//        Type type3 = Type.builder()
//                .typeName("Woda")
//                .multiplier(34.5)
//                .combatList(testMap3)
//                .icon(getResourceAsByteArray("/trainerPictures/profile3.jpeg"))
//                .build();
//        typeService.create(type1);
//        typeService.create(type2);
//        typeService.create(type3);

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
