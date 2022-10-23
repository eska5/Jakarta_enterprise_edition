package service;

import classes.Pokemon;
import dto.Pokemon.GetPokemonsResponse;
import lombok.NoArgsConstructor;
import repository.PokemonRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
public class PokemonService {
    private PokemonRepository pokemonRepository;

    @Inject
    public PokemonService(PokemonRepository pokemonRepository){
        this.pokemonRepository = pokemonRepository;
    }

    public Optional<Pokemon> find(String name) {
        return pokemonRepository.find(name);
    }

    public void create(Pokemon pokemon) {
        pokemonRepository.create(pokemon);
    }

    public List<Pokemon> findAll(){
        return pokemonRepository.findAll();
    }

    public void updatePhoto(String name, InputStream is) {
        pokemonRepository.find(name).ifPresent(pokemon -> {
            try {
                pokemon.setPhoto(is.readAllBytes());
                pokemonRepository.update(pokemon);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }

    public void deletePhoto(String name){
        pokemonRepository.find(name).ifPresent(pokemon -> {
            pokemon.setPhoto(null);
            pokemonRepository.update(pokemon);
        });
    }
}
