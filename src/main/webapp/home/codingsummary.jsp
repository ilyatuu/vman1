<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Coding Summary</title>
<%@ page import="vman2.Settings" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>
<%

	int roleid=0,userid=0,notcoded=0,discordant=0;
	if(session.getAttribute("roleid") != null){
		roleid = Integer.parseInt(session.getAttribute("roleid").toString());
	}
	if(session.getAttribute("userid") != null){
		userid = Integer.parseInt(session.getAttribute("userid").toString());
	}
	
	if(session.isNew()){
		//response.sendRedirect("../index.jsp");
		response.getWriter().write("Your session has expired. Please <a href='../index.jsp'>relogin</a>");
		return;
	}
	if(request.getParameterMap().containsKey("notcoded")){
		notcoded = Integer.parseInt(request.getParameter("notcoded"));
	}
	if(request.getParameterMap().containsKey("discordant")){
		discordant = Integer.parseInt(request.getParameter("discordant"));
	}
	if(session.isNew()){
		//response.sendRedirect("../index.jsp");
		response.getWriter().write("Your session has expired. Please <a href='../index.jsp'>relogin</a>");
		return;
	}
%>
</head>
<body>
<!-- main container -->
<div class="page-container container-fluid" style="padding:0;">
	<div class="panel panel-default">
			<div class="panel-heading">
				<h6 class="panel-title"><i class="fa fa-newspaper-o"></i>Coding Work</h6>
				<div class="pull-right"><h6 id="user_name" class="panel-title"><i class="fa fa-user"></i>Username</h6></div>
			</div>
			<div class="panel-body">
				<table id="tblCodingResults" data-striped="true"></table>
			</div>
	</div>
</div>


<!-- scripts -->
<script type="text/javascript" src="../js/plugins/interface/bootstrap-table-export.min.js"></script>
<script>
$(document).ready(function(){
	
	//Initialize
	summaryOfCoding();
	
	
	
	/*
		User defined functions
	*/
	function summaryOfCoding(){
		$.ajax({
			url:"../VAMethods",
			data:"rtype=16",
			type:"post",
			dataType:"json",
			success:function(data){
				$("#tblCodingResults").bootstrapTable({
					data:data.rows,
					search: true,
					pagination: true,
					pageSize: 10,
					columns:[{
					        	field:'fullname',
					        	title:'Coder Name',
					        	sortable:true
					         },{
					        	field:'assigned',
					        	title:'Assigned',
					        	sortable:true,
					        	align:'right'
					         },{
					        	 field:'coded',
					        	 title:'Coded',
					        	 sortable:true,
					        	 align:'right'
					         },{
					        	 field:'pending',
					        	 title:'Pending',
					        	 sortable:true,
					        	 align:'right',
					        	 formatter:formatDifferences
					        	 
					         },{
					        	 field:'concordant',
					        	 title:'Concordant',
					        	 sortable:true,
					        	 align:'right'
					         },{
					        	 field:'discordant',
					        	 title:'Discordant',
					        	 sortable:true,
					        	 align:'right'
					         }]
				})
			}
		})
	};
	
	function formatDifferences(value,row,index){
		return row['assigned'] - row['coded'];
	};
});
</script>
</body>
</html>