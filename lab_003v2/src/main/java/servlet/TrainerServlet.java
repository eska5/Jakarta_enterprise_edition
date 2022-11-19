package servlet;

import classes.Trainer;
import dto.Trainer.GetTrainerResponse;
import dto.Trainer.GetTrainersResponse;
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
import java.net.http.HttpHeaders;
import java.util.Optional;

@WebServlet(urlPatterns = {
        TrainerServlet.Paths.TRAINER + "/*",
        TrainerServlet.Paths.TRAINERS,
})
@MultipartConfig(maxFileSize = 400 * 1024)
public class TrainerServlet extends HttpServlet {
    private final TrainerService trainerService;

    @Inject
    public TrainerServlet(TrainerService trainerService){
        this.trainerService = trainerService;
    }

    public static class Paths {
        public static final String TRAINER = "/api/trainer";
        public static final String TRAINERS = "/api/trainers";
        public static final String PROFILEPICTURE = "profilePicture";
    }

    private final Jsonb jsonb = JsonbBuilder.create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (Paths.TRAINER.equals(servletPath)) {
            if (isProfilePictureInPath(request, response)) {
                getTrainerProfilePicture(request, response);
            } else {
                getTrainer(request, response);
            }
        } else if (Paths.TRAINERS.equals(servletPath)) {
            getTrainers(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String servletPath = request.getServletPath();

        if (Paths.TRAINER.equals(servletPath)){
            if (isProfilePictureInPath(request, response)) {
                putTrainerProfilePicture(request, response);
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

        if (Paths.TRAINER.equals(servletPath)){
            if (isProfilePictureInPath(request, response)) {
                deleteTrainerProfilePicture(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void getTrainer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = ServletUtility.parseRequestPath(request).replaceAll("/","");
        Optional<Trainer> trainer = trainerService.find(login);
        if (trainer.isPresent()){
            response.setContentType("application/json");
            response.getWriter()
                    .write(jsonb.toJson(GetTrainerResponse.entityToDtoMapper().apply(trainer.get())));
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void getTrainers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.getWriter().write(jsonb.toJson(GetTrainersResponse.entityToDtoMapper().apply(trainerService.findAll())));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void getTrainerProfilePicture(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] urlParts = request.getPathInfo().split("/");
        String login = urlParts[1];
        Optional<Trainer> trainer = trainerService.find(login);
        if (trainer.isPresent()) {
            try {
                response.setContentLength(trainer.get().getProfilePicture().length);
                response.getOutputStream().write(trainer.get().getProfilePicture());
                response.addHeader("Content-Type", "image/jpeg");
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (NullPointerException ex) {
                response.getWriter().write("This trainer does not have profile picture");
                response.addHeader("Content-Type", "application/json");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @SneakyThrows
    private void putTrainerProfilePicture(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] urlParts = request.getPathInfo().split("/");
        String login = urlParts[1];
        Optional<Trainer> trainer = trainerService.find(login);
        if (trainer.isPresent()) {
            Part profilePicture = request.getPart("profilePicture");
            if (profilePicture != null) {
                if (trainer.get().getProfilePicture() != null) {
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } else {
                    response.setStatus(HttpServletResponse.SC_CREATED);
                }
                trainerService.updateProfilePicture(login, profilePicture.getInputStream());
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void deleteTrainerProfilePicture(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] urlParts = request.getPathInfo().split("/");
        String login = urlParts[1];
        Optional<Trainer> trainer = trainerService.find(login);
        if (trainer.isPresent()) {
            if (trainer.get().getProfilePicture() != null) {
                trainerService.deleteProfilePicture(login);
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

    private Boolean isProfilePictureInPath(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] urlParts = request.getPathInfo().split("/");
        if (urlParts.length == 3) {
            if (Paths.PROFILEPICTURE.equals(urlParts[2])) {
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
