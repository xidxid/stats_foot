// Servlet Test.java  de test de la configuration
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class RentrerResultatMatch extends HttpServlet
{

public void doPost( HttpServletRequest req, HttpServletResponse res ) 
       throws ServletException, IOException
  {

    PrintWriter out = res.getWriter();
    
    // enregistrement du driver
    try{
    Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

    // connexion à la base
    String url = "jdbc:odbc:foot";
    String nom = "quhent";
    String mdp = "xxx";
    Connection con = DriverManager.getConnection(url,nom,mdp);
	Statement stmt = con.createStatement();
    
    
    res.setContentType( "text/html" );
     
	
    //Récupération des paramètres
    String domicile = req.getParameter("Domicile");
    String exterieur = req.getParameter("Exterieur");
    int scoreDom = Integer.parseInt(req.getParameter("ScoreD"));
    int scoreExt = Integer.parseInt(req.getParameter("ScoreE"));
    int annee = Integer.parseInt(req.getParameter("annee"));
    int mois = Integer.parseInt(req.getParameter("mois"));
    int jour = Integer.parseInt(req.getParameter("jour"));

    //Verification équipes
    if(domicile.equals(exterieur)){
		HttpSession session = req.getSession(true);
		session.setAttribute("erreur", 1);/*retourne le code d'erreur 1 : équipes incorrectes*/
 		String url3 = "./RentrerMatch";
        res.sendRedirect(url3);
    }else{

		Date d=new Date(annee, mois, jour);
		int year = d.getYear()+1;
		
		//verification annee bissextile ou non
		boolean bissextile = false;
		if(annee%4 == 0){
			bissextile = true;
		}else if(annee%100 == 0){
			bissextile = false;
		}else if(annee%400 == 0){
			bissextile = true;
		}
		
		/*Vérifications sur la Date*/
		if(annee >= year || annee < 0){
			HttpSession session = req.getSession(true);
			session.setAttribute("erreur", 2);/* retourne le code d'erreur 2 : année incorrecte*/
			String url3 = "./RentrerMatch";
			res.sendRedirect(url3);
		}else if(mois < 1 || mois > 12){
			HttpSession session = req.getSession(true);
			session.setAttribute("erreur", 3);/*retourne le code d'erreur 3 : mois incorrect*/
			String url3 = "./RentrerMatch";
			res.sendRedirect(url3);
		}else if(jour < 1 || (mois%2 == 0 && jour > 30) || (mois%2 == 1 && jour > 31) || (mois == 2 && !(bissextile) && jour > 28) || (mois == 2 && bissextile && jour > 29)){
			HttpSession session = req.getSession(true);
			session.setAttribute("erreur", 4);/*retourne le code d'erreur 4 : jour incorrect*/
			String url3 = "./RentrerMatch";
			res.sendRedirect(url3);
		}
		//Verification score
		else if(scoreDom < 0 || scoreExt < 0){
			HttpSession session = req.getSession(true);
			session.setAttribute("erreur", 5);/*retourne le code d'erreur 5 : score incorrect*/
			String url3 = "./RentrerMatch";
			res.sendRedirect(url3);
		}else{
			String saison = ""+d.getYear()+"/"+year;
			//Requete d'insertion
			String query = "insert into rencontre(date_renc, domicile, exterieur, score_dom, score_ext, saison) values( '"+d+"', '"+domicile+"', '"+exterieur+"', '"+scoreDom+"', '"+scoreExt+"', '"+saison+"')";
			int nb = stmt.executeUpdate(query);

			// fermeture des espaces
			con.close();
			
			if(!(req.getParameter("Domicile").equals(req.getParameter("Exterieur")))){
				HttpSession session = req.getSession(true);
				session.setAttribute("erreur", 7);/*retourne le code d'erreur 7 : Pas d'erreur, on confirme l'insertion*/
				String url3 = "./RentrerMatch";
				res.sendRedirect(url3);
			}
		}
	}
	}catch(NumberFormatException e){
		HttpSession session = req.getSession(true);
		session.setAttribute("erreur", 6);/*retourne le code d'erreur 6 : Champ(s) vide(s)*/
		String url3 = "./RentrerMatch";
		res.sendRedirect(url3);
	}catch(Exception e){
		out.println(e.getMessage());
	}		
}

    public void doGet( HttpServletRequest req, HttpServletResponse res )throws ServletException, IOException {
      PrintWriter out = res.getWriter();
      try{doPost(req, res);	
      }catch(Exception e){
		out.println(e.getMessage());
      }
    }
}
