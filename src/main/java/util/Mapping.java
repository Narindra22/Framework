package util;

public class Mapping {
    private String className;  // Contiendra par exemple : "controller.EmpController"
    private String methodName; // Contiendra par exemple : "liste"

    // Constructeur pour créer l'objet facilement
    public Mapping(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    // Getters pour permettre à la Servlet de lire les informations
    public String getClassName() { return className; }
    public String getMethodName() { return methodName; }
}