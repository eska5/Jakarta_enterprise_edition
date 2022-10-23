package datastore;

import classes.Pokemon;
import classes.Trainer;
import classes.Type;
import lombok.extern.java.Log;
import serialization.CloningUtility;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log
@ApplicationScoped
public class DataStore {
    private Set<Trainer> trainers = new HashSet<>();
    private Set<Pokemon> pokemons = new HashSet<>();
    private Set<Type> types = new HashSet<>();

//    TRAINERS
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
//    END OF TRAINERS

//    POKEMON

    public synchronized List<Pokemon> findAllPokemons(){
        return pokemons.stream()
                .map(CloningUtility::clone)
                .collect(Collectors.toList());
    }

    public synchronized Optional<Pokemon> findPokemon(String name){
        return pokemons.stream()
                .filter(pokemon -> pokemon.getName().equals(name))
                .findFirst()
                .map(CloningUtility::clone);
    }

    public synchronized void createPokemon(Pokemon pokemon) throws IllegalArgumentException {
        findPokemon(pokemon.getName()).ifPresentOrElse(
                original -> {
                    throw new IllegalArgumentException(
                            String.format("Login \"%s\" is already taken", pokemon.getName()));
                },
                () -> pokemons.add(CloningUtility.clone(pokemon)));
    }

    public synchronized void updatePokemon(Pokemon pokemon) throws IllegalArgumentException {
        findPokemon(pokemon.getName()).ifPresentOrElse(
                original -> {
                    pokemons.remove(original);
                    pokemons.add(CloningUtility.clone(pokemon));
                },
                () -> {
                    throw new IllegalArgumentException(
                            String.format("User with login \"%s\" does not exist", pokemon.getName()));
                });
    }
//    END OF POKEMON

//    TYPE

    public synchronized List<Type> findAllTypes(){
        return types.stream()
                .map(CloningUtility::clone)
                .collect(Collectors.toList());
    }

    public synchronized Optional<Type> findType(String typeName){
        return types.stream()
                .filter(type -> type.getTypeName().equals(typeName))
                .findFirst()
                .map(CloningUtility::clone);
    }

    public synchronized void createType(Type type) throws IllegalArgumentException {
        findType(type.getTypeName()).ifPresentOrElse(
                original -> {
                    throw new IllegalArgumentException(
                            String.format("Login \"%s\" is already taken", type.getTypeName()));
                },
                () -> types.add(CloningUtility.clone(type)));
    }

    public synchronized void updateType(Type type) throws IllegalArgumentException {
        findType(type.getTypeName()).ifPresentOrElse(
                original -> {
                    types.remove(original);
                    types.add(CloningUtility.clone(type));
                },
                () -> {
                    throw new IllegalArgumentException(
                            String.format("User with login \"%s\" does not exist", type.getTypeName()));
                });
    }
//    END OF TYPE




}
