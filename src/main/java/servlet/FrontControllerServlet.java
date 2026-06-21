package servlet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import util.ClasseUtilitaire; // On importe notre classe utilitaire simple

public class FrontControllerServlet extends HttpServlet {
    
    // Déclaration de la liste pour stocker les noms des contrôleurs trouvés
    private List<String> listNomController = new ArrayList<>();
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

        if (url.endsWith("test.html")) {
            res.setContentType("text/html;charset=UTF-8");
            req.getServletContext().getNamedDispatcher("default").forward(req, res);
        } else {
            res.setContentType("text/plain;charset=UTF-8");
            try (PrintWriter out = res.getWriter()) {
                out.println(url);
                out.println("Liste des contrôleurs détectés");
                
                // Boucle simple pour afficher la liste à l'écran
                if (listNomController.isEmpty()) {
                    out.println("Aucun contrôleur trouvé.");
                } else {
                    for (String nom : listNomController) {
                        out.println("-> " + nom);
                    }
                }
            }
        }
    }
}