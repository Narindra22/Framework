package servlet;

import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebListener;
import util.ClasseUtilitaire;
import util.Mapping;
import util.UrlMethode;

@WebListener
public class FrontControllerListner implements ServletContextListener {

    public static final String CONTROLLERS_ATTRIBUTE = "frontController.controllers";
    public static final String URL_MAPPINGS_ATTRIBUTE = "frontController.urlMappings";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServletContext context = sce.getServletContext();
            ClasseUtilitaire util = new ClasseUtilitaire();
            String packageClasse = "controller";

            List<String> controllers = util.findController(packageClasse);
            Map<UrlMethode, Mapping> urlMappings = util.findUrlMappings(packageClasse);

            context.setAttribute(CONTROLLERS_ATTRIBUTE, controllers);
            context.setAttribute(URL_MAPPINGS_ATTRIBUTE, urlMappings);
        } catch (Exception e) {
            throw new RuntimeException(new ServletException("Echec du scan au demarrage", e));
        }
    }
}
