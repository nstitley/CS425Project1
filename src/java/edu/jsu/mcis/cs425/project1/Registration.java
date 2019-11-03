package edu.jsu.mcis.cs425.project1;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Registration", urlPatterns = {"/registration"})
public class Registration extends HttpServlet {

    protected void processGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter pw = response.getWriter()) {
            
            Database dB = new Database();
            String dataTb = dB.getResults(request.getParameter("code"));
            pw.println(dataTb);
            
        }
    }
    
    protected void processPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        
        String dbcode = request.getParameter("code");
        String[] list = dbcode.split(";");
        
        String first = list[0];
        String last = list[1];
        String dpname = list[2];
        String id = list[3];
        
        Database dB = new Database();
        String results = dB.addReg(first, last, dpname, id);
        
        try (PrintWriter out = response.getWriter()) {
            
            out.println(results);
            
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processGet(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processPost(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Information";
    }

}
