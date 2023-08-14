<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<%@ page import="vman2.Settings" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%
	JSONArray vatables = null;
	JSONObject settings = Settings.jsettings;
	vatables = settings.getJSONArray("va_tables");
%>
<title>VMan Maps</title>
</head>
<body>
<div id="map" style="width:100%;height:550px;"></div>

<script>	
	//center of the map
	var center = [-6.5, 36];
	

	// Create the map
	var map = L.map('map').setView(center, 6);
	
	// Create layer
	var layer = new L.TileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png');

	
	map.addLayer(layer);         // Adding layer to the map
	
	// Options for the marker
	
	
	var iconOptions = {
		iconUrl: '../images/placeholder.png',
		iconSize: [10, 10]
	}
	
	var customIcon = L.icon(iconOptions);
	
	var markerOptions = {
		title: "Interview Location",
		clickable: true,
		draggable: true,
		icon: customIcon
	}
	
	//var marker = L.marker( center, markerOptions );
	//marker.bindPopup('Center').openPopup();
	//marker.addTo(map);
	
	var popup;
	var vatable;
	var vatables = <%= vatables %>

	$.each(vatables,function(indx,table){
		vatable = {
			"name" : table.table_name,
			"col_lat" : table.gps_lat,
			"col_lon" : table.gps_lon,
			"col_alt" : table.gps_alt,
			"col_acc" : table.gps_acc,
			"col_det" : table.gps_det,
			"col_intv": table.interviewer_name_column
		}
	})
	
	$.ajax({
			url:"../GetMapData",
			method:"POST",
			datatype:"json",
			data:{
				rtype:1,
				table:JSON.stringify(vatable)
			},success:function(data){
				$.each(data,function(indx,gps){
					popup = "Interviewer: "+gps.intv+"<br />GPS accuracy: "+gps.acc+"<br />Interview location: "+gps.det;
					L.marker( [gps.lat,gps.lon], markerOptions ).bindPopup(popup).openPopup().addTo(map);	
				})
			}
	});
	
    $(document).ready(function() {
    	
	
    });
	
</script>
</body>
</html>