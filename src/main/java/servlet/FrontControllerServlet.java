package servlet;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import util.Mapping;
import util.UrlMethode;

public class FrontControllerServlet extends HttpServlet {
    
    // Déclaration de la liste pour stocker les noms des contrôleurs trouvés
    private List<String> listNomController = new ArrayList<>();
    private Map<UrlMethode, Mapping> urlMappings = new HashMap<>();

    @Override
    public void init() throws ServletException {
        super.init();

        ServletContext context = getServletContext();
        Object controllersAttribute = context.getAttribute(FrontControllerListner.CONTROLLERS_ATTRIBUTE);
        Object urlMappingsAttribute = context.getAttribute(FrontControllerListner.URL_MAPPINGS_ATTRIBUTE);

        if (controllersAttribute instanceof List<?>) {
            listNomController = (List<String>) controllersAttribute;
        }

        if (urlMappingsAttribute instanceof Map<?, ?>) {
            urlMappings = (Map<UrlMethode, Mapping>) urlMappingsAttribute;
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            processRequest(req, res);
        }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            processRequest(req, res);
        }
    
    public void processRequest(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        String route = url.substring(contextPath.length());
        String methodeHttp = req.getMethod();

        if (url.endsWith("test.html")) {
            res.setContentType("text/html;charset=UTF-8");
            req.getServletContext().getNamedDispatcher("default").forward(req, res);
        } else {
            res.setContentType("text/plain;charset=UTF-8");
            try (PrintWriter out = res.getWriter()) {
                afficherControllers(out);
                out.println();

                UrlMethode urlMethode = new UrlMethode(route, methodeHttp);
                Mapping mapping = urlMappings.get(urlMethode);

                if (mapping == null) {
                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.println("Aucune méthode trouvée pour l'URL : " + route + " en " + methodeHttp);
                    out.println();
                    afficherUrlsDisponibles(out);
                    return;
                }

                Object resultat = executeMapping(mapping);
                out.println("URL trouvée : " + route);
                out.println("Méthode HTTP : " + methodeHttp);
                out.println("Controller : " + mapping.getClassName());
                out.println("Méthode : " + mapping.getMethodName());
                out.println("Résultat : " + resultat);
            }
        }
    }

    private void afficherControllers(PrintWriter out) {
        out.println("Liste des controllers détectés :");

        if (listNomController.isEmpty()) {
            out.println("Aucun controller trouvé.");
            return;
        }

        for (String nomController : listNomController) {
            out.println("-> " + nomController);
        }
    }

    private void afficherUrlsDisponibles(PrintWriter out) {
        out.println("URLs disponibles :");

        if (urlMappings.isEmpty()) {
            out.println("Aucune URL disponible.");
            return;
        }

        for (Map.Entry<UrlMethode, Mapping> entry : urlMappings.entrySet()) {
            Mapping mapping = entry.getValue();
            UrlMethode urlMethode = entry.getKey();
            out.println("-> [" + urlMethode.getMethode() + "] " + urlMethode.getUrl() + " : " + mapping.getClassName() + "." + mapping.getMethodName() + "()");
        }
    }

    private Object executeMapping(Mapping mapping) throws ServletException {
        try {
            Class<?> clazz = Class.forName(mapping.getClassName());
            Object controller = clazz.getDeclaredConstructor().newInstance();
            Method methode = clazz.getDeclaredMethod(mapping.getMethodName());

            return methode.invoke(controller);
        } catch (Exception e) {
            throw new ServletException("Erreur pendant l'exécution du mapping", e);
        }
    }
}
