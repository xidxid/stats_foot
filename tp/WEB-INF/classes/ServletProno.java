import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import java.io.FileInputStream;

public class ServletProno extends HttpServlet
{
	public void doPost( HttpServletRequest req, HttpServletResponse res )
		throws ServletException, IOException
	{
		PrintWriter out = res.getWriter();
		res.setContentType("text/html");
		
	out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
	out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
	out.println("<head> <link rel=\"stylesheet\" type=\"text/css\" href=\"../style.css\">");

	out.println("<title>Pronostic d'un match</title>");
	out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />");
	out.println("</head>");
	
	out.println("<body>");
	out.println("<div id=\"container\">");
	out.println("<div>");
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
	  out.println("<div id=\"corps\">");
	  out.println("<FORM Method=\"POST\" Action=\"../servlet/ServletInfoSearch\">");
		
	  out.println("<h1> Pronostic </h1><br></br>");
      out.print("<label>Equipe 1 : </label>");
	  out.println("<SELECT name=\"Equipe1\">");
      while (rs.next())
      {
			String nom = rs.getString(1);
			out.println("<OPTION VALUE="+nom+">"+nom+"</OPTION>");
            
      }
	  out.println("</SELECT>");
	  out.print("&nbsp;&nbsp;");
	  out.println("<label>Equipe 2 : </label>");
	  out.println("<SELECT name=\"Equipe2\">");
	  
	  rs = stmt.executeQuery(query);
      while (rs.next())
      {
			String nom2 = rs.getString(1);
			out.println("<OPTION VALUE="+nom2+">"+nom2+"</OPTION>");
            
      }
	  out.println("</SELECT>");
	  
	  
	  

	  out.println("&nbsp");
	  
      con.close();

out.println("<TR><TD COLSPAN=2><INPUT type=\"submit\" value=\"Envoyer\"></TD></TR>");


}




catch (Exception e){

out.println(e.getMessage());
}




out.println("</FORM></center>");




String aff = req.getParameter("aff");

if (aff!=null){

int aff2  = Integer.parseInt(req.getParameter("aff"));

	if(aff2==1){
	
	double rez  = Double.parseDouble(req.getParameter("rez"));
	
	out.println("<center>");
	
	out.println("<br></br><br></br>");
	
	n="../images/"+req.getParameter("ep1")+".png";
	n2="../images/"+req.getParameter("ep2")+".png";
	
	n=n.toLowerCase(); 
	n2=n2.toLowerCase();
	
	
	out.print("<img src="+n+" width=\"110\" height=\"120\" alt=\"Mon Image\"> ");
	out.print("<label class=\"vs\">&nbsp;&nbsp;VS&nbsp;&nbsp;</label>");
	out.print("<img src="+n2+" width=\"110\" height=\"120\" alt=\"Mon Image\"> ");
	
	int affrez=(int)(rez*100);
	
	
	out.println("<br></br><label class=\"vs\">&nbsp;&nbsp;"+affrez+"%"+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+(100-affrez)+"%</label>");
	
	out.println("</center>");
	
	

	}
	
}
out.println("</div>");

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
