package repository;

import classes.Pokemon;
import classes.Trainer;
import classes.Type;
import datastore.DataStore;
import dto.Pokemon.UpdatePokemonRequest;
import serialization.CloningUtility;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Dependent
public class PokemonRepository implements Repository<Pokemon, String> {
    private DataStore store;

    @Inject
    public PokemonRepository(DataStore store) {
        this.store = store;
    }

    @Override
    public Optional<Pokemon> find(String name) {
        return store.findPokemon(name);
    }

    @Override
    public List<Pokemon> findAll() {
        return store.findAllPokemons();
    }

    @Override
    public void create(Pokemon entity) {
        store.createPokemon(entity);
    }

    @Override
    public void delete(Pokemon entity) {
        store.deletePokemon(entity.getName());
    }

    @Override
    public void update(Pokemon entity) {
        store.updatePokemon(entity);
    }

    public Optional<Pokemon> findByVarAndType(String var, Type type) {
        return store.findAllPokemons().stream()
                .filter(pokemon -> pokemon.getType().equals(type))
                .filter(pokemon -> pokemon.getName().equals(var))
                .findFirst()
                .map(CloningUtility::clone);
    }

    public List<Pokemon> findAllByType(Type type) {
        return store.findAllPokemons().stream()
                .filter(pokemon -> pokemon.getType().equals(type))
                .map(CloningUtility::clone)
                .collect(Collectors.toList());
    }
}
