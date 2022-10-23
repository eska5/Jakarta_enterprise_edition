package configuration;

import datastore.DataStore;
import repository.TrainerRepository;
import service.TrainerService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class CreateService implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DataStore dataSource = (DataStore) sce.getServletContext().getAttribute("datasource");

        sce.getServletContext().setAttribute("trainerService", new TrainerService(new TrainerRepository(dataSource)));
    }
}
