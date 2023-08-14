package vman2;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONTokener;

import org.json.JSONArray;
import org.json.JSONObject;
/**
 * Servlet implementation class Config
 */
@WebServlet("/Config")
public class Config extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	JSONObject jobj,json,jreturn,opts;
	JSONArray jarr,columns,forms,xy;
	PrintWriter pw;
	
	private DbConnect db;
	private String query,query2,formName, dateColumn;
	private Connection cnn = null;
	private ResultSet rset = null;
	private PreparedStatement pstm = null;
	private ResultSetMetaData rcolumns;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Config() {
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
		response.setContentType("application/json");
		
		opts = new JSONObject();
		if(request.getParameterMap().containsKey("rtype"))
			opts.put("rtype", request.getParameter("rtype"));
		pw = response.getWriter();
		
		switch(opts.getString("rtype")) {
		case "savetabs":
			jobj = new JSONObject(request.getParameter("tab"));
			SaveTab(jobj);
			ReadTabs();
			break;
		case "GetODKForms":
			pw.print(getODKForms());
			break;
		case "loadtab":
			formName = request.getParameter("formName");
			dateColumn = request.getParameter("dateColumn");
			columns = new JSONArray(request.getParameter("columns"));
			pw.print(getSummary(formName,dateColumn,columns));
			break;
		case "formsummary":
			forms = new JSONArray(request.getParameter("forms"));
			pw.print( getFormSummary(forms) );
			break;
		case "formsummary2":
			forms = new JSONArray(request.getParameter("forms"));
			pw.print( getFormSummary2(forms) );
			break;
		}
		
	}
	
	
	protected JSONArray getFormSummary(JSONArray forms) {
		try {
			jarr = new JSONArray();
			db = new DbConnect();
			cnn = db.getConn();
			int formId = 0;
			for(Object form:forms) {
				formId++;
				if(form instanceof String ) {
					String formName = (String) form;
					query = "select count(*) as total,max(\"_CREATION_DATE\")::timestamp(0) as created,";
					query += "max(\"_SUBMISSION_DATE\")::timestamp(0) as submitted from "+formName+";";
					
					if(cnn.isClosed()) cnn = db.getConn();
					pstm = cnn.prepareStatement(query);
					
					rset = pstm.executeQuery();
					
					jobj = new JSONObject();
					jobj.put("name", formName);
					jobj.put("id", formId);
					if(rset.next()) {
						jobj.put("total", rset.getInt("total"));
						jobj.put("created", rset.getString("created"));
						jobj.put("submitted", rset.getString("submitted"));
					}
					
					query  = "select ";
					query += " extract(month from \"_SUBMISSION_DATE\") as submission_month,count(*) as submission_total";
					query += " from "+formName;
					query += " where extract(year from \"_SUBMISSION_DATE\") = extract(year from now())";
					query += " group by 1 order by 1;";
					
					if(cnn.isClosed()) cnn = db.getConn();
					pstm = cnn.prepareStatement(query);
					rset = pstm.executeQuery();
					
					columns = new JSONArray();
					while(rset.next()) {
						xy = new JSONArray();
						xy.put(rset.getInt("submission_month"));
						xy.put(rset.getInt("submission_total"));
						columns.put(xy);
					}
					
					jobj.put("monthly_submission", columns);
					jarr.put(jobj);
				}
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
	
	protected JSONArray getFormSummary2(JSONArray forms) {
		try {
			jarr = new JSONArray();
			db = new DbConnect();
			cnn = db.getConn();
			
			for(int i = 0; i < forms.length(); i++)
			{
			      JSONObject form = forms.getJSONObject(i);
			      
			      query = "select count(*) as total,max(\"_CREATION_DATE\")::timestamp(0) as created,";
			      query += "count("+form.getString("column_consented")+"::text = 'yes' OR NULL) as consented,";
			      query += "max(\"_SUBMISSION_DATE\")::timestamp(0) as submitted FROM "+form.getString("table_name")+";";
			      
			      if(cnn.isClosed()) cnn = db.getConn();
			      	pstm = cnn.prepareStatement(query);
			      	
			      	rset = pstm.executeQuery();
					
					jobj = new JSONObject();
					jobj.put("name", form.getString("table_name"));
					jobj.put("id", form.getInt("id"));
					if(rset.next()) {
						jobj.put("total", rset.getInt("total"));
						jobj.put("created", rset.getString("created"));
						jobj.put("consented", rset.getString("consented"));
						jobj.put("submitted", rset.getString("submitted"));
					}
					
					//monthly submission
					query  = "select ";
					query += " extract(month from \"_SUBMISSION_DATE\") as submission_month,count(*) as submission_total";
					query += " from "+form.getString("table_name");
					query += " where extract(year from \"_SUBMISSION_DATE\") = extract(year from now())";
					query += " group by 1 order by 1;";
					
					if(cnn.isClosed()) cnn = db.getConn();
					pstm = cnn.prepareStatement(query);
					rset = pstm.executeQuery();
					
					columns = new JSONArray();
					while(rset.next()) {
						xy = new JSONArray();
						xy.put(rset.getInt("submission_month"));
						xy.put(rset.getInt("submission_total"));
						columns.put(xy);
					}
					
					jobj.put("monthly_submission", columns);
					
					
					// location submission
					query  = "SELECT DISTINCT ";
					query += form.getString("intv_loc_level1")+" as location,";
					query += "COUNT(*) OVER (partition by "+form.getString("intv_loc_level1")+") as count,";
					query += "COUNT("+form.getString("column_consented")+"::text = 'yes' OR NULL) OVER (partition by "+form.getString("intv_loc_level1")+") as consented,";
					query += "CAST(MAX(\"_CREATION_DATE\") OVER (partition by "+form.getString("intv_loc_level1")+") AS TIMESTAMP(0)) AS lastsubmission,";
					query += "round(((COUNT(*) OVER (partition by "+form.getString("intv_loc_level1")+" )) * 100)::numeric / COUNT(*) OVER () , 2) as percent ";
					query += "FROM "+form.getString("table_name")+";";
					
					if(cnn.isClosed()) cnn = db.getConn();
					pstm = cnn.prepareStatement(query);
					rset = pstm.executeQuery();
					
					columns = new JSONArray();
					while(rset.next()) {
						json = new JSONObject();
						json.put("location", rset.getString("location"));
						json.put("count", rset.getString("count"));
						json.put("consented", rset.getString("consented"));
						json.put("percent", rset.getString("percent"));
						json.put("lastsubmission", rset.getString("lastsubmission"));
						columns.put(json);
					}
	
				    jobj.put("location_submission", columns);
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

	protected JSONObject getSummary(String formName, String dateColumn, JSONArray columns) {
		try{
			
			query  = "select 5 as no,'total' as period,";
			for(Object column:columns) {
				if(column instanceof JSONObject) {
					JSONObject col = (JSONObject) column;
					query += "sum("+col.getString("name")+"::int) as "+col.getString("shortname")+",";
				}
			}
			query = query.substring(0, query.length()-1) + " from "+formName;
			
			
			
			query2 = query.replace("select 5", "select 4").replace("total", "this year") + " where "+dateColumn+"::date > now() - interval '1 year'";
			
			query = query + " union " + query2;
			
			query = query + " union " + query2.replace("select 4", "select 3").replace("this year","this month").replace("1 year","1 month");
			query = query + " union " + query2.replace("select 4", "select 2").replace("this year","this week").replace("1 year","1 week");
			query = query + " union " + query2.replace("select 4", "select 1").replace("this year","today").replace("1 year","1 day");
			query += " order by 1 asc;";
			
			
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
			rcolumns = rset.getMetaData();
			jarr = new JSONArray();
			while(rset.next()) {
				jobj = new JSONObject();
				for (int i=1;i<=rcolumns.getColumnCount();i++){
					jobj.put(rcolumns.getColumnLabel(i), rset.getObject(i));
				}
				jarr.put(jobj);
				
			}
			jreturn = new JSONObject();
			jreturn.put("rows", jarr);
			return jreturn;
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
	protected void SaveTab(JSONObject tab) {
		//https://www.mkyong.com/java/json-simple-example-read-and-write-json/
		
		try {
			//final String dir = System.getProperty("user.dir");
			
			File file = new File("tabs.json");
			file.createNewFile();
			FileWriter fileWriter = new FileWriter(file);
			
			fileWriter.write(tab.toString());
			fileWriter.flush();
			fileWriter.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	protected void ReadTabs() {
		
		try {
			JSONTokener tokener = new JSONTokener(new FileReader("tabs.json"));
			jobj = new JSONObject(tokener);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	protected JSONArray getODKForms() {
		try{
			query  = "SELECT DISTINCT \"PERSIST_AS_TABLE_NAME\" FROM _form_data_model where \"PERSIST_AS_TABLE_NAME\" like '%CORE%';";
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
			jarr = new JSONArray();
			while(rset.next()){
				jobj = new JSONObject();
				jobj.put("name", rset.getString(1));
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
