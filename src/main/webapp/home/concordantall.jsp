<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Concordant VA Data</title>
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
		<h6 class="panel-title"><i class="fa fa-list"></i>Coded VA Data</h6>
		<div class="pull-right"><h6 id="user_name" class="panel-title"><i class="fa fa-user"></i>Username</h6></div>
	</div>
	<div class="panel-body">
		<div class="row">
			<div class="col-md-3" style="margin-left:-10px;">
				<select data-tags="true" id="select_va_table" name="va_table" class="select2">
					<option value='- -'>Select Table</option>
				</select>
			</div>
			<div class="col-md-3" style="margin-left:-10px;">
				<select data-tags="true" id="select_coding_status" name="coding_status" class="select2">
					<option value='all'>All</option>
					<option value='concordant'>Concordant</option>
					<option value='discordant'>Discordant</option>
				</select>
			</div>
			<div class="col-md-4">
			<div class="input-group">
					<span class="input-group-btn">
	                	<button id="btnGo" class="btn btn-default" type="button" style="width:80px;"><i class="fa fa-share" aria-hidden="true"></i>Go</button>
	                </span>
            </div>
			</div>
			
		</div>
		<div class="row" style="margin-top:1.5em; padding:0 5px;">
			<table id="tblCodedVA" data-row-style="rowStyle" ></table>
		</div>
	</div>
</div>
</div>

<!-- display VA Record -->
<div id="divViewVA" class="modal fade" role="dialog" aria-labelledby="divViewVA" aria-hidden="true">
<div class="modal-dialog modal-lg" style="width:90%">
<div class="modal-header">
 	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	<h5 class="modal-title">Display VA Document</h5>
</div>
<div class="modal-body has-padding" style="background-color:#fff;">
	<div class="row">
		<div class="col-sm-6">
			<div class="panel panel-default">
				<div class="panel-heading"><h6 class="panel-title">VA Document</h6></div>
				<div class="panel-body" style="height:445px;overflow-y:scroll;">
					<table 	id="tbl-va-view"
		   				data-pagination="false"
		   				data-striped = "true"
		   				data-sort-name="id"
		   				data-sort-order="asc"
		   				data-classes="table table-no-bordered">
		   			<thead>
		   				<tr>
		   					<th data-field="id" data-sortable="true">ID</th>
		   					<th data-field="label" data-width="60%">Label</th>
		   					<th data-field="value">Response</th>
		   				</tr>
		   			</thead>
		   			</table>
				</div>
			</div>
		</div>
		<div class="col-sm-3">
			<div class="panel panel-default">
				<div class="panel-heading"><h6 class="panel-title">Summary</h6></div>
				<div class="panel-body" id="va-summary" style="height:445px;overflow-y:scroll;">
					<h5>VA ID: 				<span id="summary-va-id" 		class="text-primary"></span> </h5>
					<h5>VA Type: 			<span id="summary-va-type" 		class="text-primary"></span> </h5>
					<h5>Record ID:	 		<span id="summary-va-d2sn" 		class="text-primary"></span> </h5>
					<h5>Interviewer Name:	<span id="summary-int-name" 	class="text-primary"></span> </h5>
					<h5>Interviewer Phone:	<span id="summary-int-phone" 	class="text-primary"></span> </h5>
					<h5>Respondent Name:	<span id="summary-res-name" 	class="text-primary"></span> </h5>
					<h5>Respondent Phone:	<span id="summary-res-phone" 	class="text-primary"></span> </h5>
					<h5>Interview Date:		<span id="summary-int-date" 	class="text-primary"></span> </h5>
					<h5>Interview Start:	<span id="summary-int-start" 	class="text-primary"></span> </h5>
					<h5>Interview End:		<span id="summary-int-end" 		class="text-primary"></span> </h5>
					<h5>Interview Time:		<span id="summary-int-time" 	class="text-primary"></span> </h5>
					<h5>GPS Accuracy		<span id="summary-gps-acc" 		class="text-primary"></span> </h5>
					<h5>Narrative:			<span id="summary-int-narr" 	class="text-primary"></span> </h5>
				</div>
			</div>
		</div>
		<div class="col-sm-3">
			<div class="panel panel-default">
				<div class="panel-heading"><h6 class="panel-title">Coding Details</h6></div>
				<div class="panel-body" id="va-summary" style="height:445px;overflow-y:scroll;">
					<h5>Coder 1: 				<span id="coding-coder1" 				class="coding-text text-primary"></span> </h5>
					<h5>Coder 1 Comments: 		<span id="coding-coder1-comments" 		class="coding-text text-primary"></span> </h5>
					<h5>Coder 1 CoDa: 			<span id="coding-coder1-coda" 			class="coding-text text-primary"></span> </h5>
					<h5>Coder 1 CoDb: 			<span id="coding-coder1-codb" 			class="coding-text text-primary"></span> </h5>
					<h5>Coder 1 CoDc: 			<span id="coding-coder1-codc" 			class="coding-text text-primary"></span> </h5>
					<h5>Coder 1 CoDd: 			<span id="coding-coder1-codd" 			class="coding-text text-primary"></span> </h5>
					<h5>Coder 1 Contributory: 	<span id="coding-coder1-cont" 			class="coding-text text-primary"></span> </h5>
					<h5>Coder 1 Underlying: 	<span id="coding-coder1-underlying" 	class="coding-text text-primary"></span> </h5>
					<hr />
					<h5>Coder 2: 				<span id="coding-coder2" 				class="coding-text text-primary"></span> </h5>
					<h5>Coder 2 Comments: 		<span id="coding-coder2-comments" 		class="coding-text text-primary"></span> </h5>
					<h5>Coder 2 CoDa: 			<span id="coding-coder2-coda" 			class="coding-text text-primary"></span> </h5>
					<h5>Coder 2 CoDb: 			<span id="coding-coder2-codb" 			class="coding-text text-primary"></span> </h5>
					<h5>Coder 2 CoDc: 			<span id="coding-coder2-codc" 			class="coding-text text-primary"></span> </h5>
					<h5>Coder 2 CoDd: 			<span id="coding-coder2-codd" 			class="coding-text text-primary"></span> </h5>
					<h5>Coder 2 Contributory: 	<span id="coding-coder2-cont" 			class="coding-text text-primary"></span> </h5>
					<h5>Coder 2 Underlying: 	<span id="coding-coder2-underlying" 	class="coding-text text-primary"></span> </h5>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="modal-footer">
	<button type="button" class="btn btn-default" data-dismiss="modal">Dismiss</button>
