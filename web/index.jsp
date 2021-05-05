<%--
    Document   : index
    Created on : 16 sept. 2009, 16:54:32
    Author     : michel buffa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<!-- Ne pas oublier cette ligne sinon tous les tags de la JSTL seront ignorés ! -->
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Gestionnaire d'utilisateurs</title>
        <link href="css/bootstrap.min.css" rel='stylesheet' type='text/css' />
        <script src="js/jquery.min.js"></script>
        <script src="js/popper.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container">
            <h3>Gestionnaire d'utilisateurs</h3>


            <!-- Message qui s'affiche lorsque la page est appelé avec un paramètre http message -->
            <c:if test="${!empty param['message']}">
                <h4>Reçu message : ${param.message}</h4>
            </c:if>


            <h5>Menu de gestion des utilisateurs</h5>
            <ul>
                <li ><a href="ServletUsers?action=creerUtilisateursDeTest" >Créer 4 utilisateurs de test</a></li>
                <li><a href="ServletUsers?action=listerLesUtilisateurs" >Afficher/raffraichir la liste de tous les utilisateurs</a></li>
                <p>
            </ul>
            <h6>Liste des fonctionnalités à implémenter dans la Servlet (note : après chaque action cette page sera
                rappelée par la servlet avec la liste des utilisateurs raffraichie et un message de confirmation</h6>
            <ol class="list-group list-group-numbered">
                <li><strong>Créer un utilisateur</strong></a</li>

                
                <form action="ServletUsers" method="post">

                    <div class="form-group">
                        <label for="nom">Nom</label>
                        <input type="text"  class="form-control" name="nom" required/>

                    </div>
                    <div class="form-group">
                        <label for="prenom">Prenom</label>
                        <input type="text" class="form-control" name="prenom" required/>

                    </div>
                    <div class="form-group">
                        <label for="login">Login</label>
                        <input type="text" class="form-control" name="login" required/>

                    </div>
                    <!-- Astuce pour passer des paramètres à une servlet depuis un formulaire JSP !-->
                    <input type="hidden" name="action" value="creerUnUtilisateur"/>
                    <button type="submit" class="btn btn-success">Créer l'utilisateur</button><br><br>

                </form>

                <li><strong>Afficher les détails d'un utilisateur</strong></li>
                <form action="ServletUsers" method="get">
                    <div class="form-group">
                        <label for="login">Login</label>
                        <input type="text" class="form-control" name="login" required/>


                    </div>
                    <input type="hidden" name="action" value="chercherParLogin"/>
                    <button type="submit" class="btn btn-success">Chercher</button><br><br>

                </form>


                <li><strong>Modifier les détails d'un utilisateur :</strong></li>
                <form action="ServletUsers" method="get">


                    <div class="form-group">
                        <label for="login">Login</label>
                        <input type="text" class="form-control" name="login" value="${requestScope.log}"/>

                    </div>
                    <div class="form-group">
                        <label for="nom">Nom</label>
                        <input type="text"  class="form-control" name="nom" value="${requestScope.lastName}"/>
                    </div>

                    <div class="form-group">
                        <label for="prenom">Prenom</label>
                        <input type="text" class="form-control" name="prenom" value="${requestScope.firstName}"/>
                        <input type="hidden" name="userId" value="${requestScope.userId}"/>
                        <input type="hidden" name="action" value="updateUtilisateur"/>
                    </div>
                        <button type="submit" class="btn btn-success">Mettre à jour</button><br><br>
                </form>
            </ol>

            <!-- Fin du menu -->

            <!-- Zone qui affiche les utilisateurs si le paramètre action vaut listerComptes -->
            <c:if test="${param['action'] == 'listerLesUtilisateurs'}" >
                <h2>Liste des utilisateurs</h2>

                <table class="table">
                    <!-- La ligne de titre du tableau des comptes -->


                    <thead>
                        <tr>
                            <th>Login</th>
                            <th>Nom</th>
                            <th>Prénom</th>
                        </tr>
                    </thead>

                    <!-- Ici on affiche les lignes, une par utilisateur -->
                    <!-- cette variable montre comment on peut utiliser JSTL et EL pour calculer -->
                    <c:set var="total" value="0"/>

                    <c:forEach var="u" items="${requestScope['listeDesUsers']}">
                        <tbody>
                        <tr>
                            <td>${u.login}</td>
                            <td>${u.lastName}</td>
                            <td>${u.firstName}</td>
                            <!-- On compte le nombre de users -->
                            <c:set var="total" value="${total+1}"/>
                        </tr>
                        </tbody>
                    </c:forEach>

                    <!-- Affichage du solde total dans la dernière ligne du tableau -->
                    <tr><td><b>TOTAL</b></td><td></td><td><b>${total}</b></td><td></td></tr>
                </table>

            </c:if>


        </div>      
    </body>
</html>
