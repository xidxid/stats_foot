import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class RentrerMatch extends HttpServlet {
	
	private Connection con;

	public void ConnexionBDD()throws Exception{
		/*enregistrement du driver*/
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		/*Connexion Ã  la base : source de donnÃ©es ODBC a le nom Access*/
		String url = "jdbc:odbc:Access";
		String nom = "admin";
		String mdp = "xxx";
		this.con = DriverManager.getConnection(url,nom,mdp);
		
		
	}

	public void DeconnexionBDD()throws Exception{
		/*fermeture des connexions Ã  la BDD*/
		this.con.close();
	}
	
	public void Affichage(PrintWriter out, int err,Statement stmt) throws SQLException {
		
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//FR\"\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"fr\">");
		out.println("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\" /><title> Pronostiques Football </title></head>");
		out.println("<body><h1 align=\"center\"><font color=\"blue\">Rentrer un match</font></h1><br><br>");
		if(err == 1){
			out.println("<h3 align=center><font color=red>Vous ne pouvez selectionner qu'une seule fois une equipe</font></h3>");
		}
		
		out.println("<br><form method=post action=RentrerResultatMatch> <p>Date</p><p class=EquipeDom>Equipe Domicile</p><p class=ScoreDom>Score Domicile></p><p class=ScoreExt>Score Exterieur</p><p class=EquipeExt>Equipe exterieur></p>");
		out.println("<p><input type=text name=jour>/<input type=text name=mois>/<input type=text name=annee></p>");
		
		/*Sélection de l'equipe à domicile*/		
		out.println("<p><select name=Domicile>");

		String query = "select nom from equipe";
		ResultSet rs =stmt.executeQuery(query);
		
		while(rs.next()){
			out.println("<option value="+rs.getString(1)+">"+rs.getString(1)+"</option>");
		}
		out.println("</select>");

		/*Entrée des scores des équipes*/
		out.println("<input type=text name=ScoreD><input type=text name=ScoreE>");

		/*Sélection de l'équipe à l'extèrieur*/
		out.println("<p><select name=Exterieur>");
		rs =stmt.executeQuery(query);
		
		while(rs.next()){
			out.println("<option value="+rs.getString(1)+">"+rs.getString(1)+"</option>");
		}
		out.println("</select>");

		out.println("<br><p align=center><input type=submit name=envoyer><input type=reset name=effacer></p>");
		out.println("</body></html>");
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
		String erreur = "0";
		Statement stmt = null;
		PrintWriter out = res.getWriter();

		/*Connexion à la base de données*/		
		try{
			this.ConnexionBDD();
		}catch(Exception e){
			out.println("Echec de connexion à la BDD");
		}

		try{
			/*execution d'une requete*/		
			stmt = this.con.createStatement();
		}catch(SQLException e){
			e.getMessage();
		}

		
		res.setContentType("text/html");

		/*Test si l'utilisateur a sélectionné 2 fois la même équipe*/
		try{
			// lecture de la requete
			erreur = req.getParameter("Err");
			// traitements
			if(erreur.equals("1")){
				this.Affichage(out,1,stmt);
			}else{
				this.Affichage(out,0,stmt);
			}
		}catch(SQLException e){
			try{
				this.Affichage(out,0,stmt);
			}catch(Exception er){
				er.getMessage();
			}
		}		
		
		try{
			this.DeconnexionBDD();
		}catch(Exception e){
			out.println("Echec de déconnexion à la BDD");
		}
	}

}
