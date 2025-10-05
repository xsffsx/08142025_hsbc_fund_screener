<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>MKD ${projectName} Dashboard</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
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
#healthCheckTb {
	border: 1px solid gray;
}
#healthCheckTb tr {
	border: 1px solid gray;
	height: 20px;
	width: 200px;
}
#healthCheckTb td {
	border: 1px solid gray;
}

</style>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<%-- <td><b>MDS App Server (${host})</b></td> --%>
		<td><b>MDS App Server (${INSTANCE_NAME})</b></td>
		<td align="right"><font size="1">Request: ${requestTimestamp};&nbsp;&nbsp;&nbsp;Response: ${resposneTimestamp} | Ver. ${version}</font></td>
	</tr>
</table>
<br />
<b>This page refreshes every ${refreshTime} seconds.</b>
<br />
<br />
${results}
*
<FONT COLOR="#008000">OK</FONT>
for positive status with response time; *
<FONT COLOR="#FF0000">Failed</FONT>
for negative status with Exception name.
<br />
<br />
<table id="healthCheckTb">
	${HEALTH_DASHBOARD}
</table>

<table>
	<tr>
		<td>
			<form action="${pageContext.request.contextPath}/healthcheck/dashboard" method="post">
				<input id="event" name="event" value="submit" type="hidden" />
				<div class="buttons">
					<button accesskey="s" onclick="" type="submit" name="submit" value="submit">Refresh</button>
				</div>
			</form>
		</td>
	</tr>
</table>

</body>
</html>
