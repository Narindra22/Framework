package util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import annotation.Controller;
import annotation.UrlMapping;

public class ClasseUtilitaire {

    public List<String> findController(String packageClasse) throws Exception {
        Set<String> liste = new LinkedHashSet<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(packageClasse.replace(".", "/"));

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();

            if ("file".equals(resource.getProtocol())) {
                File dossier = new File(URLDecoder.decode(resource.getFile(), "UTF-8"));
                scanDossierController(dossier, packageClasse, liste);
            } else if ("jar".equals(resource.getProtocol())) {
                scanJarController(resource, packageClasse, liste);
            }
        }

        return new ArrayList<>(liste);
    }

    private void scanDossierController(File dossier, String packageClasse, Set<String> liste) throws Exception {
        if (!dossier.exists() || !dossier.isDirectory()) {
            return;
        }

        File[] fichiers = dossier.listFiles();
        if (fichiers == null) {
            return;
        }

        for (File fichier : fichiers) {
            if (fichier.getName().endsWith(".class")) {
                String nomClasseSeul = fichier.getName().substring(0, fichier.getName().length() - 6);
                ajouterControllerSiAnnote(packageClasse + "." + nomClasseSeul, liste);
            }
        }
    }

    private void scanJarController(URL resource, String packageClasse, Set<String> liste) throws Exception {
        String packagePath = packageClasse.replace(".", "/") + "/";
        JarURLConnection connection = (JarURLConnection) resource.openConnection();

        try (JarFile jarFile = connection.getJarFile()) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String nomEntry = entry.getName();

                if (nomEntry.startsWith(packagePath) && nomEntry.endsWith(".class") && !nomEntry.substring(packagePath.length()).contains("/")) {
                    String nomClasseSeul = nomEntry.substring(packagePath.length(), nomEntry.length() - 6);
                    ajouterControllerSiAnnote(packageClasse + "." + nomClasseSeul, liste);
                }
            }
        }
    }

    private void ajouterControllerSiAnnote(String nomComplet, Set<String> liste) throws Exception {
        Class<?> clazz = Class.forName(nomComplet);
        if (clazz.isAnnotationPresent(Controller.class)) {
            liste.add(nomComplet);
        }
    }

    public Map<UrlMethode, Mapping> findUrlMappings(String packageClasse) throws Exception {
        Map<UrlMethode, Mapping> mappings = new HashMap<>();
        List<String> controllers = findController(packageClasse);

        for (String nomController : controllers) {
            Class<?> clazz = Class.forName(nomController);

            for (Method methode : clazz.getDeclaredMethods()) {
                if (methode.isAnnotationPresent(UrlMapping.class)) {
                    UrlMapping annotation = methode.getAnnotation(UrlMapping.class);
                    UrlMethode urlMethode = new UrlMethode(annotation.value(), annotation.method());

                    mappings.put(urlMethode, new Mapping(nomController, methode.getName()));
                }
            }
        }

        return mappings;
    }
}
