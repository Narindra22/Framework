package controller;

import annotation.Controller;
import annotation.UrlMapping;
import util.ModelAndView;

@Controller
public class EmpController {

    @UrlMapping("/emp/list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("/emp/list.jsp");
        modelAndView.addAttribut("message", "Liste des employés");
        return modelAndView;
    }

    @UrlMapping("/emp/create")
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("/emp/create.jsp");
        modelAndView.addAttribut("message", "Création d'un employé");
        return modelAndView;
    }

    @UrlMapping("/emp/andrana")
    public ModelAndView andrana() {
        ModelAndView modelAndView = new ModelAndView("/emp/andrana.jsp");
        modelAndView.addAttribut("message", "Andrana");
        return modelAndView;
    }
}
