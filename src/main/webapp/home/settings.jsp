<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Settings</title>
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
<div class="row">
<div class="col-sm-12">
<div class="panel panel-default">
	<div class="panel-heading">
		<h6 class="panel-title"><i class="fa fa-cog"></i>Settings</h6>
	</div>
	<div class="panel-body">
		<form class="form-horizontal">
			<div class="form-group row">
			<label class="col-sm-2 control-label">Application name:</label>
			<div class="col-sm-4">
				<input name="appname" class="form-control" />
			</div>
			</div>
			
			<div class="form-group row">
			<label class="col-sm-2 control-label">Application page title:</label>
			<div class="col-sm-4">
				<input name="page_title" class="form-control" />
			</div>
			</div>
			
			<div class="form-group row">
			<label class="col-sm-2 control-label">Application page sub title:</label>
			<div class="col-sm-4">
				<input name="page_sub_title" class="form-control" />
			</div>
			</div>
			
			<h5>VA Tables</h5>
			<div class="row">
				<div class="col-sm-12">
					<div id="toolbar">
						<button type="button" class="btn btn-default"><i class="fa fa-plus"></i>Add Table</button>
						<button type="button" class="btn btn-default"><i class="fa fa-save"></i>Update Settings</button>
					</div>
					<table id="tblVATables" data-toolbar="#toolbar" data-toggle="table" data-show-toggle="true" data-card-view="true">
						<thead>
							<tr>
								<th rowspan=2 data-field="display_name" data-halign="center" data-valign="middle" >Display Name</th>
								<th rowspan=2 data-field="table_name"   data-halign="center" data-valign="middle" >DB Table Name</th>
								<th colspan=7 data-halign="center">Important Columns</th>
							</tr>
							<tr>
								<th data-field="id_column"		data-halign="center">ID</th>
								<th data-field="gender_column"	data-halign="center">Gender</th>
								<th data-field="adult_column"	data-halign="center">Adult</th>
								<th data-field="child_column"	data-halign="center">Child</th>
								<th data-field="neonate_column"	data-halign="center">Neonate</th>
								<th data-field="interviewer_name_column"	data-halign="center">Interviewer Name</th>
								<th data-field="interviewer_phone_column"	data-halign="center">Interviewer Phone</th>	
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</form>
	</div>
</div>
</div>
</div>
<!-- end of page -->
<!-- scripts -->
<script type="text/javascript">
$(document).ready(function(){
	
	var settings = <%=Settings.jsettings%>;
	$('input[name="appname"]').val(settings.app_name)
	$('input[name="page_title"]').val(settings.page_title)
	$('input[name="page_sub_title"]').val(settings.page_sub_title)
	
	//$("#tblVATables").bootstrapTable("destroy");
	$("#tblVATables").bootstrapTable({
		data:settings.va_tables
	});
	
});

</script>
</body>
</html>