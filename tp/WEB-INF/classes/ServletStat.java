import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import java.io.FileInputStream;

public class ServletStat extends HttpServlet
{
	public void doPost( HttpServletRequest req, HttpServletResponse res )
		throws ServletException, IOException
	{
		PrintWriter out = res.getWriter();
		res.setContentType("text/html");
		
	out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
	out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
	out.println("<head> <link rel=\"stylesheet\" type=\"text/css\" href=\"../style.css\">");
	
	out.println("<title>Statistiques</title>");
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
	out.println("<div id=\"tout\">");
	String n="";
	String n2="";
		
		try{


	  Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

      String url ="jdbc:odbc:foot";
      String id ="";
      String mdp ="";

      Connection con = DriverManager.getConnection(url,id,mdp);
  
      Statement stmt = con.createStatement();
      String query = "select Nom from Equipe";
      ResultSet rs = stmt.executeQuery(query);

	  out.println("<FORM Method=\"POST\" Action=\"../servlet/ServletDomExt\">");
		
	  out.println("<h1> Statistiques </h1><br></br>");
      out.print("<label>Equipe : </label>");
	  out.println("<SELECT name=\"Equipe\">");
      while (rs.next())
      {
			String nom = rs.getString(1);
			out.println("<OPTION VALUE="+nom+">"+nom+"</OPTION>");
            
      }
	  out.println("</SELECT>");
	  

	  out.println("&nbsp;");
	  
      con.close();

out.println("<TR><TD COLSPAN=2><INPUT type=\"submit\" value=\"Envoyer\"></TD></TR>");

}




catch (Exception e){

out.println(e.getMessage());
}




out.println("</FORM></center>");




String aff = req.getParameter("aff");
String eqp = req.getParameter("equipe");



if (aff!=null){

int aff2  = Integer.parseInt(req.getParameter("aff"));

	if(aff2==1){
	
	try{
	

		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

		String url ="jdbc:odbc:foot";
		String id ="";
		String mdp ="";
		Connection con = DriverManager.getConnection(url,id,mdp);
		
		Statement stmt = con.createStatement();
		
		
		String query = ("SELECT * FROM home WHERE nom='"+eqp+"'");
		ResultSet rs = stmt.executeQuery(query);
	
		ResultSetMetaData rsmd = rs.getMetaData();
	
		int nbCol = rsmd.getColumnCount();
	
		String tabNom[]  = new String[nbCol];
		String tabVal[]  = new String[nbCol];
		
		rs.next();
		out.println("<center>");
		out.println("<br></br>");
		
		out.println("<div id=\"gauche\">");
			
			out.println("<h1> A domicile </h1>");
			
			int nbmatch=Integer.parseInt(rs.getString("nb_match"));
			int nbvict=Integer.parseInt(rs.getString("nb_vict"));
			int nbnul=Integer.parseInt(rs.getString("nb_nul"));
			int nbdef=Integer.parseInt(rs.getString("nb_def"));
			
			int prvict=(nbvict*100)/nbmatch;
			int prnul=(nbnul*100)/nbmatch;
			int prdef=(nbdef*100)/nbmatch;
			
			String sprvict=" ("+prvict+"%)";
			String sprnul=" ("+prnul+"%)";
			String sprdef=" ("+prdef+"%)";
			
			
			out.print("<br><label>Buts pour : "+rs.getString("buts_pour")+"</label></br>");
			out.print("<br><label>Buts contre : "+rs.getString("buts_contre")+" </label></br>");
			out.print("<br><label>Matchs : "+nbmatch+"</label></br>");
			out.print("<br><label>Victoires : "+nbvict+sprvict+"</label></br>");
			out.print("<br><label>Nuls : "+nbnul+sprnul+"</label></br>");
			out.print("<br><label>Défaites : "+nbdef+sprdef+" </label></br>");

		
		out.println("</div>");
	
	
		out.print("<div id=\"milieu\">");
			n="../images/"+eqp+".png";
			n=n.toLowerCase();
			out.print("<img src="+n+" width=\"100\" height=\"110\" alt=\"Mon Image\"> ");
		out.print("</div>");
		
		
	
		query = ("SELECT * FROM ext WHERE nom='"+eqp+"'");
		rs = stmt.executeQuery(query);
	
		rsmd = rs.getMetaData();
		nbCol = rsmd.getColumnCount();
	
	
		String tabNom2[]  = new String[nbCol];
		String tabVal2[]  = new String[nbCol];
		
		rs.next();
		
		out.println("<div id=\"droite\">");
			
			out.println("<h1> A l'extérieur </h1>");
			
			nbmatch=Integer.parseInt(rs.getString("nb_match"));
			nbvict=Integer.parseInt(rs.getString("nb_vict"));
			nbnul=Integer.parseInt(rs.getString("nb_nul"));
			nbdef=Integer.parseInt(rs.getString("nb_def"));
			
			prvict=(nbvict*100)/nbmatch;
			prnul=(nbnul*100)/nbmatch;
			prdef=(nbdef*100)/nbmatch;
			
			sprvict=" ("+prvict+"%)";
			sprnul=" ("+prnul+"%)";
			sprdef=" ("+prdef+"%)";
			
			
			out.print("<br><label>Buts pour : "+rs.getString("buts_pour")+"</label></br>");
			out.print("<br><label>Buts contre : "+rs.getString("buts_contre")+" </label></br>");
			out.print("<br><label>Matchs : "+nbmatch+"</label></br>");
			out.print("<br><label>Victoires : "+nbvict+sprvict+"</label></br>");
			out.print("<br><label>Nuls : "+nbnul+sprnul+"</label></br>");
			out.print("<br><label>Défaites : "+nbdef+sprdef+" </label></br>");
			
		out.println("</div>");
		out.println("</center>");
		
		

		
	
	}catch(Exception e){
		out.println(e.getMessage());

	}
	}
	
}


out.println("<br></br><br></br><br></br><div id=\"pied_de_page\">");
	out.println("©Copyright TpHauspie 2010, tous droits réservés");
out.println("</div>");

out.println("</div>");
out.println("</div>");
out.println("</body>");


}
	public void doGet( HttpServletRequest req, HttpServletResponse res )throws ServletException, IOException{
	
		doPost(req,res);
	}
}
