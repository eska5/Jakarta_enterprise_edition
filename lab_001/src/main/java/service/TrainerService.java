package service;

import classes.Trainer;
import repository.TrainerRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class TrainerService {
    private TrainerRepository repository;

    public TrainerService(TrainerRepository repository){
        this.repository = repository;
    }

    public Optional<Trainer> find(String login) {
        return repository.find(login);
    }

    public Optional<Trainer> find(String login, char[] password){
        return repository.findByLoginAndPassword(login, password);
    }

    public void create(Trainer trainer) {
        repository.create(trainer);
    }

    public List<Trainer> findAll(){
        return repository.findAll();
    }

    public void updateProfilePicture(String login, InputStream is) {
        repository.find(login).ifPresent(trainer -> {
            try {
                trainer.setProfilePicture(is.readAllBytes());
                repository.update(trainer);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }

    public void deleteProfilePicture(String login){
        repository.find(login).ifPresent(trainer -> {
            trainer.setProfilePicture(null);
            repository.update(trainer);
        });
    }

}
