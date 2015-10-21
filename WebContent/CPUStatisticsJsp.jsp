<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<head>

		<link rel="stylesheet" href="styles.css">
 <script type="text/javascript" src="https://www.google.com/jsapi"></script>
 <%
 ArrayList xTime = (ArrayList)(session.getAttribute("xTime"));
 ArrayList yCPU = (ArrayList)(session.getAttribute("yCPU"));
 
 System.out.println("CPU session "+xTime);
 System.out.println("CPU session "+yCPU);
 //for(int i=0; i<xTime.size();i++)
 //	System.out.println(xTime.get(i));
 %>
 <script>
 
 google.load('visualization', '1', {packages: ['corechart', 'bar']});
   // google.load('visualization', '1', {packages: ['corechart']});
      google.setOnLoadCallback(drawChart);
      var yCPU=[];
      var xTime=[];
 <% for (int i=0; i<yCPU.size(); i++) { %>
 yCPU[<%= i %>] = "<%= yCPU.get(i) %>"; 
 <% } %>
 <% for (int i=0; i<xTime.size(); i++) { %>
 xTime[<%= i %>] = "<%= xTime.get(i) %>"; 
 <% } %>
  //      console.log(xTime+" "+yCPU);
        
  function drawChart() {
var data = new google.visualization.DataTable();
        data.addColumn('date', 'date');
        data.addColumn('number', 'cpu useage');
        //data.addColumn('date', 'Time of Day');
        /*data.addRows([
         [new Date(2016, 03, 11), 1886], [new Date(2015, 10, 11), 1549], [new Date(2015, 30, 11), 1478]]); */
        
        for(var i=0; i<yCPU.length;i++)
        	{
        	console.log(new Date(xTime[i])+""+yCPU);
       // data.addRow[[new Date(xTime[i])], (yCPU[i])];
       data.addRow([new Date(xTime[i]), parseInt(yCPU[i])]);
        	}

        var options = {
          title: 'cpu useage',
          width: 900,
          height: 500,
        };

        var chart = new google.visualization.BarChart(document.getElementById('chart_div'));

        chart.draw(data, options);

        /*var button = document.getElementById('change');

        button.onclick = function () {

          // If the format option matches, change it to the new option,
          // if not, reset it to the original format.
          options.hAxis.format === 'M/d/yy' ?
          options.hAxis.format = 'MMM dd, yyyy' :
          options.hAxis.format = 'M/d/yy';

          chart.draw(data, options);
        };*/
      } 
 
 </script> 
 </head>

<body>
  <div id="chart_div"></div>
  <button id="change">change</button>
 
</body>
</html>