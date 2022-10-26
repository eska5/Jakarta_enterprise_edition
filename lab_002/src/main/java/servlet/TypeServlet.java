package servlet;

import classes.Pokemon;
import classes.Trainer;
import classes.Type;
import dto.Trainer.GetTrainerResponse;
import dto.Trainer.GetTrainersResponse;
import dto.Type.CreateTypeRequest;
import dto.Type.GetTypeResponse;
import dto.Type.GetTypesResponse;
import dto.Type.UpdateTypeRequest;
import lombok.SneakyThrows;
import service.PokemonService;
import service.TrainerService;
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
import java.net.http.HttpHeaders;
import java.util.Optional;


@WebServlet(urlPatterns = {
        TypeServlet.Paths.TYPE + "/*",
        TypeServlet.Paths.TYPES,
})
@MultipartConfig(maxFileSize = 400 * 1024)
public class TypeServlet extends HttpServlet {
    private final TypeService typeService;
    private final PokemonService pokemonService;

    @Inject
    public TypeServlet(TypeService typeService, PokemonService pokemonService){
        this.typeService = typeService;
        this.pokemonService = pokemonService;
    }

    public static class Paths {
        public static final String TYPE = "/api/type";
        public static final String TYPES = "/api/types";
        public static final String ICON = "icon";
    }

    private final Jsonb jsonb = JsonbBuilder.create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (TypeServlet.Paths.TYPE.equals(servletPath)) {
            if (isTypeIconInPath(request, response)) {
                getTypesIcon(request, response);
            }
                getType(request, response);
        } else if (TypeServlet.Paths.TYPES.equals(servletPath)) {
            getTypes(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String servletPath = request.getServletPath();

        if (TypeServlet.Paths.TYPE.equals(servletPath)) {
            if (isTypeIconInPath(request, response)) {
                putTypeIcon(request, response);
            }
            putType(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String servletPath = ServletUtility.parseRequestPath(request);

        if (TypeServlet.Paths.TYPES.equals(request.getServletPath())) {
            if (isTypeIconInPath(request, response)) {
                putTypeIcon(request, response);
            }
            postType(request, response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = ServletUtility.parseRequestPath(request);

        if (TypeServlet.Paths.TYPE.equals(request.getServletPath())){
            deleteType(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void getType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String typeName = ServletUtility.parseRequestPath(request).replaceAll("/","");
        Optional<Type> type = typeService.find(typeName);
        if (type.isPresent()){
            type.get().setPokemons(pokemonService.findAllForType(typeName));
            response.setContentType("application/json");
            response.getWriter()
                    .write(jsonb.toJson(GetTypeResponse.entityToDtoMapper().apply(type.get())));
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private void getTypes(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.getWriter().write(jsonb.toJson(GetTypesResponse.entityToDtoMapper().apply(typeService.findAll())));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void getTypesIcon(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] urlParts = request.getPathInfo().split("/");
        String login = urlParts[1];
        Optional<Type> type = typeService.find(login);
        if (type.isPresent()) {
            try {
                response.setContentLength(type.get().getIcon().length);
                response.getOutputStream().write(type.get().getIcon());
//                response.addHeader("Content-Type", "image/jpeg");
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (NullPointerException ex) {
                response.getWriter().write("This type does not have an icon");
                response.addHeader("Content-Type", "application/json");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @SneakyThrows
    private void putTypeIcon(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] urlParts = request.getPathInfo().split("/");
        String login = urlParts[1];
        Optional<Type> type = typeService.find(login);
        if (type.isPresent()) {
            Part icon = request.getPart("icon");
            if (icon != null) {
                if (type.get().getIcon() != null) {
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } else {
                    response.setStatus(HttpServletResponse.SC_CREATED);
                }
                typeService.updateIcon(login, icon.getInputStream());
            }
        }
    }

    private void deleteTypeIcon(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] urlParts = request.getPathInfo().split("/");
        String login = urlParts[1];
        Optional<Type> type = typeService.find(login);
        if (type.isPresent()) {
            if (type.get().getIcon() != null) {
                typeService.deleteIcon(login);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.getWriter().write("This trainer does not have profile picture");
                response.addHeader("Content-Type", "application/json");
            }
        }
    }

    private Boolean isTypeIconInPath(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            if (TypeServlet.Paths.ICON.equals(urlParts[2])) {
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

    private void deleteType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = ServletUtility.parseRequestPath(request).replaceAll("/","");
        Optional<Type> type = typeService.find(code);

        if (type.isPresent()){
            typeService.delete(type.get().getTypeName());
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void putType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = ServletUtility.parseRequestPath(request).replaceAll("/","");
        Optional<Type> type = typeService.find(code);

        if (type.isPresent()){
            UpdateTypeRequest requestBody = jsonb.fromJson(request.getInputStream(), UpdateTypeRequest.class);
            UpdateTypeRequest.dtoToEntityUpdater().apply(type.get(), requestBody);
            typeService.update(type.get());
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    private void postType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CreateTypeRequest requestBody = jsonb.fromJson(request.getInputStream(), CreateTypeRequest.class);

        Type type = CreateTypeRequest
                .dtoToEntityMapper()
                .apply(requestBody);
        try {
            typeService.create(type);
            response.setStatus(HttpServletResponse.SC_CREATED);
        }  catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
