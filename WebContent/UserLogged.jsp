<%@ page language="java" contentType="text/html; charset=windows-1256" pageEncoding="windows-1256"
         import="UserModel"%>
 
   <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
   "http://www.w3.org/TR/html4/loose.dtd">

   <html>

      <head>
         <meta http-equiv="Content-Type" 
            content="text/html; charset=windows-1256">
         <title>   User Logged Successfully   </title>
         <script>
        function enableVMNameText()
        {
        	document.getElementById("vm-Name").enabled = true;
        }
        </script>
        <link rel="stylesheet" href="styles.css">
      </head>
	
      <body>
			
     
        
           <% UserModel currentUser = (UserModel) (session.getAttribute("currentSessionUser"));%>
			
            Welcome <%= currentUser.getFirstName() + " " + currentUser.getLastName() %> 
            
          <form action="DisplayVMDetails" method="get">

<fieldset>
<table>
<tr><td>
<button type="submit" name="Instance" value="Instance" onclick="location.href='/CMPE283_-_Project_2/VMInfo.jsp'">vm info</button></td></tr></table>
<br>
</form>

<form action="CreateNewVM" method="get">
<table><tr><td>
<input type="submit" name="Create-VM" value="Create VM" "></td><td>
<input type="text" id="vm-Name" name="vm-Name" value="" ></td></tr>
</table>
<br>
</form>

<form action="DeployNewVM" method="get">
<table>
<tr><td>
<input type="submit" name="Deploy-VM" value="Deploy VM"></td></tr>
<tr><td>
<input type="radio" name="choiceOS" value="Ubuntu" id="opt1">  Ubuntu</td></tr>
<tr><td>
<input type="radio" name="choiceOS" value="Windows" id="opt2">Windows</td></tr>
<tr><td>
<input type="text" id="vm-deployName" name="vm-deployName" value=""></td></tr>
<br>
</fieldset>
</table>
</form>


<form action="CreateCPUStatistics" method="get">
<table><tr><td>
<button type="button" name="Create-CPUStatistics" value="View CPU Statistics" onclick="location.href='/CMPE283_-_Project_2/CPUStatisticsJsp.jsp'">Statistics</button></td></tr>
</table>
<br>
</form>       
  
</body>
	
   </html>
