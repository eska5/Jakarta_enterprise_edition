package servlet;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;

@MultipartConfig(maxFileSize =  400 * 1024)
public class TrainerProfilePicture  extends HttpServlet {
    public static class Paths {
        public static final String PROFILEPICTURE = "/api/profile_pictures";
    }
}
