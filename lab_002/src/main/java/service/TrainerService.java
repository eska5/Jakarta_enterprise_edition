package service;

import classes.Trainer;
import lombok.NoArgsConstructor;
import repository.TrainerRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
public class TrainerService {
    private TrainerRepository trainerRepository;

    @Inject
    public TrainerService(TrainerRepository trainerRepository){
        this.trainerRepository = trainerRepository;
    }

    public Optional<Trainer> find(String login) {
        return trainerRepository.find(login);
    }

    public Optional<Trainer> find(String login, char[] password){
        return trainerRepository.findByLoginAndPassword(login, password);
    }

    public void create(Trainer trainer) {
        trainerRepository.create(trainer);
    }

    public List<Trainer> findAll(){
        return trainerRepository.findAll();
    }

    public void updateProfilePicture(String login, InputStream is) {
        trainerRepository.find(login).ifPresent(trainer -> {
            try {
                trainer.setProfilePicture(is.readAllBytes());
                trainerRepository.update(trainer);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }

    public void deleteProfilePicture(String login){
        trainerRepository.find(login).ifPresent(trainer -> {
            trainer.setProfilePicture(null);
            trainerRepository.update(trainer);
        });
    }

}
