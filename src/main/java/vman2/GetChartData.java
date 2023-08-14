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
 * Servlet implementation class GetChartData
 */
@WebServlet("/GetChartData")
public class GetChartData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	PrintWriter pw;
	DbConnect db;
	Connection cnn = null;
	PreparedStatement pstm = null;
	ResultSet rset = null;
	ResultSetMetaData columns;
	String query,tablename;
	
	int rtype;
	
	JSONObject jobj,json,jopt;
	JSONArray jarr1,jarr2,jarr3;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetChartData() {
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
		//doGet(request, response);
		jopt = new JSONObject();
		
		if(request.getParameterMap().containsKey("tablename"))
			jopt.put("tablename", request.getParameter("tablename"));
		
		if(request.getParameterMap().containsKey("qtype"))
			jopt.put("qtype", request.getParameter("qtype"));
		
		if(request.getParameterMap().containsKey("wherevalue"))
			if(!request.getParameter("wherevalue").isEmpty()){
				jopt.put("wherevalue", request.getParameter("wherevalue"));
				jopt.put("wherecolumn", request.getParameter("wherecolumn"));
			}
			
		if(request.getParameterMap().containsKey("filtervalue"))
			if(!request.getParameter("filtervalue").isEmpty()){
				jopt.put("filtervalue", request.getParameter("filtervalue"));
				jopt.put("filtercolumn", request.getParameter("filtercolumn"));
			}
				
		
		if(request.getParameterMap().containsKey("columns[]")){
			jarr1 = new JSONArray( request.getParameterValues("columns[]") );
			jopt.put("columns", jarr1);
		}

		
		//set content type
		response.setContentType("application/json");
		if(request.getParameterMap().containsKey("rtype")){
			rtype = Integer.parseInt(request.getParameter("rtype"));
			pw = response.getWriter();
			switch(rtype){
			case 1: //
				//Print Results
				pw.print( getChartMonthly(jopt).toString() );
				break;
			case 12://
				if(request.getParameterMap().containsKey("tblName"))
					jopt.put("tblName",request.getParameter("tblName"));
				
				if(request.getParameterMap().containsKey("dateCol"))
					jopt.put("dateCol",request.getParameter("dateCol"));
				
				if(request.getParameterMap().containsKey("dateDoD"))
					jopt.put("dateDoD",request.getParameter("dateDoD"));
				
				if(request.getParameterMap().containsKey("intYear"))
					jopt.put("intYear",request.getParameter("intYear"));
				
				pw.print( getChartMonthly2(jopt));
				
				break;
			default:
				break;
			}
		}else{
			pw.print("No request type set. Function GetChartData");
		}
	}

	protected JSONObject getChartMonthly2(JSONObject opts) {
		try{
			query = "select 'Total VA Submission' as title,";
			query += "sum(case when extract(month from "+opts.getString("dateCol")+") = 1 then 1 else 0 end) \"jan\",";
			query += "sum(case when extract(month from "+opts.getString("dateCol")+") = 2 then 1 else 0 end) \"feb\",";
			query += "sum(case when extract(month from "+opts.getString("dateCol")+") = 3 then 1 else 0 end) \"mar\",";
			query += "sum(case when extract(month from "+opts.getString("dateCol")+") = 4 then 1 else 0 end) \"apr\",";
			query += "sum(case when extract(month from "+opts.getString("dateCol")+") = 5 then 1 else 0 end) \"may\",";
			query += "sum(case when extract(month from "+opts.getString("dateCol")+") = 6 then 1 else 0 end) \"jun\",";
			query += "sum(case when extract(month from "+opts.getString("dateCol")+") = 7 then 1 else 0 end) \"jul\",";
			query += "sum(case when extract(month from "+opts.getString("dateCol")+") = 8 then 1 else 0 end) \"aug\",";
			query += "sum(case when extract(month from "+opts.getString("dateCol")+") = 9 then 1 else 0 end) \"sep\",";
			query += "sum(case when extract(month from "+opts.getString("dateCol")+") = 10 then 1 else 0 end) \"oct\",";
			query += "sum(case when extract(month from "+opts.getString("dateCol")+") = 11 then 1 else 0 end) \"nov\",";
			query += "sum(case when extract(month from "+opts.getString("dateCol")+") = 12 then 1 else 0 end) \"dec\" ";
			query += "from "+opts.getString("tblName");
			query += " where extract(year from "+opts.getString("dateCol")+")="+opts.getInt("intYear")+";";
			
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(query);
			
			rset = pstm.executeQuery();
			columns = rset.getMetaData();
			
			jarr2 = new JSONArray();
			for (int i=2;i<=columns.getColumnCount();i++){
				jarr2.put(columns.getColumnLabel(i));
			}
			
			jobj = new JSONObject();
			while(rset.next()){
				//Get dataset name
				
				jobj.put("title", rset.getObject(1));
				jobj.put("labels", jarr2);
				
				//Get dataset values
				jarr3 = new JSONArray();
				for (int i=2;i<=columns.getColumnCount();i++){
					jarr3.put(rset.getObject(i));
				}
				jobj.put("data", jarr3);
			}
			
			// Get summary based on DoD
			query = "select 'Total Deaths "+opts.getInt("intYear")+"' as title,";
			query += "sum(case when extract(month from "+opts.getString("dateDoD")+"::date) = 1 then 1 else 0 end) \"jan\",";
			query += "sum(case when extract(month from "+opts.getString("dateDoD")+"::date) = 2 then 1 else 0 end) \"feb\",";
			query += "sum(case when extract(month from "+opts.getString("dateDoD")+"::date) = 3 then 1 else 0 end) \"mar\",";
			query += "sum(case when extract(month from "+opts.getString("dateDoD")+"::date) = 4 then 1 else 0 end) \"apr\",";
			query += "sum(case when extract(month from "+opts.getString("dateDoD")+"::date) = 5 then 1 else 0 end) \"may\",";
			query += "sum(case when extract(month from "+opts.getString("dateDoD")+"::date) = 6 then 1 else 0 end) \"jun\",";
			query += "sum(case when extract(month from "+opts.getString("dateDoD")+"::date) = 7 then 1 else 0 end) \"jul\",";
			query += "sum(case when extract(month from "+opts.getString("dateDoD")+"::date) = 8 then 1 else 0 end) \"aug\",";
			query += "sum(case when extract(month from "+opts.getString("dateDoD")+"::date) = 9 then 1 else 0 end) \"sep\",";
			query += "sum(case when extract(month from "+opts.getString("dateDoD")+"::date) = 10 then 1 else 0 end) \"oct\",";
			query += "sum(case when extract(month from "+opts.getString("dateDoD")+"::date) = 11 then 1 else 0 end) \"nov\",";
			query += "sum(case when extract(month from "+opts.getString("dateDoD")+"::date) = 12 then 1 else 0 end) \"dec\" ";
			query += "from "+opts.getString("tblName");
			query += " where extract(year from "+opts.getString("dateDoD")+"::date)="+opts.getInt("intYear")+";";
			
			if(cnn.isClosed()) {
				db = new DbConnect();
				cnn = db.getConn();
			}
			pstm = cnn.prepareStatement(query);
			rset = pstm.executeQuery();
			columns = rset.getMetaData();
			while(rset.next()){
				jarr3 = new JSONArray();
				for (int i=2;i<=columns.getColumnCount();i++){
					jarr3.put(rset.getObject(i));
				}
				jobj.put("data2", jarr3);
			}
			/////////
			
			json = new JSONObject();
			json.put("dataset", jobj);
			
			return json;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try{
		         if(pstm!=null)
		            cnn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(cnn!=null)
		            cnn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		}
	}
	protected JSONObject getChartMonthly( JSONObject opts){
		try{
			query = "select 'Total Submission' as dataset,";
			query += "sum(jan) as January,";
			query += "sum(feb) as February,";
			query += "sum(mar) as March,";
			query += "sum(apr) as April,";
			query += "sum(may) as May,";
			query += "sum(jun) as June,";
			query += "sum(jul) as July,";
			query += "sum(aug) as August,";
			query += "sum(sep) as September,";
			query += "sum(oct) as October,";
			query += "sum(nov) as November,";
			query += "sum(dece) as December";
			query +=" FROM "+opts.getString("tablename")+";";
				
			if(opts.has("wherevalue")){
				query = query.replace(";", " ")+"WHERE "+opts.getString("wherecolumn")+" = '"+opts.getString("wherevalue")+"';";
				
				if(opts.has("filtervalue"))
					query = query.replace(";", " ")+"AND "+opts.getString("filtercolumn")+" = '"+opts.getString("filtervalue")+"';";
				
			}else if(opts.has("filtervalue"))
					query = query.replace(";", " ")+"WHERE "+opts.getString("filtercolumn")+" = '"+opts.getString("filtervalue")+"';";
					
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(query);
			
			rset = pstm.executeQuery();
			columns = rset.getMetaData();
			
			jarr2 = new JSONArray();
			for (int i=2;i<=columns.getColumnCount();i++){
				jarr2.put(columns.getColumnName(i));
			}
			json = new JSONObject();
			json.put("labels", jarr2);
			
			while(rset.next()){
				//Get dataset name
				jobj = new JSONObject();
				jobj.put("name", rset.getObject(1));
				
				//Get dataset values
				jarr3 = new JSONArray();
				for (int i=2;i<=columns.getColumnCount();i++){
					jarr3.put(rset.getObject(i));
				}
				jobj.put("data", jarr3);
			}
			json.put("dataset", jobj);
			return json;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try{
		         if(pstm!=null)
		            cnn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(cnn!=null)
		            cnn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		}
	}
	
	/*
	 * Get chat data
	 * Test button is btn-create-content on the page index.jsp
	 * */
	protected JSONObject getChartData(String squery){
		try{
			
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(squery);
			rset = pstm.executeQuery();
			columns = rset.getMetaData();
			
			jarr2 = new JSONArray();
			for (int i=1;i<=columns.getColumnCount();i++){
				jarr2.put(columns.getColumnName(i));
			}
			json = new JSONObject();
			json.put("labels", jarr2);
			
			while(rset.next()){
				
				//Get dataset name
				jobj = new JSONObject();
				jobj.put("name", rset.getObject(1));
				
				//Get dataset values
				jarr3 = new JSONArray();
				for (int i=2;i<=columns.getColumnCount();i++){
					jarr3.put(rset.getObject(i));
				}
				jobj.put("data", jarr3);
			}
			json.put("dataset", jobj);
			return json;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try{
		         if(pstm!=null)
		            cnn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(cnn!=null)
		            cnn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		}
	}
}
