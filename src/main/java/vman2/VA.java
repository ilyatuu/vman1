package vman2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import org.json.JSONException;

/*
 * Author: Isaac Lyatuu
 * Email: ilyatuu@gmail.com
 * */
public class VA {
	private String sql,colname;
	private DbConnect db;
	private Connection cnn = null;
	private PreparedStatement pstm = null;
	private ResultSet rset = null;
	private ResultSetMetaData columns;
	
	private JSONObject json,jobj;
	private JSONArray jarr;
	
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Date date = new Date();
	private String sqlDate = df.format(date);
	private int intResult1,intResult2;
	
	public boolean AssignCoD(JSONObject assignObj){
		try{
			sql  = " UPDATE _web_assignment SET coder1_coda=?,coder1_codb=?,coder1_codc=?,coder1_codd=?,coder1_comments=?,";
			sql += " c1_coda_time=?,c1_codb_time=?, c1_codc_time=?,c1_codd_time=?,c1_codcont_time=?,c1_codcont=?";
			sql += " WHERE va_uri=? AND coder1_id=?;";
			
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(sql);
			
			if(assignObj.getInt("coda")!=0){
				pstm.setInt(1, assignObj.getInt("coda"));
			}else{ 
				pstm.setNull(1, java.sql.Types.INTEGER);
			}
			
			if(assignObj.getInt("codb")!=0){ 
				pstm.setInt(2, assignObj.getInt("codb"));
			}else{ 
				pstm.setNull(2, java.sql.Types.INTEGER);}
			
			if(assignObj.getInt("codc")!=0){ 
				pstm.setInt(3, assignObj.getInt("codc"));
			}else{ 
				pstm.setNull(3, java.sql.Types.INTEGER);}
			
			if(assignObj.getInt("codd") != 0){ 
				pstm.setInt(4, assignObj.getInt("codd"));
			}else{ 
				pstm.setNull(4, java.sql.Types.INTEGER);}
			
			if(assignObj.has("notes")){ 
				pstm.setString(5, assignObj.getString("notes"));
			}else{ 
				pstm.setNull(5, java.sql.Types.VARCHAR);}
			
			if(assignObj.has("coda_time")){ 
				pstm.setString(6, assignObj.getString("coda_time"));
			}else{ 
				pstm.setNull(6, java.sql.Types.VARCHAR);}
			
			if(assignObj.has("codb_time")){ 
				pstm.setString(7, assignObj.getString("codb_time"));
			}else{ 
				pstm.setNull(7, java.sql.Types.VARCHAR);}
			
			if(assignObj.has("codc_time")){ 
				pstm.setString(8, assignObj.getString("codc_time"));
			}else{ 
				pstm.setNull(8, java.sql.Types.VARCHAR);}
			
			if(assignObj.has("codd_time")){ 
				pstm.setString(9, assignObj.getString("codd_time"));
			}else{ 
				pstm.setNull(9, java.sql.Types.VARCHAR);}
			
			if(assignObj.has("codcont_time")){ 
				pstm.setString(10, assignObj.getString("codcont_time"));
			}else{ 
				pstm.setNull(10, java.sql.Types.VARCHAR);}
			
			if(assignObj.getInt("codcont")!=0){ 
				pstm.setInt(11, assignObj.getInt("codcont"));
			}else{ 
				pstm.setNull(11, java.sql.Types.INTEGER);}
			
			pstm.setString(12, assignObj.getString("vaid"));
			pstm.setInt(13, assignObj.getInt("userid"));
			
			System.out.println(pstm.toString());
			pstm.executeUpdate();
			
			//Check Coder 2
			sql  = " UPDATE _web_assignment SET coder2_coda=?,coder2_codb=?,coder2_codc=?,coder2_codd=?,coder2_comments=?,";
			sql += " c2_coda_time=?,c2_codb_time=?, c2_codc_time=?,c2_codd_time=?,c2_codcont_time=?,c2_codcont=?";
			sql += " WHERE va_uri=? AND coder2_id=?;";
			
			pstm = cnn.prepareStatement(sql);
			if(assignObj.getInt("coda")!=0){
				pstm.setInt(1, assignObj.getInt("coda"));
			}else{ 
				pstm.setNull(1, java.sql.Types.INTEGER);
			}
			
			if(assignObj.getInt("codb")!=0){ 
				pstm.setInt(2, assignObj.getInt("codb"));
			}else{ 
				pstm.setNull(2, java.sql.Types.INTEGER);}
			
			if(assignObj.getInt("codc")!=0){ 
				pstm.setInt(3, assignObj.getInt("codc"));
			}else{ 
				pstm.setNull(3, java.sql.Types.INTEGER);}
			
			if(assignObj.getInt("codd") != 0){ 
				pstm.setInt(4, assignObj.getInt("codd"));
			}else{ 
				pstm.setNull(4, java.sql.Types.INTEGER);}
			
			if(assignObj.has("notes")){ 
				pstm.setString(5, assignObj.getString("notes"));
			}else{ 
				pstm.setNull(5, java.sql.Types.VARCHAR);}
			
			if(assignObj.has("coda_time")){ 
				pstm.setString(6, assignObj.getString("coda_time"));
			}else{ 
				pstm.setNull(6, java.sql.Types.VARCHAR);}
			
			if(assignObj.has("codb_time")){ 
				pstm.setString(7, assignObj.getString("codb_time"));
			}else{ 
				pstm.setNull(7, java.sql.Types.VARCHAR);}
			
			if(assignObj.has("codc_time")){ 
				pstm.setString(8, assignObj.getString("codc_time"));
			}else{ 
				pstm.setNull(8, java.sql.Types.VARCHAR);}
			
			if(assignObj.has("codd_time")){ 
				pstm.setString(9, assignObj.getString("codd_time"));
			}else{ 
				pstm.setNull(9, java.sql.Types.VARCHAR);}
			
			if(assignObj.has("codcont_time")){ 
				pstm.setString(10, assignObj.getString("codcont_time"));
			}else{ 
				pstm.setNull(10, java.sql.Types.VARCHAR);}
			
			if(assignObj.getInt("codcont")!=0){ 
				pstm.setInt(11, assignObj.getInt("codcont"));
			}else{ 
				pstm.setNull(11, java.sql.Types.INTEGER);}
			
			pstm.setString(12, assignObj.getString("vaid"));
			pstm.setInt(13, assignObj.getInt("userid"));
			
			System.out.println(pstm.toString());
			pstm.executeUpdate();
			
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
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
	}
	/**
	 * Get Discordant VA
	 */
	public JSONObject getDiscordantVA(int coderId){
		try{
			sql="SELECT * FROM view_individual_va WHERE (c1ucd != c2ucd) and (c1id=? OR c2id=?);";
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(sql);
			pstm.setInt(1, coderId);
			pstm.setInt(2, coderId);
			rset = pstm.executeQuery();
			columns = rset.getMetaData();
			jarr = new JSONArray();
			while(rset.next()){
				json = new JSONObject();
				for(int i=1;i<=columns.getColumnCount();i++){
					json.put(columns.getColumnName(i), rset.getObject(i));
				}
				jarr.put(json);
			}
			jobj = new JSONObject();
			jobj.put("total", jarr.length());
			jobj.put("rows", jarr);
			return jobj;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			try{
		         if(pstm!=null)
		            pstm.close();
		      }catch(SQLException se){
		    	  se.printStackTrace();
		      }
		      try{
		         if(cnn!=null)
		            cnn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		}
	}
	public JSONObject getVARec(String vaid){
		try{
			
			sql="SELECT * FROM view_va WHERE _URI like ?";
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(sql);
			pstm.setString(1, vaid);
			rset = pstm.executeQuery();
			columns = rset.getMetaData();
			if(rset.next()){
				json = new JSONObject();
				//Forward way use (int i=1;i<=columns.getColumnCount();i++)
				//Backward way use (int i=columns.getColumnCount();i>0;i--)
				for(int i=1;i<=columns.getColumnCount();i++){
					colname = columns.getColumnName(i);
					if(colname.indexOf("_ID") > 0){
						colname = colname.substring(colname.indexOf("_ID")+1, colname.length());
					}
					if(colname.indexOf("_AGE") > 0){
						colname = colname.substring(colname.indexOf("_AGE")+1, colname.length());
					}
					json.put(colname, rset.getObject(i));
				}
			}
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
	}
	/**
	 * Get VA Document
	 */
	public JSONObject getVARec(String vaid, String tbl){
		try{
			sql="SELECT * FROM "+tbl+" WHERE \"_URI\" like ?";
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(sql);
			pstm.setString(1, vaid);
			//System.out.println(pstm.toString());
			rset = pstm.executeQuery();
			columns = rset.getMetaData();
			if(rset.next()){
				json = new JSONObject();
				//Forward way use (int i=1;i<=columns.getColumnCount();i++)
				//Backward way use (int i=columns.getColumnCount();i>0;i--)
				for(int i=1;i<=columns.getColumnCount();i++){
					colname = columns.getColumnName(i);
					if(colname.indexOf("_ID") > 0){
						colname = colname.substring(colname.indexOf("_ID")+1, colname.length());
					}
					if(colname.indexOf("_AGE") > 0){
						colname = colname.substring(colname.indexOf("_AGE")+1, colname.length());
					}
					if(colname.indexOf("_GPS") > 0){
						colname = colname.substring(colname.indexOf("_GPS")+1, colname.length());
					}
					if(colname.indexOf("_IS_") > 0){
						colname = colname.substring(colname.indexOf("_IS")+1, colname.length());
					}
					json.put(colname, rset.getObject(i));
				}
			}
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
		            pstm.close();
		      }catch(SQLException se){
		    	  se.printStackTrace();
		      }
		      try{
		         if(cnn!=null)
		            cnn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		}
		
	}
	/*
	 * Assign VA
	 * */
	public JSONObject getCodedVA2(JSONObject table, String filterBy) throws JSONException {
		try {
			//get the denominator
			sql  = "select count(*)";
			sql += " from _web_assignment a";
			sql += " left join "+table.getString("table_name")+" b on a.va_uri = b.\"_URI\"";
			sql += " where returnUnderline(coder1_coda,coder1_codb,coder1_codc,coder1_codd) = returnUnderline(coder2_coda,coder2_codb,coder2_codc,coder2_codd)";
			sql += " and va_table = ? ";
			
			switch(filterBy.toString().toLowerCase()) {
			case "adults":
				sql += " and "+table.getString("adult_column")+"::integer = 1";
				break;
			case "children":
				sql += " and "+table.getString("child_column")+"::integer = 1";
				break;
			case "neonates":
				sql += " and "+table.getString("neonate_column")+"::integer = 1";
				break;
			}
			
			sql += ";";
			
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(sql);
			
			pstm.setString(1, table.getString("table_name"));
			
			rset = pstm.executeQuery();
			int rows = 0;
			if(rset.next()) {
				rows = rset.getInt(1);
			}
			json = new JSONObject();
			json.put("total", rows);
			
			if(rows==0) {
				json.put("data", "");
				return json;
			}
			
			//get the data
			sql = "select ROW_NUMBER () OVER (order by count(icdname) desc) as position,";
			sql += " icdcode,icdname,count(icdname) as count, round(count(icdname)::decimal / ? * 100,2) as ratio ";
			sql += " from ( select va_uri::text, c.icdcode::text, c.icdname::text,";
			sql += " case";
			sql += " when "+table.getString("neonate_column")+"::integer = 1 then 'neonates'";
			sql += " when "+table.getString("child_column")+"::integer = 1 then 'children'";
			sql += " when "+table.getString("adult_column")+"::integer = 1 then 'adults'";
			sql += " else 'unknown'";
			sql += " end as category";
			sql += " from _web_assignment a";
			sql += " left join "+table.getString("table_name")+" b on a.va_uri = b.\"_URI\"";
			sql += " left join _web_icd10 c on returnUnderline(coder1_coda,coder1_codb,coder1_codc,coder1_codd) = c.id";
			sql += " where";
			sql += " returnUnderline(coder1_coda,coder1_codb,coder1_codc,coder1_codd) = returnUnderline(coder2_coda,coder2_codb,coder2_codc,coder2_codd)";
			sql += " and b.\"_URI\" is not null";
			sql += " ) as codedVA";
			
			if (!filterBy.equalsIgnoreCase("all")) {
				sql += " where category = '"+filterBy+"'";
			}
			
			sql += " group by icdcode,icdname limit ?";
			
			
			pstm = cnn.prepareStatement(sql);
			pstm.setInt(1, rows);
			pstm.setInt(2, table.getInt("limitTo"));
			
			rset = pstm.executeQuery();
			columns = rset.getMetaData();
			jarr = new JSONArray();
			while(rset.next()) {
				jobj = new JSONObject();
				for (int i=1;i<=columns.getColumnCount();i++){
					colname = columns.getColumnName(i);
					jobj.put( colname, rset.getObject(i));
				}
				jarr.put(jobj);
			}
			json = new JSONObject();
			json.put("total", table.getInt("limitTo"));
			json.put("rows", jarr);

			return json;
		}catch(SQLException e){
			e.printStackTrace();
		}catch(JSONException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
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
	public JSONObject getCodedVA(String tblName,String filterBy, int limitTo) {
		try {
			
			//get the denominator
			sql  = "select count(*)";
			sql += " from _web_assignment a";
			sql += " left join "+tblName+" b on a.va_uri = b.\"_URI\"";
			sql += " where returnUnderline(coder1_coda,coder1_codb,coder1_codc,coder1_codd) = returnUnderline(coder2_coda,coder2_codb,coder2_codc,coder2_codd)";
			sql += " and va_table = ? ";
			
			switch(filterBy.toString().toLowerCase()) {
			case "adults":
				sql += " and \"CONSENTED_DECEASED_CRVS_INFO_ON_DECEASED_IS_ADULT\"::integer = 1";
				break;
			case "children":
				sql += " and \"CONSENTED_DECEASED_CRVS_INFO_ON_DECEASED_IS_CHILD\"::integer = 1";
				break;
			case "neonates":
				sql += " and \"CONSENTED_DECEASED_CRVS_INFO_ON_DECEASED_IS_NEONATAL\"::integer = 1";
				break;
			}
			
			sql += ";";
			
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(sql);
			pstm.setString(1, tblName);
			
			rset = pstm.executeQuery();
			int rows = 0;
			if(rset.next()) {
				rows = rset.getInt(1);
			}
			json = new JSONObject();
			json.put("total", rows);
			
			if(rows==0) {
				json.put("data", "");
				return json;
			}
			
			//get the data
			sql = "select ROW_NUMBER () OVER (order by count(icdname) desc) as position,";
			sql += " icdcode,icdname,count(icdname) as count, round(count(icdname)::decimal / ? * 100,2) as ratio ";
			sql += " from ( select va_uri::text, c.icdcode::text, c.icdname::text,";
			sql += " case";
			sql += " when \"CONSENTED_DECEASED_CRVS_INFO_ON_DECEASED_IS_NEONATAL\"::integer = 1 then 'neonates'";
			sql += " when \"CONSENTED_DECEASED_CRVS_INFO_ON_DECEASED_IS_CHILD\"::integer = 1 then 'children'";
			sql += " when \"CONSENTED_DECEASED_CRVS_INFO_ON_DECEASED_IS_ADULT\"::integer = 1 then 'adults'";
			sql += " else 'unknown'";
			sql += " end as category";
			sql += " from _web_assignment a";
			sql += " left join "+tblName+" b on a.va_uri = b.\"_URI\"";
			sql += " left join _web_icd10 c on returnUnderline(coder1_coda,coder1_codb,coder1_codc,coder1_codd) = c.id";
			sql += " where";
			sql += " returnUnderline(coder1_coda,coder1_codb,coder1_codc,coder1_codd) = returnUnderline(coder2_coda,coder2_codb,coder2_codc,coder2_codd)";
			sql += " and b.\"_URI\" is not null";
			sql += " ) as codedVA";
			
			if (!filterBy.equalsIgnoreCase("all")) {
				sql += " where category = '"+filterBy+"'";
			}
			
			sql += " group by icdcode,icdname limit ?";
			
			
			pstm = cnn.prepareStatement(sql);
			pstm.setInt(1, rows);
			pstm.setInt(2, limitTo);
			
			rset = pstm.executeQuery();
			columns = rset.getMetaData();
			jarr = new JSONArray();
			while(rset.next()) {
				jobj = new JSONObject();
				for (int i=1;i<=columns.getColumnCount();i++){
					colname = columns.getColumnName(i);
					jobj.put( colname, rset.getObject(i));
				}
				jarr.put(jobj);
			}
			json = new JSONObject();
			json.put("total", limitTo);
			json.put("rows", jarr);

			return json;
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
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
	public JSONObject getCodedVAAll2(JSONObject vatable) {
		
		try {
			sql  = "select va_uri,b.icdname as underline,";
			sql += "c.icdname as coder1_coda,d.icdname as coder2_coda,";
			sql += "e.icdname as coder1_codb,f.icdname as coder2_codb,";
			sql += "g.icdname as coder1_codc,h.icdname as coder2_codc,";
			sql += "i.icdname as coder1_codd,j.icdname as coder2_codd,";
			sql += "k."+vatable.getString("death_loc_level1")+" as death_loc_level1,";
			sql += "k."+vatable.getString("death_loc_level2")+" as death_loc_level2";
			sql += " from _web_assignment a";
			sql += " left join _web_icd10 b on returnunderline(coder1_coda,coder1_codb,coder1_codc,coder1_codd) = b.id";
			sql += " left join _web_icd10 c on a.coder1_coda = c.id";
			sql += " left join _web_icd10 d on a.coder2_coda = d.id";
			sql += " left join _web_icd10 e on a.coder1_codb = e.id";
			sql += " left join _web_icd10 f on a.coder2_codb = f.id";
			sql += " left join _web_icd10 g on a.coder1_codc = g.id";
			sql += " left join _web_icd10 h on a.coder2_codc = h.id";
			sql += " left join _web_icd10 i on a.coder1_codd = i.id";
			sql += " left join _web_icd10 j on a.coder2_codd = j.id";
			sql += " left join "+vatable.getString("table_name")+" k on a.va_uri = k.\"_URI\"";
			sql += " where returnunderline(coder1_coda,coder1_codb,coder1_codc,coder1_codd) = returnunderline(coder2_coda,coder2_codb,coder2_codc,coder2_codd)";
			sql += " and va_table=?;";
			
			
			
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(sql);
			pstm.setString(1, vatable.getString("table_name"));			
			rset = pstm.executeQuery();
			columns = rset.getMetaData();
			jarr = new JSONArray();
			
			int rowCount=0;
			while(rset.next()) {
				rowCount++;
				jobj = new JSONObject();
				for (int i=1;i<=columns.getColumnCount();i++){
					colname = columns.getColumnName(i);
					jobj.put( colname, rset.getObject(i));
				}
				jarr.put(jobj);
			}
			json = new JSONObject();
			json.put("total", rowCount);
			json.put("rows", jarr);
			return json;
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
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
	public JSONObject getCodedVAAll(String tblName) {
		try {
			sql  = " SELECT va_uri,icdcode,icdname,COALESCE(coder1_codd,coder1_codc,coder1_codb,coder1_coda) as icdid";
			sql += " FROM _web_assignment a";
			sql += " LEFT JOIN _web_icd10 b on COALESCE(coder1_codd,coder1_codc,coder1_codb,coder1_coda) = b.id";
			sql += " WHERE COALESCE(coder1_codd,coder1_codc,coder1_codb,coder1_coda) = COALESCE(coder2_codd,coder2_codc,coder2_codb,coder2_coda)";
			sql += " AND va_table=?";
			
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(sql);
			pstm.setString(1, tblName);
			rset = pstm.executeQuery();
			columns = rset.getMetaData();
			jarr = new JSONArray();
			
			int rowCount=0;
			while(rset.next()) {
				rowCount++;
				jobj = new JSONObject();
				for (int i=1;i<=columns.getColumnCount();i++){
					colname = columns.getColumnName(i);
					jobj.put( colname, rset.getObject(i));
				}
				jarr.put(jobj);
			}
			json = new JSONObject();
			json.put("total", rowCount);
			json.put("rows", jarr);
			return json;
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
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
	public JSONObject getCodedVAAll3(JSONObject opts) {
		try {
			sql  = " SELECT va_uri, b.icdname as coder1_underline,c.icdname as coder2_underline,d.fullname as coder1,coder1_comments,e.fullname as coder2,coder2_comments,va_table,";
			sql += " f.icdname as coder1_coda,g.icdname as coder1_codb,h.icdname as coder1_codc,i.icdname as coder1_codd,j.icdname as c1_codcont,";
			sql += " k.icdname as coder2_coda,l.icdname as coder2_codb,m.icdname as coder2_codc,n.icdname as coder2_codd,o.icdname as c2_codcont,";
			sql += " CASE ";
			sql += " WHEN COALESCE(coder1_codd,coder1_codc,coder1_codb,coder1_coda) = COALESCE(coder2_codd,coder2_codc,coder2_codb,coder2_coda) THEN b.icdcode || ' | ' || b.icdname ";
			sql += " ELSE NULL ";
			sql += " END AS final_underline ";
			sql += " FROM _web_assignment a ";
			sql += " LEFT JOIN _web_icd10 f on a.coder1_coda = f.id";
			sql += " LEFT JOIN _web_icd10 g on a.coder1_codb = g.id";
			sql += " LEFT JOIN _web_icd10 h on a.coder1_codc = h.id";
			sql += " LEFT JOIN _web_icd10 i on a.coder1_codd = i.id";
			sql += " LEFT JOIN _web_icd10 j on a.c1_codcont = j.id";
			sql += " LEFT JOIN _web_icd10 k on a.coder2_coda = k.id";
			sql += " LEFT JOIN _web_icd10 l on a.coder2_codb = l.id";
			sql += " LEFT JOIN _web_icd10 m on a.coder2_codc = m.id";
			sql += " LEFT JOIN _web_icd10 n on a.coder2_codd = n.id";
			sql += " LEFT JOIN _web_icd10 o on a.c2_codcont = o.id";
			sql += " LEFT JOIN _web_icd10 b on COALESCE(coder1_codd,coder1_codc,coder1_codb,coder1_coda) = b.id ";
			sql += " LEFT JOIN _web_icd10 c on COALESCE(coder2_codd,coder2_codc,coder2_codb,coder2_coda) = c.id ";
			sql += " LEFT JOIN _web_users d on coder1_id = d.id ";
			sql += " LEFT JOIN _web_users e on coder2_id = e.id ";
			sql += " WHERE va_table=? ";
			
			if(opts.has("coding_status")) {
				if(opts.getString("coding_status").equalsIgnoreCase("concordant")) {
					sql+="AND COALESCE(coder1_codd,coder1_codc,coder1_codb,coder1_coda) = COALESCE(coder2_codd,coder2_codc,coder2_codb,coder2_coda)";
				}
				if(opts.getString("coding_status").equalsIgnoreCase("discordant")) {
					sql+="AND COALESCE(coder1_codd,coder1_codc,coder1_codb,coder1_coda) != COALESCE(coder2_codd,coder2_codc,coder2_codb,coder2_coda)";
				}
			}
			
			//terminate SQL
			sql+=";";
			
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(sql);
			pstm.setString(1, opts.getString("table_name"));
			rset = pstm.executeQuery();
			columns = rset.getMetaData();
			jarr = new JSONArray();
			
			int rowCount=0;
			while(rset.next()) {
				rowCount++;
				jobj = new JSONObject();
				for (int i=1;i<=columns.getColumnCount();i++){
					colname = columns.getColumnName(i);
					jobj.put( colname, rset.getObject(i));
				}
				jarr.put(jobj);
			}
			json = new JSONObject();
			json.put("total", rowCount);
			json.put("rows", jarr);
			return json;
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
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
	public boolean AssignVA(JSONObject vaObj){
		try{
			if(vaObj.getInt("coderType")==1){
				//Coder 1
				if(vaObj.getString("assignType").equalsIgnoreCase("assign")){
					sql  = "INSERT INTO _web_assignment(coder1_id,coder1_assigned_date,va_table,va_uri) VALUES(?,?,?,?);";
				}else{
					//Update
					sql  = "UPDATE _web_assignment SET coder1_id=?,coder1_assigned_date=?,va_table=? WHERE va_uri=?;";
				}
			}else{
				//Coder 2
				if(vaObj.getString("assignType").equalsIgnoreCase("assign")){
					sql  = "INSERT INTO _web_assignment(coder2_id,coder2_assigned_date,va_table,va_uri) VALUES(?,?,?,?);";
				}else{
					sql  = "UPDATE _web_assignment SET coder2_id=?,coder2_assigned_date=?,va_table=? WHERE va_uri=?;";
				}
				
			}
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(sql);
			cnn.setAutoCommit(false); //if you want to manually commit
			jarr = new JSONArray();
			jarr = vaObj.getJSONArray("vaIds");
			for(int i=0;i<jarr.length();i++){
				pstm.setInt(1, vaObj.getInt("coderId"));
				pstm.setTimestamp(2, Timestamp.valueOf(sqlDate));
				pstm.setString(3, vaObj.getString("va_table"));
				pstm.setString(4, jarr.getString(i));
				pstm.addBatch();
			}			
			pstm.executeBatch();
			cnn.commit();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
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
		return false;
	}
	
	/**
	 * Unassign VA from the _web_assignment
	 * **/
	public JSONObject UnAssignVA(JSONObject obj) {
		try {
			switch(obj.getInt("coderType")) {
			case 1:
				sql  = "UPDATE _web_assignment ";
				sql += "SET coder1_id = NULL ";
				sql += "WHERE va_uri = ? AND coder1_id="+obj.getInt("coderId")+" AND coder1_coda IS NULL;";
				break;
			case 2:
				sql  = "UPDATE _web_assignment ";
				sql += "SET coder2_id = NULL ";
				sql += "WHERE va_uri = ? AND coder2_id="+obj.getInt("coderId")+" AND coder2_coda IS NULL;";
				break;
			}
			
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(sql);
			cnn.setAutoCommit(false); //if you want to manually commit
			
			jarr = new JSONArray();
			jarr = obj.getJSONArray("vaIds");
			
			
			String ids = jarr.getString(0);
			ids = ids.replace("[", "(");
			ids = ids.replace("]", ")");
			System.out.println(ids);
			
			for(int i=0;i<jarr.length();i++){
				
				
				pstm.setString(1, jarr.getString(i));
				pstm.addBatch();
				
				System.out.println(jarr.getString(i));
				System.out.println(jarr.length());
			}
			
			
			int[] result = pstm.executeBatch();
			cnn.commit();
			json = new JSONObject();
			json.put("total", result.length);
			
			System.out.println(pstm.toString());
			System.out.println(result.length);
			
			return json;
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
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
	
	/*
	 * UnAssign VA version 2:
	 * For some reason, I could not iterate through the json object. 
	 * Therefore, this is option No.2
	 * I think this is because the values are coming as string array list
	 * Need to change from stringlist to array
	 * */
	public JSONObject UnAssignVA2(JSONObject obj) {
		try {
			
			jarr = new JSONArray();
			jarr = obj.getJSONArray("vaIds");
			
			
			String ids = jarr.getString(0);
			ids = ids.replace("[", "(");
			ids = ids.replace("]", ")");
			ids = ids.replace("\"", "'");
			
			switch(obj.getInt("coderType")) {
			case 1:
				sql  = "UPDATE _web_assignment ";
				sql += "SET coder1_id = NULL ";
				sql += "WHERE va_uri IN "+ids+" AND coder1_id="+obj.getInt("coderId")+" AND coder2_id IS NOT NULL;";
				break;
			case 2:
				sql  = "UPDATE _web_assignment ";
				sql += "SET coder2_id = NULL ";
				sql += "WHERE va_uri IN "+ids+" AND coder2_id="+obj.getInt("coderId")+" AND coder1_id IS NOT NULL;";
				break;
			}
			
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(sql);
			cnn.setAutoCommit(false); //if you want to manually commit
			intResult1 = pstm.executeUpdate();
			cnn.commit();
			
			
			// if only one coder exist
			switch(obj.getInt("coderType")) {
			case 1:
				sql  = "DELETE FROM _web_assignment ";
				sql += "WHERE va_uri IN "+ids+" AND coder1_id="+obj.getInt("coderId")+" AND coder2_id IS NULL;";
				break;
			case 2:
				sql  = "DELETE FROM _web_assignment ";
				sql += "WHERE va_uri IN "+ids+" AND coder2_id="+obj.getInt("coderId")+" AND coder1_id IS NULL;";
				break;
			}
			
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(sql);
			cnn.setAutoCommit(false); //if you want to manually commit
			intResult2 = pstm.executeUpdate();
			cnn.commit();
			
			json = new JSONObject();
			json.put("total", intResult1+intResult2);
			
			return json;
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
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
	/**
	 * Get ICD10 Codes
	 */
	public JSONArray getICD10List(){
		try{
			sql  = "SELECT id,icdname,icdcode,concat(icdcode,' | ',icdname) as icdlabel ";
			sql += "FROM _web_icd10;";
			db = new DbConnect();
			cnn = db.getConn();
			pstm = cnn.prepareStatement(sql);
			rset = pstm.executeQuery();
			jarr = new JSONArray();
			while(rset.next()){
				json = new JSONObject();
				json.put("id", rset.getInt(1));
				json.put("icdname",  rset.getString(2));
				json.put("icdcode",  rset.getString(3));
				json.put("icdlabel", rset.getString(4));
				
				jarr.put(json);
			}
			return jarr;
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
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
