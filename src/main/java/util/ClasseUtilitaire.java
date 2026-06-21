package util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import annotation.Controller;

public class ClasseUtilitaire {

    public List<String> findController(String packageClasse) throws Exception {
        List<String> liste = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(packageClasse);

        if (resource == null) {
            return liste;
        }
        File dossier = new File(resource.getFile().replace("%20", " "));

        if (dossier.exists() && dossier.isDirectory()) {
            for (File fichier : dossier.listFiles()) {
                if (fichier.getName().endsWith(".class")) {
                    String nomClasseSeul = fichier.getName().substring(0, fichier.getName().length() - 6);
                    String nomComplet = packageClasse + "." + nomClasseSeul;

                    Class<?> clazz = Class.forName(nomComplet);
                    if (clazz.isAnnotationPresent(Controller.class)) {
                        liste.add(nomComplet); 
                    }
                }
            }
        }
        return liste;
    }
}