package vman2;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
/**
 * Servlet implementation class GetMapData
 */
@WebServlet("/GetMapData")
public class GetMapData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Integer rtype;
	private String query;
	private PrintWriter pw;
	private DbConnect db;
	private Connection cnn = null;
	private ResultSet rset = null;
	private ResultSetMetaData columns;
	private PreparedStatement pstm = null;
	private JSONObject json,opts;
	private JSONArray jarr;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMapData() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		if(request.getParameterMap().containsKey("rtype"))
			rtype = Integer.parseInt(request.getParameter("rtype"));
		else
			return;
		
		pw = response.getWriter();
		switch(rtype) {
		case 1:
			opts = new JSONObject(request.getParameter("table"));
			pw.print( getCordinates(opts));
			break;
		}
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	protected JSONArray getCordinates(JSONObject tbl) {
		try {
			query = "SELECT ";
			
			if(tbl.has("col_intv"))
				query += tbl.getString("col_intv") + " AS intv,";
			
			query += tbl.getString("col_lat") + " AS lat,";
			query += tbl.getString("col_lon") + " AS lon,";
			query += tbl.getString("col_alt") + " AS alt,";
			query += tbl.getString("col_acc") + " AS acc,";
			query += tbl.getString("col_det") + " AS det";
			
			
			
			query += " FROM "+tbl.getString("name");
			
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(query);
			
			rset = pstm.executeQuery();
			columns = rset.getMetaData();
			jarr = new JSONArray();
			while(rset.next()) {
				json = new JSONObject();
				for (int i=1;i<=columns.getColumnCount();i++){
					json.put(columns.getColumnName(i), rset.getObject(i));
				}
				
				//json.put("lat", rset.getObject("lat"));
				//json.put("lon", rset.getObject("lon"));
				//json.put("alt", rset.getObject("alt"));
				//json.put("acc", rset.getObject("acc"));
				//json.put("det", rset.getObject("det"));
				
				jarr.put(json);
				
			}
			return jarr;
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//finally block used to close resources
		      try{
		         if(pstm!=null)
		            pstm.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(cnn!=null)
		            cnn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		}
		return null;
	}

}
