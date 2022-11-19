package Controller;

import dto.Pokemon.GetPokemonsResponse;
import service.PokemonService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
public class pokemonController {
    private PokemonService pokemonService;

    public pokemonController(){
    }

    @Inject
    public void setService(PokemonService pokemonService){
        this.pokemonService = pokemonService;
    }

    @GET
    @Path("/pokemons")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPokemons() {
        return Response
                .ok(GetPokemonsResponse.entityToDtoMapper().apply(pokemonService.findAll()))
                .build();
    }
}
