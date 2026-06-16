package servlet;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class FrontControllerServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException{
            processRequest(req, res);
        }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException{
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
                out.println("URL interceptée par le framework : " + url);
            }
        }
    }
}