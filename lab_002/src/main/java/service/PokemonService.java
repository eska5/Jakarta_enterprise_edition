package service;

import classes.Pokemon;
import dto.Pokemon.GetPokemonsResponse;
import lombok.NoArgsConstructor;
import repository.PokemonRepository;
import repository.TypeRepository;

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
    private TypeRepository typeRepository;

    @Inject
    public PokemonService(PokemonRepository pokemonRepository, TypeRepository typeRepository){
        this.pokemonRepository = pokemonRepository;
        this.typeRepository = typeRepository;
    }

    public Optional<Pokemon> find(String name) {
        return pokemonRepository.find(name);
    }

    public void create(Pokemon pokemon) {
        pokemonRepository.create(pokemon);
    }

    public void update(Pokemon pokemon) {
        pokemonRepository.update(pokemon);
    }

    public void delete(String name) {
        pokemonRepository.delete(pokemonRepository.find(name).orElseThrow());
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

    public List<Pokemon> findAllForType(String typeName) {
        return pokemonRepository.findAllByType(typeRepository.find(typeName).orElseThrow());
    }

    public Optional<Pokemon> findForType(String var1, String typeName) {
        return pokemonRepository.findByVarAndType(var1, typeRepository.find(typeName).orElseThrow());
    }
}
