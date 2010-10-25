import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class RentrerMatch extends HttpServlet {
	
	private Connection con;

	public void ConnexionBDD()throws Exception{
		/*enregistrement du driver*/
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		/*Connexion à la base : source de données ODBC a le nom foot*/
		String url = "jdbc:odbc:foot";
		String nom = "admin";
		String mdp = "xxx";
		this.con = DriverManager.getConnection(url,nom,mdp);
		
		
	}

	public void DeconnexionBDD()throws Exception{
		/*fermeture des connexions à la BDD*/
		this.con.close();
	}
	public void SelectionEquipe(PrintWriter out, Statement stmt, String eq) throws SQLException {
		/*S?ction de l'equipe ?omicile*/		
		out.println("<select name="+eq+" id="+eq+">");
		out.println("equipe : "+eq);

		String query = "select nom from equipe";
		ResultSet rs =stmt.executeQuery(query);
		
		//variable pour stocker le nom de chaque equipe
		String nom;
		while(rs.next()){
			//On change le nom de l'?ipe ?haque tour de boucle
			nom = rs.getString(1);
			out.println("<option value="+nom+">"+nom+"</option>");
			
		}
		out.println("</select>");
	
	}
	
	public void Affichage(PrintWriter out, int err,Statement stmt) throws SQLException {
		
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
	out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
	out.println("<head> <link rel=\"stylesheet\" type=\"text/css\" href=\"../style.css\">");

	out.println("<title>Pronostic d'un match</title>");
	out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />");
	out.println("</head>");
	
	out.println("<body>");
	out.println("<div id=\"container\">");
	out.println("<div id=\"en_tete\">");
   
	out.println("</div>");
	

	
	out.print("<div id=\"navbar2\">");
	out.print("<ul>");
	out.print("<li><a href=\"../Menu.html\">Menu</a></li>");
	out.print("<li><a href=\"../servlet/RentrerMatch\">Ajouter un match</a></li>");
	out.print("<li><a href=\"../servlet/ServletProno\">Pronostics</a></li>");
	out.print("<li><a href=\"../servlet/ServletStat\">Statistiques</a></li>");
	out.print("</ul>");
	out.print("</div>");
	
	out.println("<center>");
	out.println("<br></br>");
		
		
		out.println("<FORM Method=\"POST\" Action=\"../servlet/RentrerResultatMatch\">");
		
		out.println("<h1> Rentrer un match </h1><br></br>");
		
		out.println("<p><label>Date (jj/mm/aaaa) </label></p>");
		out.println("<p><input type=text name=jour size=2>/<input type=text name=mois size=2>/<input type=text name=annee size=4>");
		out.println("<p><label>Score</label></p>");
		
		
		//Selection Equipe Domicile
		this.SelectionEquipe(out,stmt,"Domicile");
		
		/*Entr?des scores des ?ipes*/
		out.println("<input type=text name=ScoreD size=2> <input type=text name=ScoreE size=2>");

		//S?ction Equipe Exterieur
		this.SelectionEquipe(out,stmt,"Exterieur");

		out.println("</p><br><p align=center><INPUT type=\"submit\" value=\"Valider\"><INPUT type=\"reset\" value=\"Effacer\"></p>");
		
		
		/*Tests d'erreurs*/
		if(err == 1){		/*Erreur sur les Equipes*/
			out.println("<h3 align=center><font color=red>Equipes identiques.</font></h3>");
		}else if(err == 2){	/*Erreur sur l'année/
			out.println("<h3 align=center><font color=red>Annee incorrecte, veuillez rectifier</font></h3>");
		}else if(err == 3){	/*Erreur sur le mois*/
			out.println("<h3 align=center><font color=red>Mois incorrect.</font></h3>");
		}else if(err == 4){	/*Erreur sur le jour*/
			out.println("<h3 align=center><font color=red>Jour incorrect.</font></h3>");
		}else if(err == 5){	/*Erreur sur le score*/
			out.println("<h3 align=center><font color=red>Score incorrect.</font></h3>");
		}else if(err == 6){ /*Erreur sur les champs(vides)*/
			out.println("<h3 align=center><font color=red>Champ(s) vide(s).</font></h3>");
		}else if(err == 7){ /*Aucun erreur, tout se passe bien*/
			out.println("<h3 align=center><font color=green>Merci, le match a bien été inséré.</font></h3>");
		}
		
		out.println("<br></br><br></br><br></br><div id=\"pied_de_page\">");
		out.println("©Copyright TpHauspie 2010, tous droits réservés");
		out.println("</div>");
		out.println("</center></div></body></html>");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
		PrintWriter out = res.getWriter();		
		try{
			doPost(req,res);
		}catch(ServletException e){
			out.println(e.getMessage());
		}			
  	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);		
		Integer erreur = (Integer)session.getAttribute("erreur");
		erreur = new Integer(erreur == null ? 0 : erreur.intValue());
		session.setAttribute("erreur",erreur);
		
		Statement stmt = null;
		PrintWriter out = res.getWriter();

		/*Connexion ?a base de donn?*/		
		try{
			this.ConnexionBDD();
		}catch(Exception e){
			out.println("Echec de connexion ?a BDD");
		}

		try{
			/*execution d'une requete*/		
			stmt = this.con.createStatement();
		}catch(SQLException e){
			e.getMessage();
		}

		
		res.setContentType("text/html");
		
		
		/*Test si il y a un cas d'erreur*/
		try{			
			// traitements
			if(erreur != 0){
				this.Affichage(out,erreur,stmt);
				session.setAttribute("erreur",0);
			}else{
				this.Affichage(out,0,stmt);
			}
		}catch(SQLException e){
			out.println(e.getMessage());
			//this.Affichage(out,0,stmt);
			
		}		
		
		try{
			this.DeconnexionBDD();
		}catch(Exception e){
			out.println("Echec de déconnexion à BDD");
		}
	}

}
