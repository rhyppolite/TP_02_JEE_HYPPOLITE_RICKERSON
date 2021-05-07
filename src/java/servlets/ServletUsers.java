/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utilisateurs.Server;
import utilisateurs.gestionnaires.UserHandler;
import utilisateurs.modeles.User;

/**
 *
 * @author michel
 */
// En Java EE 6 on peut presque se passer du fichier web.xml, les annotations de codes
// sont très pratiques !
@WebServlet(name = "ServletUsers",
        urlPatterns = {"/ServletUsers"},
        initParams = {
            @WebInitParam(name = "ressourceDir", value = "D:\\INSTALLED_SOFTWARE_HOME")
        }
)
public class ServletUsers extends HttpServlet {

    UserHandler uH = new UserHandler();
    String login = null;
    String lastName = null;
    String firstName = null;
    String userId = null;
    String forwardTo = "";
    String message = "";

    @Override
    public void init(ServletConfig config) throws ServletException {
        // appel à init obligatoire sinon cet init sera appelé à chaque fois au lieu d'une seule !
        super.init(config);
        System.out.println("Dans le init ! Appelé une seule fois lors de la première invocation");

        // On charge la base de données des utilisateurs et on initialise dans la classe Server
        // le gestionnaire d'utilisateurs'
        Server.init(config.getInitParameter("ressourceDir"));
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Pratique pour décider de l'action à faire
        String action = request.getParameter("action");

        if (action != null) {

            if (action.equals("listerLesUtilisateurs")) {
                Collection<User> liste = Server.uh.getUsers();
                request.setAttribute("listeDesUsers", liste);
                forwardTo = "index.jsp?action=listerLesUtilisateurs";
                message = "Liste des utilisateurs";

            } else if (action.equals("creerUtilisateursDeTest")) {
                Server.uh.creerUtilisateursDeTest();
                forwardTo = "index.jsp?action=todo";
                message = "Les utilisateurs Test sont créés avec succès";
            } else if (action.equals("chercherParLogin")) {
                login = request.getParameter("login");
                if (Server.uh.getUserFromId(login) == null) {
//                    uH.createUser(login, lastName, firstName);
                    forwardTo = "index.jsp?action=todo";
                    message = "Cet Utilisateur n'existe pas";
                } else {
                    User usr = Server.uh.getUserFromLogin(login);
                    request.setAttribute("usr", usr);
                    forwardTo = "index.jsp?action=chercherParLogin";
                    message = "Affichage d'un utilisateur";
                }

            } else if (action.equals("updateUtilisateur")) {
                login = request.getParameter("login");
                lastName = request.getParameter("nom");
                firstName = request.getParameter("prenom");
                userId = request.getParameter("userId");
                uH.updateUser(userId, login, lastName, firstName);
                forwardTo = "index.jsp?action=chercherParLogin";
                message = "L'utilisateur a été modifié avec succès";

            } else {
                forwardTo = "index.jsp?action=todo";
                message = "Créer un utilisateur";
            }
        }

        RequestDispatcher dp = request.getRequestDispatcher(forwardTo + "&message=" + message);
        dp.forward(request, response);
        // Après un forward, plus rien ne peut être exécuté après !

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        login = request.getParameter("login");
        lastName = request.getParameter("nom");
        firstName = request.getParameter("prenom");

        if (uH.getUserFromId(login) == null) {
            uH.createUser(login, lastName, firstName);
            forwardTo = "index.jsp?action=todo";
            message = "Utilisateur créé avec succès";
        } else {
            forwardTo = "index.jsp?action=todo";
            message = "Cet Utilisateur existe déjà";
        }

        RequestDispatcher dp = request.getRequestDispatcher(forwardTo + "&message=" + message);
        dp.forward(request, response);
        processRequest(request, response);

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
