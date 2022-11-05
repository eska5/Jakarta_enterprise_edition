package Controller;

import classes.Pokemon;
import classes.Type;
import dto.Pokemon.CreatePokemonRequest;
import dto.Pokemon.GetPokemonResponse;
import dto.Pokemon.GetPokemonsResponse;
import dto.Pokemon.UpdatePokemonRequest;
import dto.Type.CreateTypeRequest;
import dto.Type.GetTypeResponse;
import dto.Type.GetTypesResponse;
import dto.Type.UpdateTypeRequest;
import lombok.SneakyThrows;
import service.PokemonService;
import service.TypeService;
import servlet.ServletUtility;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.print.attribute.standard.Media;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Path("/types")
public class TypeController {

    private PokemonService pokemonService;

    private TypeService typeService;

    public  TypeController() {
    }

    @Inject
    public void setService(TypeService typeService, PokemonService pokemonService){
        this.typeService = typeService;
        this.pokemonService = pokemonService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTypes(){
        return Response
                .ok(GetTypesResponse.entityToDtoMapper().apply(typeService.findAll()))
                .build();
    }

    @GET
    @Path("/{typeName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getType(@PathParam("typeName") String typeName){
        Optional<Type> type = typeService.find(typeName);
        System.out.println(type);
        if (type.isPresent()) {
            return Response
                    .ok(GetTypeResponse.entityToDtoMapper(type.get().getPokemons()).apply(type.get()))
                    .build();
        } else {
            return Response.ok("Type with given name does not exist")
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createType(CreateTypeRequest request) {
        Optional<Type> type = typeService.find(request.getTypeName());
        if (type.isEmpty()) {
            Type newType = CreateTypeRequest
                    .dtoToEntityMapper()
                    .apply(request);
            typeService.create(newType);
            return Response.ok("Type with name: " +request.getTypeName() + "created successfully")
                    .status(Response.Status.CREATED)
                    .build();
        } else {
            return Response.ok("Type with this name already exists")
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }

    @DELETE
    @Path("/{typeName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteType(@PathParam("typeName") String typeName) {
        typeName = typeName;
        Optional<Type> type = typeService.find(typeName);
        if (type.isPresent()) {
            typeService.delete(typeName);
            return Response.ok("Type" + typeName + " was removed successfully")
                    .status(Response.Status.OK)
                    .build();
        } else {
            return Response.ok("Type not found")
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @PUT
    @Path("/{typeName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateType(@PathParam("typeName") String typeName, UpdateTypeRequest request) {
        typeName = typeName;
        Optional<Type> type = typeService.find(typeName);
        if (type.isPresent()) {
            Type newType = UpdateTypeRequest
                    .dtoToEntityUpdater(typeName, type.get().getPokemons())
                    .apply(request);
            typeService.update(newType);
            return Response.ok("Type" + typeName + " was updated successfully")
                    .status(Response.Status.OK)
                    .build();
        } else {
            return Response.ok("Type not found")
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @GET
    @Path("/{typeName}/pokemons/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPokemon(@PathParam("typeName") String typeName, @PathParam("name") String name){
        typeName = typeName;
        Optional<Type> type = typeService.find(typeName);
        System.out.println(type);
        if (type.isPresent()) {
            Optional<Pokemon> pokemon = pokemonService.findForType(name,typeName);
            System.out.println(pokemon);
            if (pokemon.isPresent()){
                return Response
                        .ok(GetPokemonResponse.entityToDtoMapper().apply(pokemon.get()))
                        .build();
            } else {
                return Response.ok("Pokemon with given name does not exist")
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
        } else {
            return Response.ok("Type with given typeName does not exist")
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @GET
    @Path("/{typeName}/pokemons")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPokemonFromType(@PathParam("typeName") String typeName) {
        typeName = typeName;

        Optional<Type> type = typeService.find(typeName);
        if (type.isPresent()) {

            List<Pokemon> pokemons = pokemonService.findAllForType(typeName);
            return Response
                    .ok(GetPokemonsResponse.entityToDtoMapper().apply(pokemons))
                    .build();
        } else {
            return Response
                    .ok("Type with given name was not found")
                    .build();
        }
    }

    @POST
    @Path("/{typeName}/pokemons")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPokemon(@PathParam("typeName") String typeName, CreatePokemonRequest request) {
        Optional<Type> type = typeService.find(typeName);
        if (type.isPresent()) {
            Optional<Pokemon> pokemon2 = pokemonService.find(request.getName());
            if (pokemon2.isEmpty()) {
                Pokemon pokemon = CreatePokemonRequest
                        .dtoToEntityMapper(type_Name -> typeService.find(type_Name).orElse(null), () -> null)
                        .apply(request);

                pokemonService.create(pokemon);
                return Response
                        .ok("Pokemon with name: " + pokemon.getName() + " created successfully")
                        .status(Response.Status.CREATED)
                        .build();
            }
            else{
                return Response
                        .ok("Pokemon with that name already exists")
                        .status(Response.Status.CONFLICT)
                        .build();
            }
        } else {
            return Response
                    .ok("Type not found")
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @PUT
    @Path("/{typeName}/pokemons/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePokemon(@PathParam("typeName") String typeName,@PathParam("name") String name ,
                                  UpdatePokemonRequest request) {
        Optional<Type> type = typeService.find(typeName);
        if (type.isPresent()) {
            Optional<Pokemon> pokemon = pokemonService.findForType(name, typeName);
            if (pokemon.isPresent()){
                Pokemon newPokemon = UpdatePokemonRequest
                        .dtoToEntityUpdater()
                        .apply(pokemon.get(), request);
                pokemonService.update(newPokemon);
                return Response
                        .ok("Pokemon with name: " + name + " updated successfully")
                        .build();
            } else {
                return Response
                        .ok("Pokemon not found")
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
        } else {
            return Response
                    .ok("Type not found")
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @DELETE
    @Path("/{typeName}/pokemons/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePokemon(@PathParam("typeName") String typeName, @PathParam("name") String name){
        typeName = typeName;

        Optional<Type> type = typeService.find(typeName);
        if (type.isPresent()) {
            Optional<Pokemon> pokemon = pokemonService.findForType(name, typeName);
            if (pokemon.isPresent()) {
                pokemonService.delete(name);
                return Response
                        .ok("Pokemon with name: " + name + "removed successfully")
                        .build();
            } else {
                return Response.ok("Pokemon with given name not found")
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
        } else {
            return Response.ok("Type with given name not found")
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }
}
