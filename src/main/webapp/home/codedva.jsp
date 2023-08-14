<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>VA Data</title>
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
<!-- layout -->
<div class="page-container container-fluid" style="padding:0;">
<div class="row" style="margin-bottom:10px;margin-top:-10px;">
			<div>
				<div class="col-md-2">
					<select data-tags="true" id="sourceTable" name="sourceTable" class="select2">
						<option value=0> - - </option>
					</select>
				</div>
				<div class="col-md-2">
					<select data-tags="true" id="codingType" name="codingType" class="select2">
						<option value='physician'>Physician</option>
					</select>
				</div>
				<div class="col-md-2">
					<select data-tags="true" id="limitTo" name="limitTo" class="select2">
						<option value='10'>Top 10</option>
						<option value='20'>Top 20</option>
					</select>
				</div>
			</div>
</div>
<div id="divAll" class="row">
	<div class="col-sm-12">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h6 class="panel-title"><i class="fa fa-list"></i>Age Group: All</h6>
		</div>
		<div class="row panel-body">
			<div class="col-sm-4" style="margin-top:1.5em; padding:0 5px;">
				<table id="tblAll" data-row-style="rowStyle" ></table>
			</div>
			<div class="col-sm-offset-1 col-sm-6" style="margin-top:1.5em; padding:0 5px;">
				<div id="graph_csmf_all" class="graph-standard" style="height:350px;"></div>
			</div>
		</div>
	</div>
	</div>
</div>

<div class="row">
	<div class="col-sm-12">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h6 class="panel-title"><i class="fa fa-list"></i>Age Group: Adults</h6>
		</div>
		<div class="panel-body">
			<div class="col-sm-4" style="margin-top:1.5em; padding:0 5px;">
				<table id="tblAdults" data-row-style="rowStyle" ></table>
			</div>
			<div class="col-sm-offset-1 col-sm-6" style="margin-top:1.5em; padding:0 5px;">
				<div id="graph_csmf_adults" class="graph-standard" style="height:350px;"></div>
			</div>
		</div>
	</div>
	</div>
</div>

<div class="row">
	<div class="col-sm-12">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h6 class="panel-title"><i class="fa fa-list"></i>Age Group: Children</h6>
		</div>
		<div class="panel-body">
			<div class="col-sm-4" style="margin-top:1.5em; padding:0 5px;">
				<table id="tblChildren" data-row-style="rowStyle" ></table>
			</div>
			<div class="col-sm-offset-1 col-sm-6" style="margin-top:1.5em; padding:0 5px;">
				<div id="graph_csmf_children" class="graph-standard" style="height:350px;"></div>
			</div>
		</div>
	</div>
	</div>
</div>

<div class="row">
	<div class="col-sm-12">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h6 class="panel-title"><i class="fa fa-list"></i>Age Group: Neontates</h6>
		</div>
		<div class="panel-body">
			<div class="col-sm-4" style="margin-top:1.5em; padding:0 5px;">
				<table id="tblNeonates" data-row-style="rowStyle" ></table>
			</div>
			<div class="col-sm-offset-1 col-sm-6" style="margin-top:1.5em; padding:0 5px;">
				<div id="graph_csmf_neonates" class="graph-standard" style="height:350px;"></div>
			</div>
		</div>
	</div>
	</div>
</div>

