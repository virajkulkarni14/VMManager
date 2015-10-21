<!-- 
<%@ page language="java" 
    contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"
%>
 -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
                                <meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
		<title>Login Page</title>
	
		<link rel="stylesheet" href="styles.css">
	</head>

	<body>	
	<div id="centerPosition">
		<legend>
	<div id="login">

	<center>
	<h1>Login</h1>
	
	<fieldset style="background-color:lightgrey">
		<form action="LoginServlet" method="get">		
		
			<table>
			<tr><td>
			<div class="form-group">
				UserName</td>
				<td> <input type="text" name="un" value="test" ></td>
			</div>
		</tr>
		<tr><td>
		<div class="form-group">
			Password </td><td><input type="password"  name="pw" value="test">
		</div>
		</td></tr>
		</table>
		
		<button onclick="location.href='/CMPE283_-_Project_2/LoginServlet.java'" class="btn btn-default">Login</button>
	
	
		</form>				
			</fieldset>
				</center>
			</div>
			</legend>
		
			</div>
	</body>
</html>