</div>
</div>
</div>
<!-- end display VA Record -->

<!-- scripts -->
<script type="text/javascript" src="../js/plugins/interface/bootstrap-table-export.min.js"></script>
<script>
$(document).ready(function(){
	//initialize
	
	// VAType 1=WHOVA, 2=SmartVA
	var va_type = <%=Settings.va_type%>;
	var admin_level1 = <%=Settings.admin_level1%>;
	var admin_level2 = <%=Settings.admin_level2%>;
	var admin_level3 = <%=Settings.admin_level3%>;
	
	var $va_tables = <%=Settings.va_tables%>;
	var $tblCodedVA = $("#tblCodedVA");
	var $coding;
	var $vatable;
	
	$.each($va_tables,function(ind,table){
		$("#select_va_table").append("<option value='"+table.table_name+"'>"+table.display_name+"</option>");
	})
	
	$(".select2").select2({
		width:'100%',
		minimumResultsForSearch: Infinity,
	});
	
	
	//Load VA Data
	switch( <%= roleid %> ){
		case 1://Data Viewer
			initializeTablesHideCoders();
			break;
		case 2://Coder
			initializeTablesHideCoders();
			break;
		case 3://Administrator
			initializeTables();
			break;
		default: //administrator
			initializeTablesHideCoders();
			break;
	}
	
	
	
	// click events
	$("#btnGo").on('click',function(e){
		loadCodedVA();
	});
	
	$tblCodedVA.on('click-row.bs.table', function(e, row, $element){
		 //displayVARecord(row)
		 $coding = row;
		 //alert(JSON.stringify($coding));
		 getVARec(row['va_uri'],row['va_table'])
	 });
	//functions
	function initializeTablesHideCoders(){
		$tblCodedVA.bootstrapTable({
			data:"[]",
			cache:false,
			showExport:true,
			exportDataType:'all',
			exportOptions:{
				fileName:'vman_coded_va'	
			},
			pageSize: 10,
			pageList: [10, 25, 50, 100],
			pagination: true,
			search: true,
			singleSelect: true,
			showColumns:true,
			columns:[{
				field:"va_uri",
				title:"VA UUID"
			},{
				field:"coder1_coda",
				title:"C1 CoDA",
				sortable:true,
				visible:true
			},{
				field:"coder1_codb",
				title:"C1 CoDB",
				sortable:true,
				visible:false
			},{
				field:"coder1_codc",
				title:"C1 CoDC",
				sortable:true,
				visible:false
			},{
				field:"coder1_codd",
				title:"C1 CoDD",
				sortable:true,
				visible:false
			},{
				field:"coder1_underline",
				title:"C1 Underline",
				sortable:true,
				sortable:true
			},{
				field:"c1_codcont",
				title:"C1 Contributory",
				sortable:true,
				visible:true
			},{
				field:"coder2_coda",
				title:"C2 CoDA",
				sortable:true,
				visible:true
			},{
				field:"coder2_codb",
				title:"C2 CoDB",
				sortable:true,
				visible:false
			},{
				field:"coder2_codc",
				title:"C2 CoDC",
				sortable:true,
				visible:false
			},{
				field:"coder2_codd",
				title:"C2 CoDD",
				sortable:true,
				visible:false
			},{
				field:"coder2_underline",
				title:"C2 Underline",
				sortable:true,
				sortable:true
			},{
				field:"c2_codcont",
				title:"C2 Contributory",
				sortable:true,
				visible:true
			},{
				field:"final_underline",
				title:"Final Underline",
				sortable:true,
				sortable:true
			},{
				field:"va_table",
				title:"Source Table",
				sortable:true,
				visible:false
			},{
				field: "_URI",
		    	title: "View",
		    	align: "center",
		    	formatter: formatEditColumn
			}]
		});
	}
	function initializeTables(){
		$tblCodedVA.bootstrapTable({
			data:"[]",
			cache:false,
			showExport:true,
			exportDataType:'all',
			exportOptions:{
				fileName:'vman_coded_va'	
			},
			pageSize: 10,
			pageList: [10, 25, 50, 100],
			pagination: true,
			search: true,
			singleSelect: true,
			showColumns:true,
			columns:[{
				field:"va_uri",
				title:"VA UUID"
			},{
				field:"coder1",
				title:"Coder 1",
				sortable:true,
				visible:false
			},{
				field:"coder1_coda",
				title:"C1 CoDA",
				sortable:true,
				visible:true
			},{
				field:"coder1_codb",
				title:"C1 CoDB",
				sortable:true,
				visible:false
			},{
				field:"coder1_codc",
				title:"C1 CoDC",
				sortable:true,
				visible:false
			},{
				field:"coder1_codd",
				title:"C1 CoDD",
				sortable:true,
				visible:false
			},{
				field:"coder1_underline",
				title:"C1 Underline",
				sortable:true,
				sortable:true
			},{
				field:"c1_codcont",
				title:"C1 Contributory",
				sortable:true,
				visible:true
			},{
				field:"coder2",
				title:"Coder 2",
				sortable:true,
				visible:false
			},{
				field:"coder2_coda",
				title:"C2 CoDA",
				sortable:true,
				visible:true
			},{
				field:"coder2_codb",
				title:"C2 CoDB",
				sortable:true,
				visible:false
			},{
				field:"coder2_codc",
				title:"C2 CoDC",
				sortable:true,
				visible:false
			},{
				field:"coder2_codd",
				title:"C2 CoDD",
				sortable:true,
				visible:false
			},{
				field:"coder2_underline",
				title:"C2 Underline",
				sortable:true,
				sortable:true
			},{
				field:"c2_codcont",
				title:"C2 Contributory",
				sortable:true,
				visible:true
			},{
				field:"final_underline",
				title:"Final Underline",
				sortable:true,
				sortable:true
			},{
				field:"va_table",
				title:"Source Table",
				sortable:true,
				visible:false
			},{
				field: "_URI",
		    	title: "View",
		    	align: "center",
		    	formatter: formatEditColumn
			}]
		});
	}
	function displayVARecord(row){
		$("#tbl-va-view").bootstrapTable('destroy');
		
		//alert(JSON.stringify(row));
		
		//display summary
		$("#summary-va-id").text( row['_URI'] );
		if( row['IS_ADULT']==1){
			 $("#summary-va-type").text( "Adult" );
		 }else if( row['IS_CHILD']==1){
			 $("#summary-va-type").text( "Child" );
		 }else if( row['IS_NEONATAL']==1){
			 $("#summary-va-type").text( "Neonate" );
		 }else{
			 $("#summary-va-type").text( row['IS_AGE_GROUP'] );	 
		 }
		
		 $("#summary-va-d2sn").text( row[ $vatable.column_record_id.replace( /\"/g,'' ) ] );
		 $("#summary-int-name").text( row[ removeGroupNames($vatable.interviewer_name_column.replace( /\"/g,'' )) ] );
		 $("#summary-int-phone").text( row[ removeGroupNames($vatable.interviewer_phone_column.replace( /\"/g,'' )) ] );
		 $("#summary-res-name").text( row[ removeGroupNames($vatable.respondent_name_column.replace( /\"/g,'' )) ] );
		 $("#summary-res-phone").text( row[ removeGroupNames($vatable.respondent_phone_column.replace( /\"/g,'' )) ] );
		 
		 $("#summary-int-date").text( row['_SUBMISSION_DATE'] );
		 $("#summary-int-start").text( row['START_TIME'] );
		 $("#summary-int-end").text( row['END'] );
		 $("#summary-int-time").text( timeDifference(row['START_TIME'],row['END']) );
		 $("#summary-gps-acc").text( row['GPS_LOCATION_ACC'] );
		 $("#summary-int-narr").text( row[ removeGroupNames($vatable.column_narrative.replace( /\"/g,'' )) ] );
		 
		 
		 //display coding
		 $(".coding-text").text("");
		 
		 // check if coder's name are hidden and do likewise
		 // gethiddencolumns
		 hcols = $tblCodedVA.bootstrapTable('getHiddenColumns').map(function(it){
			 return it.field;
		 })
		 if(jQuery.inArray("coder1",hcols) !== -1){
			 $("#coding-coder1").text( " ********** " );
		 }else{
			 $("#coding-coder1").text( $coding['coder1'] );
		 }
		 
		 if(jQuery.inArray("coder2",hcols) !== -1){
			 $("#coding-coder2").text( " ********** " );
		 }else{
			 $("#coding-coder2").text( $coding['coder2'] );
		 }
		 
		 
		 
		 $("#coding-coder1-comments").text( $coding['coder1_comments'] );
		 $("#coding-coder1-coda").text( $coding['coder1_coda'] );
		 $("#coding-coder1-codb").text( $coding['coder1_codb'] );
		 $("#coding-coder1-codc").text( $coding['coder1_codc'] );
		 $("#coding-coder1-codd").text( $coding['coder1_codd'] );
		 $("#coding-coder1-cont").text( $coding['coder1_cont'] );
		 $("#coding-coder1-underlying").text( $coding['coder1_underline'] );
		 
		 $("#coding-coder2-comments").text( $coding['coder2_comments'] );
		 $("#coding-coder2-coda").text( $coding['coder2_coda'] );
		 $("#coding-coder2-codb").text( $coding['coder2_codb'] );
		 $("#coding-coder2-codc").text( $coding['coder2_codc'] );
		 $("#coding-coder2-codd").text( $coding['coder2_codd'] );
		 $("#coding-coder2-cont").text( $coding['coder2_cont'] );
		 $("#coding-coder2-underlying").text( $coding['coder2_underline'] );
		
		 //display va record
		 switch(va_type){
			case 1:
				$("#tbl-va-view").bootstrapTable({
					data:renderWHOVA(row,false)
				})
				break;
			case 2:
				$("#tbl-va-view").bootstrapTable({
					data:renderSmartVA(row)
				})
				break;
		}
		 
		 
		 
	}
	function getVARec(tblname,vauri){
		$.ajax({
			url:"../VAMethods",
			type:"post",
			data:{
				table_name:tblname,
				va_uri:vauri,
				rtype:64
			},
			success:function(data){
				displayVARecord(data);
				return data;
			}
		});
	}
	function loadCodedVA(){
		$.ajax({
			url:"../VAMethods",
			type:"post",
			data:{
				table_name:$("#select_va_table").val(),
				coding_status:$("#select_coding_status").val(),
				rtype:612
			},
			success:function(data){
				$tblCodedVA.bootstrapTable('load',{ data:data.rows });
			}
		});
		
		// get the current va table
		$.each($va_tables,function(ind,table){
			if(table.table_name==$("#select_va_table").val())
				$vatable = table;
		})
		
	};
	function formatEditColumn(value, row, index){
		return [
	        "<a href='#' data-toggle='modal' data-target='#divViewVA'>",
	        "<i class='fa fa-eye' aria-hidden='true'></i>",
	        "</a>"].join('');
	}
	function timeDifference(time_sta,time_end){
		 try{
			 var dt1 = new Date(time_sta);
			 var dt2 = new Date(time_end)
			 var msec = dt2.getTime() - dt1.getTime();
			 var mins = Math.floor(msec / 60000);
			 //var hrs = Math.floor(mins / 60);
			 //var days = Math.floor(hrs / 24);
			 //var yrs = Math.floor(days / 365);
			 return mins;
			 
		 }catch(error){
			return 0;	 
		 }
	 }
	function removeGroupNames(input){
		return input.substring(input.indexOf("_ID")+1,input.length);
	}
});
</script>
</body>
</html>