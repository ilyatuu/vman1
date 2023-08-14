<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Assign VA</title>
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
<!-- main container -->
<div class="page-container container-fluid" style="padding:0;">
<div class="panel panel-default">
	<div class="panel-heading">
		<h6 class="panel-title"><i class="fa fa-stethoscope"></i>Assign VA to Coders</h6>
		<div class="pull-right"><h6 id="user_name" class="panel-title"><i class="fa fa-user"></i>Username</h6></div>
	</div>
	<div class="panel-body">
	
		<div>
		<h6 class="panel-title"><i class="fa fa-list"></i>List of Coders</h6>
		<br />
		<table id="tblCoders" data-striped="true"></table>
		</div>
		
	
	</div>
</div>
</div>

<!-- div assign va -->
<div id="divAssignVA" class="modal fade" tabindex="-1" role="dialog">
<div class="modal-dialog modal-xl" style="width:95% !important;">
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	<h5 class="modal-title">Assign VA Record to Coder</h5>
</div>
<div class="modal-body has-padding" style="background-color:#fff;">
	<div class="row">
		<!-- Left side -->
		<div class="col-sm-4">
			<div class="panel panel-default">
			<div class="panel-heading has-padding"><h5>Physician Details</h5></div>
			<div class="panel-body">
				<form class="form-horizontal" role="form" id="frmAssignVA" name="frmAssignVA">
					<div class="form-group">
					<label for="coderName" class="col-sm-4 control-label">Name</label>
					<div class="col-sm-8">
          				<input type="text" class="form-control" name="coderName" placeholder="Name" readonly>
          				<input type="hidden" name="coderId" id="coderId" value="0" >
          				<input type="hidden" name="assignType" value="assign" >
          				<input type="hidden" name="rtype" value="31">
      				</div>
					</div>
					<div class="form-group">
					<label for="coderType" class="col-sm-6 control-label">Assign As</label>
					<div class="col-sm-6">
          				<select id="coderType" name="coderType" class="select2_ctl" style="width : 100%;">
          					<option value="1">Coder 1</option>
          					<option value="2">Coder 2</option>
          				</select>
      				</div>
					</div>
					<div class="form-group">
					<label for="pastAssignment" class="col-sm-6 control-label">Past Assignment</label>
					<div class="col-sm-6">
          				<input type="text" class="form-control text-right" name="pastAssignment" placeholder="0" readonly>
      				</div>
					</div>
					<div class="form-group">
					<label for="totalCompleted" class="col-sm-6 control-label">Total Completed</label>
					<div class="col-sm-6">
          				<input type="text" class="form-control text-right" name="totalCompleted" placeholder="0" readonly>
      				</div>
					</div>
					<div class="form-group">
					<label for="newAssignment" class="col-sm-6 control-label">New Assignment</label>
					<div class="col-sm-6">
          				<input type="text" class="form-control text-right" name="newAssignment" placeholder="0" readonly>
      				</div>
					</div>
				</form>
			</div>
			</div>
		</div>
		<!-- Right side -->
		<div class="col-sm-8">
			<div class="panel panel-default">
			<div class="panel-heading has-padding">
				<div class="row">
					<div class="col-sm-4"><h5>List of VA Records</h5></div>
					<div class="col-sm-4 text-right"><h6>Select Table:</h6></div>
					<div class="col-sm-4">
					<select id="va_table" name="va_table" class="select2_ctl" style="width : 100%;">
						<option value='none'> - - </option>
          			</select>
				</div>					
				</div>
			</div>
			<div class="panel-body">
				<div class="row" style="margin-top:-.5em; padding:0 5px;">
					<div id="toolbar">
        				<button id="btnAssign" class="btn btn-default" disabled>
            				<i class="glyphicon glyphicon-tags"></i> Assign
        				</button>
        				<button id="btnUnassign" class="btn btn-default" disabled>
            				<i class="glyphicon glyphicon-share"></i> Unassign
        				</button>
    				</div>
					<table id="tblAssignment" 
						data-row-style="rowStyle"
						data-toolbar="#toolbar" >
					</table>
				</div>
			</div>
			</div>
		</div>
	</div>
</div>
<div class="modal-footer">
	<button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">Close</button>
</div>
</div>
</div>
<!-- modal assign va -->