</div>
<!-- end layout -->
<!-- script -->
<script>
$(document).ready(function(){
	//Parameters
	var settings = <%=Settings.jsettings%>;
	
	loadVATables(settings.va_tables);
	
	var $tblAll = $("#tblAll");
	var $tblAdults = $("#tblAdults");
	var $tblChildren = $("#tblChildren");
	var $tblNeonates = $("#tblNeonates");
	initializeTables();
	
	$(".select2").select2({
		width:'100%',
		minimumResultsForSearch: Infinity,
	});
	
	$("#sourceTable").on('change',function(e){
		
		 $.each(settings.va_tables,function(index,table){
			 
			 if(table.table_name == $(e.currentTarget).val() ){
				 //alert( JSON.stringify(table) );
				 //var vatable = [table];
				 //$("#tblAll tr").remove()
				 //$("#tblAll tbody").empty()
				 
				 loadCodedVA2(table);
			 }
		 })
	 });
	
	function initializeTables(){
		$tblAll.bootstrapTable({
			data:"[]",
			cache:false,
			columns:[{
				field:"position",
				title:"No.",
				align:"center"
			},{
				field:"icdcode",
				title:"ICD Code",
				visible:false
			},{
				field:"icdname",
				title:"Underlying Cause (uCOD)"
			},{
				field:"ratio",
				title:"Percent",
				align:"right"
			}]
		});
		
		$tblAdults.bootstrapTable({
			data:"[]",
			cache:false,
			columns:[{
				field:"position",
				title:"No.",
				align:"center"
			},{
				field:"icdcode",
				title:"ICD Code",
				visible:false
			},{
				field:"icdname",
				title:"Underlying Cause (uCOD)"
			},{
				field:"ratio",
				title:"Percent",
				align:"right"
			}]
		});
		
		$tblChildren.bootstrapTable({
			data:"[]",
			cache:false,
			columns:[{
				field:"position",
				title:"No.",
				align:"center"
			},{
				field:"icdcode",
				title:"ICD Code",
				visible:false
			},{
				field:"icdname",
				title:"Underlying Cause (uCOD)"
			},{
				field:"ratio",
				title:"Percent",
				align:"right"
			}]
		});
		
		$tblNeonates.bootstrapTable({
			data:"[]",
			cache:false,
			columns:[{
				field:"position",
				title:"No.",
				align:"center"
			},{
				field:"icdcode",
				title:"ICD Code",
				visible:false
			},{
				field:"icdname",
				title:"Underlying Cause (uCOD)"
			},{
				field:"ratio",
				title:"Percent",
				align:"right"
			}]
		});
		
		
	}
	function loadVATables(input){
		$.each(input,function(index,table){
			$("#sourceTable").append("<option value='"+table.table_name+"'>"+table.display_name+"</option>");
		})
	}
	//load coded VA data
	function loadCodedVA2(va_table){

		
		//add additional variables
		va_table.rtype=611;
		va_table.limitTo=$("#limitTo").val();
		$.ajax({
			url:"../VAMethods",
			type:"post",
			dataType:"json",
			data:va_table,
			success:function(data){				
				
				// draw tables
				$tblAll.bootstrapTable('load',{ data:data.all.rows });
				$tblAdults.bootstrapTable('load',{ data:data.adults.rows });
				$tblChildren.bootstrapTable('load',{ data:data.children.rows });
				$tblNeonates.bootstrapTable('load',{ data:data.neonates.rows });
				
				//draw graphs				
				if(data.all.total>0)
					drawGraphs("all",data.all);
				if(data.adults.total>0)
					drawGraphs("adults",data.all);
				if(data.children.total>0)
					drawGraphs("children",data.children);
				if(data.neonates.total>0)
					drawGraphs("neonates",data.neonates);
			}
		});
	}
	function loadCodedVA(){
		$.ajax({
			url:"../VAMethods",
			type:"post",
			data:{
				tblName:$("#sourceTable").val(),
				limitTo:$("#limitTo").val(),
				rtype:61
			},
			success:function(data){
				$("#tblAll").bootstrapTable({
					data:data.all.rows,
					columns:[{
						field:"position",
						title:"No.",
						align:"center"
					},{
						field:"icdcode",
						title:"ICD Code",
						visible:false
					},{
						field:"icdname",
						title:"Underlying Cause (uCOD)"
					},{
						field:"ratio",
						title:"Percent",
						align:"right"
					}]
				});
				$("#tblAdults").bootstrapTable({
					data:data.adults.rows,
					columns:[{
						field:"position",
						title:"No.",
						align:"center"
					},{
						field:"icdcode",
						title:"ICD Code",
						visible:false
					},{
						field:"icdname",
						title:"Underlying Cause (uCOD)"
					},{
						field:"ratio",
						title:"Percent",
						align:"right"
					}]
				})
				$("#tblChildren").bootstrapTable({
					data:data.children.rows,
					columns:[{
						field:"position",
						title:"No.",
						align:"center"
					},{
						field:"icdcode",
						title:"ICD Code",
						visible:false
					},{
						field:"icdname",
						title:"Underlying Cause (uCOD)"
					},{
						field:"ratio",
						title:"Percent",
						align:"right"
					}]
				})
				$("#tblNeonates").bootstrapTable({
					data:data.neonates.rows,
					columns:[{
						field:"position",
						title:"No.",
						align:"center"
					},{
						field:"icdcode",
						title:"ICD Code",
						visible:false
					},{
						field:"icdname",
						title:"Underlying Cause (uCOD)"
					},{
						field:"ratio",
						title:"Percent",
						align:"right"
					}]
				})
				
				//draw graphs
				drawGraphs("all",data.all);
				drawGraphs("adults",data.adults);
				drawGraphs("children",data.children);
				drawGraphs("neonates",data.neonates);
			}
		})
	}
	//
	function drawGraphs(name,datatable){
		var graphdata = [];
		var graphticks = [];
		var xmax = 0;
		var ymax = datatable.total;
		$.each(datatable.rows,function(index,value){
			var xy = [value.ratio,ymax-value.position];
			graphdata.push(xy);
			
			var ticks = [ymax-value.position,value.icdname];
			graphticks.push(ticks);
			
			if (xmax < value.ratio){
				xmax = value.ratio
			}
			
		})	
		//console.log(graphdata);
		//console.log(graphticks);
		
		drawMiniGraph("#graph_csmf_"+name,graphdata,graphticks,xmax);
	};
	function drawMiniGraph(placeholder,data,dataticks,maxvalue){
		 
		var dataset = new Array();
		     dataset.push({
		    	data:data,
		    	label: "Dataset title",
		    	color: "#008000",
		    	bars: {
		    		show: true,
		    		align:"center",
		    		barWidth:0.8
		    	}
		 })
	    var options = {
   			series: {
       			bars: { show: true },
   			},
   			bars:{
	   			align: "center",
	           	barWidth: 0.5,
	            horizontal: true,
	            fillColor: { colors: [{ opacity: 0.5 }, { opacity: 1}] },
	            lineWidth: 1
   			},
   			xaxis:{
   				axisLabel: "CSMF",
   			    axisLabelUseCanvas: true,
   			    axisLabelFontSizePixels: 12,
   			    axisLabelFontFamily: 'Verdana, Arial, Helvetica, Tahoma, sans-serif',
   			    axisLabelPadding: 25,
   			 	max: maxvalue+2,
   			    tickColor: "#5E5E5E",
	   			tickFormatter: function (v, axis) {
	   		        //return $.formatNumber(v, { format: "#,###", locale: "us" });
	   				return parseFloat(v).toFixed(0)+"%";
	   		    },
   			    color:"black"
   			},
   			yaxis:{
	   			axisLabel: "Underlying cause",
	            axisLabelUseCanvas: true,
	            axisLabelFontSizePixels: 12,
	            axisLabelFontFamily: 'Verdana, Arial',
	            axisLabelPadding: 25,
	            tickColor: "#5E5E5E",
	            ticks: dataticks,
	            tickLength:5, //remove y lines
	            color: "black"
   			},
   			legend:{
   				show:false,
   				noColumns: 0,
                labelBoxBorderColor: "#858585",
                position: "ne"
   			},
   			grid:{
   				hoverable: true,
                borderWidth: 0.5,
                backgroundColor: { colors: ["#ffffff", "#EDF5FF"] }
   			},
   			colors: ["#FF0000", "#000000"]
		};
		 $.plot(placeholder,dataset, options);
	}
});
</script>
<!-- end script -->
</body>
</html>