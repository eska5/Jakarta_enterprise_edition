package repository;

import classes.Trainer;
import datastore.DataStore;

import java.util.List;
import java.util.Optional;

public class TrainerRepository implements Repository<Trainer, String>{
    private DataStore store;
    public TrainerRepository(DataStore store) {
        this.store = store;
    }

    @Override
    public Optional<Trainer> find(String login) {
        return store.findTrainer(login);
    }

    @Override
    public List<Trainer> findAll() {
        return store.findAllTrainers();
    }

    @Override
    public void create(Trainer entity) {
        store.createTrainer(entity);
    }

    @Override
    public void delete(Trainer entity) {
        throw new UnsupportedOperationException("Nie mam jeszcze :(");
    }

    @Override
    public void update(Trainer entity) {
        store.updateTrainer(entity);
    }

    public Optional<Trainer> findByLoginAndPassword(String login, char[] password) {
        return store.findAllTrainers().stream()
                .filter(trainer -> trainer.getLogin().equals(login) && trainer.getPassword().equals(password))
                .findFirst();
    }

}
