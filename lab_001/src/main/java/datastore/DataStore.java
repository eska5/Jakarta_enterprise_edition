package datastore;

import classes.Pokemon;
import classes.Trainer;
import classes.Type;
import lombok.extern.java.Log;
import serialization.CloningUtility;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log
public class DataStore {
    private Set<Trainer> trainers = new HashSet<>();
//    private Set<Pokemon> pokemons = new HashSet<>();
//    private Set<Type> types = new HashSet<>();

    public synchronized List<Trainer> findAllTrainers(){
        return trainers.stream()
                .map(CloningUtility::clone)
                .collect(Collectors.toList());
    }

    public synchronized Optional<Trainer> findTrainer(String login){
        return trainers.stream()
                .filter(trainer -> trainer.getLogin().equals(login))
                .findFirst()
                .map(CloningUtility::clone);
    }

    public synchronized void createTrainer(Trainer trainer) throws IllegalArgumentException {
        findTrainer(trainer.getLogin()).ifPresentOrElse(
                original -> {
                    throw new IllegalArgumentException(
                            String.format("Login \"%s\" is already taken", trainer.getLogin()));
                },
                () -> trainers.add(CloningUtility.clone(trainer)));
    }

    public synchronized void updateTrainer(Trainer trainer) throws IllegalArgumentException {
        findTrainer(trainer.getLogin()).ifPresentOrElse(
                original -> {
                    trainers.remove(original);
                    trainers.add(CloningUtility.clone(trainer));
                },
                () -> {
                    throw new IllegalArgumentException(
                            String.format("User with login \"%s\" does not exist", trainer.getLogin()));
                });
    }

}
