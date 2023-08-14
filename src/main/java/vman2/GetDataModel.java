package vman2;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class GetDataModel
 */
@WebServlet("/GetDataModel")
public class GetDataModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	String query;
	
	DbConnect db;
	Connection cnn = null;
	PreparedStatement pstm = null;
	ResultSet rset = null;
	
	JSONArray jarr;
	JSONObject jobj;
	PrintWriter pw;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetDataModel() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		if(request.getParameterMap().containsKey("odk_form_name")){
			response.setContentType("application/json");
			pw = response.getWriter();
			pw.print( getDataModel(request.getParameter("odk_form_name")) );
		}
	}
	
	protected JSONArray getDataModel(String odkFormName) {
		try {
			query = "select \"ELEMENT_NAME\",\"PERSIST_AS_COLUMN_NAME\",\"ELEMENT_TYPE\"";
			query += " from _form_data_model";
			query += " where \"PERSIST_AS_TABLE_NAME\" = 'WHOVA151_CORE' AND \"PERSIST_AS_TABLE_NAME\" IS NOT NULL";
			
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(query);
			
			rset = pstm.executeQuery();
			jarr = new JSONArray();
			while(rset.next()) {
				jobj = new JSONObject();
				jobj.put("shortname", rset.getString(1));
				jobj.put("colname", rset.getString(2));
				jobj.put("type", rset.getString(3));
				
				jarr.put(jobj);
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
