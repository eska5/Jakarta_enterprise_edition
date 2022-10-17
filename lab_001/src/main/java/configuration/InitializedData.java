package configuration;

import classes.Trainer;
import classes.enums.Gender;
import digest.Sha256Utility;
import lombok.SneakyThrows;
import service.TrainerService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.InputStream;
import java.time.LocalDate;

@WebListener
public class InitializedData implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce){
        TrainerService trainerService = (TrainerService) sce.getServletContext().getAttribute("trainerService");
        init(trainerService);
    }

    private synchronized void init(TrainerService trainerService){
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