<!-- scripts -->
<script type="text/javascript" src="../js/plugins/interface/bootstrap-table-export.min.js"></script>
<script>
$(document).ready(function(){
	var RowIndex,
		RoleId,
		$tblAssignment = $("#tblAssignment");
	//initialize
	
	var va_table;
	
	// VAType 1=WHOVA, 2=SmartVA
	var va_type = <%=Settings.va_type%>;
	var admin_level1 = <%=Settings.admin_level1%>;
	var admin_level2 = <%=Settings.admin_level2%>;
	var admin_level3 = <%=Settings.admin_level3%>;
	
	var $tblCoders = $("#tblCoders");
	
	var selections = [],
	$tblAssignment = $("#tblAssignment"),
	$btnAssign = $("#btnAssign"),
	$btnUnassign = $("#btnUnassign");
	
	//set controls
	$(".select2_ctl").select2({
		minimumResultsForSearch: Infinity
	});
	
	var settings = <%=Settings.jsettings%>;
	
	
	$.each(settings.va_tables,function(index,table){
		$("#va_table").append("<option value='"+table.table_name+"'>"+table.display_name+"</option>");
	})
	
	getCoders();
	loadAssignments2();
	
	// Events handlers
	$tblCoders.on('click-row.bs.table', function(e, row, $element){
		$("input[name='update_username']").val(row['username']);
		
		$("input[name='update_fullname']").val(row['fullname']);
		$("input[name='update_phone']").val(row['phone']);
		
		$("input[name='update_organization']").val(row['organization']);
		
		//Edit VA Modal
		$("input[name='coderName']").val( row['fullname'] );
		$("input[name='coderId']").val( row['id'] );
		
		RowIndex = $element.index();
		
	});
	
	$("#va_table").on("change",function(e){
		
		$.each(settings.va_tables, function(idx,table){
			if(table.table_name == e.target.value){
				va_table = table;
			}
		})
		
		//$tblAssignment.bootstrapTable("refresh");
		refreshAssignment();
	})
	
	$tblAssignment.on('check.bs.table uncheck.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        $btnAssign.prop('disabled', !$tblAssignment.bootstrapTable('getSelections').length);
        $btnUnassign.prop('disabled', !$tblAssignment.bootstrapTable('getSelections').length);
        
        selections = getIdSelections();
        
        //update selection
        $("input[name='newAssignment']").val( selections.length  );
	});
	
	function refreshAssignment(){
		$.ajax({
			url:"../GetBootTable",
			type:"post",
			data:{
				rtype: 103,
				tablename: $("#va_table").val(),
				vatable:JSON.stringify(va_table)
			},
			success:function(data){
				$tblAssignment.bootstrapTable('load', data.rows);
				$tblAssignment.bootstrapTable("refresh");
			}
		})
	}
	function getCoders(){
		$.ajax({
			url:"../UserMethods",
			data:"rtype=131",
			type:"post",
			dataType:"json",
			success: function(data){
				$tblCoders.bootstrapTable({
					data:data.rows,
					columns: [{
						field:'id',
						title:'UserId',
						visible:false
					},{
					   	field: 'fullname',
				    	title: 'Full Name',
				    	sortable: true
				    },{
					   	field: 'phone',
					   	title: 'Phone',
					   	sortable: true
				    },{
				    	field: 'username',
				    	title: 'Username',
				    	sortable: true
				    },{
					  	field: 'organization',
					   	title: 'Organization',
					   	sortable: true
				    },{
					  	field: 'lastlogin',
					   	title: 'Last login',
					   	sortable: true
				    },{
					  	field: 'ever_assignedva',
					   	title: 'Cum. VA Ever Assigned',
					   	align: 'right',
					   	sortable: true
				    },{
				    	title: 'Assign VA',
				    	align: 'center',
				    	formatter: formatEditColumn
				    }]
				})
			}
		})
	}
	
	function loadAssignments2(){
		$tblAssignment.bootstrapTable({
			url:"../GetBootTable",
			method:"post",
			pagination: true,
			sidePagination: "server",
			showColumns: true,
			contentType: 'application/x-www-form-urlencoded',
			idField:'_URI',
			clickToSelect:true,
			search: false,
			pageSize: 10,
	    	pageList: [10, 25, 50, 100],
			queryParams: function(p){
				return{
					rtype: 103,
					tablename: $("#va_table").val(),
					limit : this.pageSize,
					offset: this.pageSize * (this.pageNumber - 1),
					//search: this.searchText,
					searchBy : $("#searchBy").val(),
					searchVal: $("#searchVal").val(),
					orderBy:	this.sortName,
					orderVal:  this.sortOrder
				}
			},onDblClickRow: function (row, $element) {
				//alert( row['_URI'] );
	            //var key = row["key"];               
	            $("#divViewVA").modal("show");
	        },
			columns: [{
		    	field: 'Assign',
		    	checkbox:true,
		    	align:'center',
		    	valign:'middle'
		    },{
			   	field: '_URI',
		    	title: 'URI',
		    	visible: false
		    },{
		    	field:'va_table',
		    	title:'Source Table',
		    	visible:false,
		    },{
			   	field: 'coder1',
		    	title: 'Coder 1',
		    	sortable: true
		    },{
			   	field: 'coder2',
		    	title: 'Coder 2',
		    	sortable: true
		    },{
			   	field: 'death_category',
		    	title: 'VA Type',
		    	sortable: true
		    },{
			   	field: 'death_loc_level1',
		    	title: admin_level1,
		    	sortable: true
		    },{
			   	field: 'death_loc_level2',
		    	title: admin_level2,
		    	sortable: true
		    },{
		    	field:'interviewer_name',
		    	title:'Interviewer',
		    	sortable:true,
		    	visible:false
		    },{
		    	field:'interview_date',
		    	title:'Interview Date',
		    	sortable:true,
		    	visible:false
		    }]
		});
	}
	
	function formatEditColumn(value, row, index){
		
		return [
	        "<a href='#' data-toggle='modal' data-target='#divAssignVA'>",
	        "<i class='fa  fa-user-md' aria-hidden='true'></i>",
	        "</a>"
	        ].join('');
	}//end function format
});
</script>
</body>
</html>