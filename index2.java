import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class index2 extends HttpServlet
{

public void doPost( HttpServletRequest req, HttpServletResponse res ) 
       throws ServletException, IOException
  {

    res.setContentType( "text/html" );
     
    //Récupération du choix
    String choix = req.getParameter("choix");
	
	if(choix.equals("resultat")){
		res.sendRedirect("../ChoixInsertion.html");
	}else if(choix.equals("probabilite")){
		res.sendRedirect("./ServletProno");
	}else if(choix.equals("simulation")){
		res.sendRedirect("../index.html");
	}else{
		res.sendRedirect("./ServletSimu");
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