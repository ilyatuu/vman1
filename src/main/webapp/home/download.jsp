<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Download Data</title>
<%@ page import="vman2.Settings" %>
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
%>
</head>
<body>
<!-- first row -->
<div class="row">
	<div class="col-sm-12">
		<div class="panel panel-default">
		<div class="panel-heading">
			<h6 class="panel-title"><i class="fa fa-download"></i>Download Data
				<small>(select source table below or alternative use the 
					<a href="#" data-toggle="popover" 
								title="Data API" 
								data-content="https://<vman.org>/api?key=123456&type=json">api</a>)
				</small>
			</h6>
		</div>
		<div class="panel-body">
			<div class="row">
				<div class="col-sm-6">
					<div class="col-sm-4"><h6>Select Data Source:</h6></div>
					<div class="col-sm-6">
						<select id="select_va_table" name="select_va_table" class="select2_ctl" style="width : 100%;">
							<option value='none'> - - </option>
			     
			          	</select>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-8 col-sm-offset-2">
					<table id="tblCodedVA" data-row-style="rowStyle" ></table>
				</div>
			</div>
		</div>
		</div>
	</div>
	
</div>
<!-- end first row -->
<script type="text/javascript" src="../js/plugins/interface/bootstrap-table-export.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
	$('[data-toggle="popover"]').popover();

	var va_tables = <%=Settings.va_tables%>;
	$.each(va_tables,function(ind,table){
		$("#select_va_table").append("<option value='"+table.table_name+"'>"+table.display_name+"</option>");
	})
	
	$(".select2_ctl").select2({
		minimumResultsForSearch: Infinity
	});
	$("#select_va_table").on("change",function(e){
		displayCodedVA( $("#select_va_table").val() );
	})
	function displayCodedVA(tblName){
		$.ajax({
			url:"../VAMethods",
			data:"rtype=62&tblName="+tblName,
			type:"post",
			dataType:"json",
			success: function(data){
				$("#tblCodedVA").bootstrapTable("destroy");
				$("#tblCodedVA").bootstrapTable({
					data:data.rows,
					showExport:true,
					exportDataType:'all',
					exportOptions:{
						fileName:'vman_coded_va'	
					},
					pageSize: 10,
					pageList: [10, 25, 50, 100, 1000, 5000],
					pagination: true,
					search: true,
					columns: [{
					   	field: 'va_uri',
				    	title: 'VA ID',
				    	halign:'center',
				    	sortable: false
				    },{
					   	field: 'icdname',
				    	title: 'Underline Cause of Death',
				    	halign:'center',
				    	align: 'right',
				    	sortable: true
				    },{
					   	field: 'icdcode',
				    	title: 'ICD10',
				    	halign:'center',
				    	align: 'right',
				    	sortable: true
				    }]
				})
			}
		})
	}
});
</script>
</body>
</html>