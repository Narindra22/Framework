package servlet;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import util.ClasseUtilitaire; // On importe notre classe utilitaire simple
import util.Mapping;

public class FrontControllerServlet extends HttpServlet {
    
    // Déclaration de la liste pour stocker les noms des contrôleurs trouvés
    private List<String> listNomController = new ArrayList<>();
    private Map<String, Mapping> urlMappings = new HashMap<>();
    private ClasseUtilitaire util = new ClasseUtilitaire();

    @Override
    public void init() throws ServletException {
        try {
            super.init();
            System.out.println("Démarrage du scan manuel...");
            
            // On veut chercher les classes dans le package nommé "controller"
            String packageClasse = "controller";

            // Remplissage de la liste avec notre méthode utilitaire
            listNomController = util.findController(packageClasse);
            urlMappings = util.findUrlMappings(packageClasse);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Échec du scan au démarrage : " + e.getMessage(), e);
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

        if (url.endsWith("test.html")) {
            res.setContentType("text/html;charset=UTF-8");
            req.getServletContext().getNamedDispatcher("default").forward(req, res);
        } else {
            res.setContentType("text/plain;charset=UTF-8");
            try (PrintWriter out = res.getWriter()) {
                afficherControllers(out);
                out.println();

                Mapping mapping = urlMappings.get(route);

                if (mapping == null) {
                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.println("Aucune méthode trouvée pour l'URL : " + route);
                    out.println();
                    afficherUrlsDisponibles(out);
                    return;
                }

                Object resultat = executeMapping(mapping);
                out.println("URL trouvée : " + route);
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

        for (Map.Entry<String, Mapping> entry : urlMappings.entrySet()) {
            Mapping mapping = entry.getValue();
            out.println("-> " + entry.getKey() + " : " + mapping.getClassName() + "." + mapping.getMethodName() + "()");
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
