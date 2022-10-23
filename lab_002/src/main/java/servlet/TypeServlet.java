package servlet;

import classes.Trainer;
import classes.Type;
import dto.Trainer.GetTrainerResponse;
import dto.Trainer.GetTrainersResponse;
import dto.Type.GetTypeResponse;
import dto.Type.GetTypesResponse;
import lombok.SneakyThrows;
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
import java.util.Optional;

@WebServlet(urlPatterns = {
        TypeServlet.Paths.TYPE + "/*",
        TypeServlet.Paths.TYPES,
})
@MultipartConfig(maxFileSize = 400 * 1024)
public class TypeServlet extends HttpServlet {
    private final TypeService typeService;

    @Inject
    public TypeServlet(TypeService typeService){
        this.typeService = typeService;
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
            } else {
                getType(request, response);
            }
        } else if (TypeServlet.Paths.TYPES.equals(servletPath)) {
            getType(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String servletPath = request.getServletPath();

        if (TypeServlet.Paths.TYPE.equals(servletPath)){
            if (isTypeIconInPath(request, response)) {
                putTypeIcon(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String servletPath = request.getServletPath();

        if (TypeServlet.Paths.TYPE.equals(servletPath)){
            if (isTypeIconInPath(request, response)) {
                deleteTypeIcon(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void getType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String typeName = ServletUtility.parseRequestPath(request).replaceAll("/","");
        Optional<Type> type = typeService.find(typeName);
        if (type.isPresent()){
            response.setContentType("application/json");
            response.getWriter()
                    .write(jsonb.toJson(GetTypeResponse.entityToDtoMapper().apply(type.get())));
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
                response.addHeader("Content-Type", "image/jpeg");
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
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private Boolean isTypeIconInPath(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] urlParts = request.getPathInfo().split("/");
        if (urlParts.length == 3) {
            if (TypeServlet.Paths.ICON.equals(urlParts[2])) {
                return true;
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return false;
            }
        } else if (urlParts.length == 2) {
            return false;
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }
    }

}
