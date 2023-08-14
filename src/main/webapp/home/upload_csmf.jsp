<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Upload CSMF</title>
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
		<div class="panel-heading"><h6 class="panel-title"><i class="fa fa-upload"></i>Upload CSMF Data</h6></div>
		<div class="panel-body">
			<div class='row' style='margin-top:1em;'>
				<div class='col-md-12'>
				<form class='form-horizontal'>
					<div class='form-group'>
						<label class='control-label col-sm-2' for='inputFile'>Locate file</label>
						<div class='col-sm-4'>
							<input id='inputFile' type='file' class='form-control'/>
						</div>
						<div id='uploadMsg' class='text-danger col-sm-6'></div>
					</div>
					<div class='form-group'>
						<div class='col-sm-10 col-sm-offset-2'>
							<button id='btnUpload' type='button' class='btn btn-default'>Process Import</button>
						</div>
					</div>
				</form>
				</div>
			</div>
		</div>
		</div>
	</div>
	
</div>
<!-- end first row -->
<!-- panels -->
<!-- alert box -->
<div id="divAlert" class="modal fade" role="dialog">
<div class="modal-dialog modal-md" style="background-color:#fff;">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h5 class="modal-title">Additional Info</h5>
	</div>
	<div class="modal-body has-padding edit-content">
		Some context for alert
	</div>
	<div class="modal-footer">
	<button type="button" class="btn btn-default" data-dismiss="modal">OK</button>
</div>
</div>
</div>
<!-- end alert box -->
<!-- end panels -->
<script type="text/javascript">
$(document).ready(function () {
	var errors;
	var impdata = null, 
		impcols = null;

	
	$("#inputFile").on("change",function(evt){
		var ext = $("input#inputFile").val().split(".").pop().toLowerCase();
		
		//$("#uploadMessage").text(ext);
		$("#uploadMsg").empty();
		if($.inArray(ext, ["csv"]) == -1) {
			$("#uploadMsg").html("<i class='fa fa-exclamation-triangle' aria-hidden='true'></i> Invalid file extension, expected .csv");
			return false;
		}
		if(!(window.File && window.FileReader && window.FileList && window.Blob)) {
			$("#uploadMsg").html("<i class='fa fa-exclamation-triangle' aria-hidden='true'></i> Invalid file API");
			return false;
	    }
		
		var file = evt.target.files[0];
        var reader = new FileReader();
        reader.readAsText(file);
        
        reader.onload = function(event) {
        	csvData = event.target.result;
            impdata = $.csv.toArrays(csvData);
            impcols = impdata[0];
            
            if (impdata && impdata.length > 0) {
            	$("#uploadMsg").html(impdata.length - 1 + " records found");
            }else{
            	$("#divAlert").find(".edit-content").html("This file has no records");
    			$("#divAlert").modal("show");
    			return;
            }
        }
        
        reader.onerror = function() {
        	$("#uploadMsg").html("<i class='fa fa-exclamation-triangle' aria-hidden='true'></i> Unable to read "+file.fileName);
        	errors = true;
        };
		
		
	});
	
	$("#btnUpload").on("click", function(evt){
		if (impdata && impdata.length > 0){
			alert("pass");
		}else{
			$("#divAlert").find(".edit-content").html("No import file supplied");
			$("#divAlert").modal("show");
		}
		
	})
})
</script>
</body>
</html>