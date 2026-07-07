package controller;

import annotation.Controller;
import annotation.UrlMapping;

@Controller
public class EmpController {

    @UrlMapping("/emp/list")
    public String list() {
        return "Liste des employés";
    }

    @UrlMapping("/emp/create")
    public String create() {
        return "Création d'un employé";
    }

    @UrlMapping("/emp/andrana")
    public String andrana() {
        return "Andrana";
    }
}
