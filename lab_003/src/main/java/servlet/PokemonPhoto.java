package servlet;

import javax.servlet.annotation.MultipartConfig;

@MultipartConfig(maxFileSize = 400 * 1024)
public class PokemonPhoto {
    public static class Paths {
        public static final String PHOTO = "/api/photos";
    }
}
