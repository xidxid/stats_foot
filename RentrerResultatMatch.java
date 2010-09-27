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
    String url = "jdbc:odbc:Cpp";
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

    //Verification
    if(domicile.equals(exterieur)){
	String url3 = "./RentrerMatch?Err=1";
        res.sendRedirect(url3);
    }

    //Requête
    Date d=new Date(annee, mois, jour);
    String saison = ""+d.getYear();
    String query = "insert into rencontre(date_renc, domicile, exterieur, score_dom, score_ext, saison) values( '"+d+"', '"+domicile+"', '"+exterieur+"', '"+scoreDom+"', '"+scoreExt+"', '"+saison+"')";
    int nb = stmt.executeUpdate(query);

    // fermeture des espaces
    con.close();
    }catch(Exception e){
	out.println(e.getMessage());
    }
    String url2 = "../index.html";
    res.sendRedirect(url2);
   }

    public void doGet( HttpServletRequest req, HttpServletResponse res )throws ServletException, IOException {
      PrintWriter out = res.getWriter();
      try{doPost(req, res);	
      }catch(Exception e){
		out.println(e.getMessage());
      }
    }
}
