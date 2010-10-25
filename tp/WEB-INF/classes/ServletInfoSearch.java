import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import java.io.FileInputStream;


public class ServletInfoSearch extends HttpServlet

{

public void doPost(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException{

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	
	String chmp = req.getParameter("Championnat");
	String eqp1 = req.getParameter("Equipe1");
	String eqp2 = req.getParameter("Equipe2");
	
	try{
	
	Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

    String url ="jdbc:odbc:foot";
    String id ="";
    String mdp ="";

    Connection con = DriverManager.getConnection(url,id,mdp);
  
    Statement stmt = con.createStatement();
    String query = ("SELECT points FROM Equipe WHERE nom = '"+eqp1+"'");
	out.println("yop");
    ResultSet rs = stmt.executeQuery(query);
	
	rs.next();
	int  pnt1 = rs.getInt(1);
	
	query = ("SELECT points FROM Equipe WHERE nom = '"+eqp2+"'");
    rs = stmt.executeQuery(query);
	
	rs.next();
	int pnt2 = rs.getInt(1);
	
	double resultat = (double)(pnt1+100)/(double)(pnt2+pnt1);
	out.println(pnt1+" "+pnt2+" "+resultat);
	
	
	
	res.sendRedirect("../servlet/ServletProno?rez="+resultat+"&aff=1"+"&ep1="+eqp1+"&ep2="+eqp2);
	
	}
	
	catch(Exception e){
		
		out.println(e.getMessage());
	
	}
	
}
}