<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>MKD ${projectName} Dashboard Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<style type="text/css">
table.sample1 {
	border-width: 1px;
	border-spacing: ;
	border-style: hidden;
	border-color: #CECECE;
	border-collapse: collapse;

	background-color: #EDEDED;
}
table.sample1 th {
	border-width: 1px;
	padding: 1px;
	border-style: hidden;
	border-color: white;

	background-color: #CECECE;
	-moz-border-radius: ;
}
table.sample1 td {
	border-width: 1px;
	padding: 1px;
	border-style: solid;
	border-color: white;
	-moz-border-radius: ;
}
</style>


<body>
	
	<table width='100%' border='1' cellspacing='1' cellpadding='1' class="sample1">
	<tr><th>NOTICE TO USERS</th></tr>
	<tr><td>
	This is a hhhh BANK plc. owned computer system. All programs and data on this 
	system are the property of, or licesed by hhhh BANK plc. It is for authorised 
	use only. Users (authorised or unauthorised) have no explicit or implicit 
	expectation of privacy.<br><br>
	
	Any or all uses of this system and all files and data on this system may be 
	intercepted, monitored, recorded, copied, audited, inspected, and disclosed to 
	relevant authorities.<br><br>
	
	By using this system, the user consents to such interception, monitoring, 
	recording, copying, auditing, inspection, and disclosure at the discretion of 
	hhhh BANK plc. authorised personnel.<br><br>
	
	Unauthorised or improper use of this system may result in administrative 
	disciplinary action and civil and criminal penalties. By continuing to use 
	this system you indicate your awareness of and consent to these items and 
	conditions of use. LOG OFF IMMEDIATELY if you do not agree to the conditions 
	stated in this warning.
	 
	</td></tr>
	</table>
	<p>
	<h2>MDS ${projectName} Dashboard Login</h2>
	<form action="${pageContext.request.contextPath}/healthcheck/dashboard" method="post">	
		<fieldset>
			<table>
				<tr>
					<td><label>User ID: </label></td>
					<td><input id="userid" name="userid" type="text" size="20" maxlength="20" /></td>
				</tr>
				<tr>
					<td><label>Password: </label></td>
					<td><input id="password" name="password" type="password"" size="20" maxlength="20" /></td>
				</tr>
			</table>
			<br/>
			<input id="event" name="event" value="submit" type="hidden"/>
			
			<br/>
			<div class="buttons"> 
				<button accesskey="s" onclick="" type="submit" name="submit" value="submit">Submit <kbd>[s]</kbd></button>
			</div>
        </fieldset>
    </form>
    <p align="right"><font size="1">Ver. ${version}</font></p>
</body>
</html>
