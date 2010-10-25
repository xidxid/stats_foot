import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Maj extends HttpServlet {
PrintWriter out;
	public void majPts(Connection connec) throws SQLException{

		String dom, ext;
		int scd, sce;
		double ptd=0.0, pte=0.0, tmpd=0.0, tmpe=0.0;
		
		//Si on decide d'utiliser des dates plus tard...
		/*long year = 365;
		year *= 24;
		year *= 3600;
		year *= 1000;
		java.util.Date cur_date_tmp = new java.util.Date();
		java.sql.Date tmpd = new java.sql.Date(cur_date_tmp.getTime());
		//Timestamp cur_date = new Timestamp((cur_date_tmp.getTime()-year));
		//out.println(cur_date.toString());*/
		
		Statement instr = connec.createStatement();
			
		//On met tout a zero puisque on va realiser une maj complete
		instr.executeUpdate("UPDATE equipe SET points = 10");
		//String req = "SELECT * FROM rencontre WHERE date_renc >= "+tmpd+" ORDER BY date_renc";
		String req = "SELECT * FROM rencontre ORDER BY date_renc";
		//out.println(tmpd);
		ResultSet req_matchs = instr.executeQuery(req);
		ResultSet req_pts;
		
		//Pour tous les matchs...
		while(req_matchs.next()){
			dom = req_matchs.getString("domicile");
			ext = req_matchs.getString("exterieur");
			scd = req_matchs.getInt("score_dom");
			sce = req_matchs.getInt("score_ext");
			
			Statement stm = connec.createStatement();
			
			req_pts = stm.executeQuery("SELECT points FROM equipe WHERE nom = '"+dom+"'");
			req_pts.next();
			ptd = (double) req_pts.getInt(1);
			req_pts = stm.executeQuery("SELECT points FROM equipe WHERE nom = '"+ext+"'");
			req_pts.next();
			pte = (double) req_pts.getInt(1);
			
			//Victoire a domicile
			if(scd>sce){
				tmpd = 10*getDiff(ptd, pte);
				tmpe = 10*getDiff(pte, ptd);
				ptd = ptd + tmpd*(1-getTh(ptd, pte));
				pte = pte + tmpe*(0-getTh(pte, ptd));
				
			//Defaite a domicile
			}else if(scd<sce){
				
				tmpd = 10*getDiff(ptd, pte);
				tmpe = 10*getDiff(pte, ptd);
				ptd = ptd + tmpd*(0-getTh(ptd, pte));
				pte = pte + tmpe*(1-getTh(pte, ptd));
			//Match nul
			}else{
				
				ptd = ptd + 10*(0.5-getTh(ptd, pte));
				pte = pte + 10*(0.5-getTh(pte, ptd));
			}
		
			stm.executeUpdate("UPDATE equipe SET points = "+ptd+" WHERE nom = '"+dom+"'");
			stm.executeUpdate("UPDATE equipe SET points = "+pte+" WHERE nom = '"+ext+"'");
		}
	
	}

	public double getTh(double me, double adv){
		return (me/(me+adv));
	}
	
	public double getDiff(double me, double adv){
		double diff=0;
		if(me>adv){
			diff = me-adv;
		}else{
			diff = adv-me;
		}
		switch((int)diff){
			case 1 : return 1.0;
			case 2 : return (3.0/2.0);
			default : return ((11.0+diff)/8.0);
		}
	}

	public void majStats(Connection connec, PrintWriter out) throws SQLException{
	
		String etmp;
		int score_ext, score_dom, nbm, mg, mp, mn, nbbp, nbbc;
		
		Statement instr = connec.createStatement();
		
		//On met tout a zero puisque on va realiser une maj complete
		instr.executeUpdate("UPDATE ext SET nb_match = 0, nb_vict = 0, nb_nul = 0, nb_def = 0, buts_pour = 0, buts_contre = 0");
		instr.executeUpdate("UPDATE home SET nb_match = 0, nb_vict = 0, nb_nul = 0, nb_def = 0, buts_pour = 0, buts_contre = 0");
		
		//On recupere le nom du club
		ResultSet req_club = instr.executeQuery("SELECT nom FROM equipe");
		
		//Pour chaque club...
		while(req_club.next()){
			
			//On stocke son nom dans une variable String
			etmp = req_club.getString(1);
			
			Statement instr3 = connec.createStatement();
			
			//On recupere les 30 derniers matchs a domicile de l'equipe courante
			String reqtmp = "SELECT * FROM rencontre WHERE domicile='"+etmp+"' ORDER BY date_renc";
			ResultSet req_matchs = instr3.executeQuery(reqtmp);
			
			//Pour chaque match...
			while(req_matchs.next()){
				
				//On recupere le score
				score_dom = req_matchs.getInt("score_dom");
				score_ext = req_matchs.getInt("score_ext");


				Statement instr2 = connec.createStatement();

				//On recupere les resultats de l'equipe a domicile avant ce match
				ResultSet req_res = instr2.executeQuery("SELECT * FROM home WHERE nom='"+etmp+"'");
				
				req_res.next();
				nbm = req_res.getInt("nb_match");
				mg = req_res.getInt("nb_vict");
				mn = req_res.getInt("nb_nul");
				mp = req_res.getInt("nb_def");
				nbbp = req_res.getInt("buts_pour");
				nbbc = req_res.getInt("buts_contre");
				
				nbm += 1;
				nbbp += score_dom;
				nbbc += score_ext;
					
				//Victoire
				if(score_dom>score_ext){
						mg += 1;
						
				//Nul
				}else if(score_dom==score_ext){
						mn += 1;
						
				//Défaite
				}else{
						mp += 1;
				}

				//On met a jour
				instr2.executeUpdate("UPDATE home SET nb_match ="+nbm+", nb_vict ="+mg+", nb_nul ="+mn+", nb_def ="+mp+", buts_pour ="+nbbp+", buts_contre ="+nbbc+" WHERE nom='"+etmp+"'");
				
				instr2.close();

			}
			
			//On recupere les 30 derniers matchs a l'exterieur de l'equipe courante
			req_matchs = instr3.executeQuery("SELECT * FROM rencontre WHERE exterieur='"+etmp+"' ORDER BY date_renc");
			
			
			while(req_matchs.next()){
				
				//On recupere le score
				score_dom = req_matchs.getInt("score_dom");
				score_ext = req_matchs.getInt("score_ext");
				
				Statement instr2 = connec.createStatement();
				
				//On recupere les resultats de l'equipe visiteuse avant ce match
				ResultSet req_res = instr2.executeQuery("SELECT * FROM ext WHERE nom='"+etmp+"'");
				
				req_res.next();
				nbm = req_res.getInt("nb_match");
				mg = req_res.getInt("nb_vict");
				mn = req_res.getInt("nb_nul");
				mp = req_res.getInt("nb_def");
				nbbp = req_res.getInt("buts_pour");
				nbbc = req_res.getInt("buts_contre");
				
				nbm += 1;
				nbbp += score_ext;
				nbbc += score_dom;
					
				//Victoire
				if(score_ext>score_dom){
						mg += 1;
						
				//Match nul	
				}else if(score_dom==score_ext){
						mn += 1;
						
				//Défaite
				}else{
						mp += 1;
				}
				
				//On met a jour
				instr2.executeUpdate("UPDATE ext SET nb_match ="+nbm+", nb_vict ="+mg+", nb_nul ="+mn+", nb_def ="+mp+", buts_pour ="+nbbp+", buts_contre ="+nbbc+" WHERE nom='"+etmp+"'");
				
				instr2.close();
			}
			
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
			out = res.getWriter();
    
			res.setContentType( "text/html" );
		
		out.println("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>Clients</title></head><body><center> ");
		out.println(" <h1>Bob</h1> ");
		
		try{
		Connection con=null;
		Statement stmt;
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); 
		String url = "jdbc:odbc:foot";
		String nom = "";
		String mdp = "";
		con = DriverManager.getConnection(url,nom,mdp);
		majPts(con);
		majStats(con, out);
		out.println("</body></html>");
		}catch(Exception e){
			out.println(e.getMessage());
		}
  }
  
  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
	doPost(req, res);
  }
  
}
