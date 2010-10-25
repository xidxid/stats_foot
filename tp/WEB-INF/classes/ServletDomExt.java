import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import java.io.FileInputStream;


public class ServletDomExt extends HttpServlet

{

public void doPost(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException{

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	
	
	String equipe = req.getParameter("Equipe");
	
	
	try{
	
	  	
	
	res.sendRedirect("../servlet/ServletStat?aff=1"+"&equipe="+equipe);

	}
	
	catch(Exception e){
	
		out.println(e.getMessage());
	
	}
	
}
public void doGet( HttpServletRequest req, HttpServletResponse res )throws ServletException, IOException{
	
		doPost(req,res);
	}
	
}
