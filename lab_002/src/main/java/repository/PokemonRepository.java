package repository;

import classes.Pokemon;
import classes.Trainer;
import datastore.DataStore;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

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
        throw new UnsupportedOperationException("Nie mam jeszcze :(");
    }

    @Override
    public void update(Pokemon entity) {
        store.updatePokemon(entity);
    }

}
