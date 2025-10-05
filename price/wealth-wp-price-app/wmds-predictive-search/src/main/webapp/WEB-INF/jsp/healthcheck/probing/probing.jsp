<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<html>
<head>
	<title>WMDS Probing</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<c:if test="${result == 'success'}">
		<b><FONT COLOR="#008000">OK, System time is ${systemTime}</FONT></b> 
		<br />
	</c:if>
	<c:if test="${result == 'failure'}">
		<b><FONT COLOR="#FF0000">Failed with ${systemTime}</FONT></b> 
		<br />
	</c:if>
	<c:if test="${result == 'samlGenerate'}">
		<b>${samlString}</b> 
		<br />
	</c:if>
</body>
</html>