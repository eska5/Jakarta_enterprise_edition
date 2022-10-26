package servlet;

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

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@WebServlet(urlPatterns = {
        PokemonServlet.Paths.POKEMON + "/*",
        PokemonServlet.Paths.POKEMONS,
})
@MultipartConfig(maxFileSize = 400 * 1024)
public class PokemonServlet extends HttpServlet {
    private PokemonService pokemonService;
    private TypeService typeService;

    @Inject
    public PokemonServlet(PokemonService pokemonService, TypeService typeService){
        this.pokemonService = pokemonService;
        this.typeService = typeService;
    }

    public static class Paths {
        public static final String POKEMON = "/api/pokemon";
        public static final String POKEMONS = "/api/pokemons";
        public static final String PHOTO = "photo";
    }

    private final Jsonb jsonb = JsonbBuilder.create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (PokemonServlet.Paths.POKEMON.equals(servletPath)) {
            if (isPokemonPhotoInPath(request, response)) {
                getPokemonPhoto(request, response);
            } else {
                getPokemon(request, response);
            }
        } else if (PokemonServlet.Paths.POKEMONS.equals(servletPath)) {
            getPokemons(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String servletPath = request.getServletPath();

        if (PokemonServlet.Paths.POKEMON.equals(servletPath)){
            if (isPokemonPhotoInPath(request, response)) {
                putPokemonPhoto(request, response);
            }
            putTypePokemon(request, response);
        }
    }

    private void putTypePokemon(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = ServletUtility.parseRequestPath(request).replaceAll("/","");
        Optional<Pokemon> pokemon = pokemonService.find(code);

        if (pokemon.isPresent()){
            UpdatePokemonRequest requestBody = jsonb.fromJson(request.getInputStream(), UpdatePokemonRequest.class);
            UpdatePokemonRequest.dtoToEntityUpdater().apply(pokemon.get(), requestBody);
            pokemonService.update(pokemon.get());
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (Paths.POKEMON.equals(request.getServletPath())){
            deleteTypePokemon(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void deleteTypePokemon(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String name = ServletUtility.parseRequestPath(request).replaceAll("/","");
        Optional<Pokemon> pokemon = pokemonService.find(name);

        if (pokemon.isPresent()) {
            pokemonService.delete(pokemon.get().getName());
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }


    private void getPokemon(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = ServletUtility.parseRequestPath(request).replaceAll("/","");
        Optional<Pokemon> pokemon = pokemonService.find(name);
        if (pokemon.isPresent()){
            response.setContentType("application/json");
            response.getWriter()
                    .write(jsonb.toJson(GetPokemonResponse.entityToDtoMapper().apply(pokemon.get())));
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void getPokemons(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.getWriter().write(jsonb.toJson(GetPokemonsResponse.entityToDtoMapper().apply(pokemonService.findAll())));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void getPokemonPhoto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] urlParts = request.getPathInfo().split("/");
        String name = urlParts[1];
        Optional<Pokemon> pokemon = pokemonService.find(name);
        if (pokemon.isPresent()) {
            try {
                response.setContentLength(pokemon.get().getPhoto().length);
                response.getOutputStream().write(pokemon.get().getPhoto());
//                response.addHeader("Content-Type", "image/jpeg");
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (NullPointerException ex) {
                response.getWriter().write("This pokemon does not have a photo");
//                response.addHeader("Content-Type", "application/json");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @SneakyThrows
    private void putPokemonPhoto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] urlParts = request.getPathInfo().split("/");
        String name = urlParts[1];
        Optional<Pokemon> pokemon = pokemonService.find(name);
        if (pokemon.isPresent()) {
            Part photo = request.getPart("photo");
            if (photo != null) {
                if (pokemon.get().getPhoto() != null) {
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } else {
                    response.setStatus(HttpServletResponse.SC_CREATED);
                }
                pokemonService.updatePhoto(name, photo.getInputStream());
            }
        }
    }

    private void deletePokemonPhoto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] urlParts = request.getPathInfo().split("/");
        String name = urlParts[1];
        Optional<Pokemon> pokemon = pokemonService.find(name);
        if (pokemon.isPresent()) {
            if (pokemon.get().getPhoto() != null) {
                pokemonService.deletePhoto(name);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.getWriter().write("This trainer does not have a photo");
                response.addHeader("Content-Type", "application/json");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private Boolean isPokemonPhotoInPath(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] urlParts = new String[1];
        try {
            System.out.println(request);
            if (request.getPathInfo() != null) {
                urlParts = request.getPathInfo().split("/");
            }
        } catch (IllegalArgumentException ex){
            System.out.println("POMOCY");
        }
        if (urlParts.length == 3) {
            if (PokemonServlet.Paths.PHOTO.equals(urlParts[2])) {
                return true;
            } else {
                return false;
            }
        } else if (urlParts.length == 2) {
            return false;
        } else {
            return false;
        }
    }

    private void deletePokemon(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = ServletUtility.parseRequestPath(request).replaceAll("/","");
        Optional<Pokemon> pokemon = pokemonService.find(code);

        if (pokemon.isPresent()){
            pokemonService.delete(pokemon.get().getName());
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String servletPath = ServletUtility.parseRequestPath(request);

        if (Paths.POKEMONS.equals(request.getServletPath())) {
            if (isPokemonPhotoInPath(request, response)) {
                putPokemonPhoto(request, response);
            }
            postTypePokemon(request, response);
        }
    }

    private void postTypePokemon(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CreatePokemonRequest requestBody = jsonb.fromJson(request.getInputStream(), CreatePokemonRequest.class);

        Pokemon pokemon = CreatePokemonRequest
                .dtoToEntityMapper(typeName -> typeService.find(typeName).orElse(null), () -> null)
                .apply(requestBody);
        try {
            pokemonService.create(pokemon);
            response.setStatus(HttpServletResponse.SC_CREATED);
        }  catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}

