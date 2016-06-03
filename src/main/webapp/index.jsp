<%@ page import="ru.glaizier.util.Utils" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<h3>PowerliftersDb test interface to Db</h3>
<p><a href=".<%=Utils.getSimplePath()%>">Simple connection</a></p>
<p><a href=".<%=Utils.getPpoolPath()%>">PostgreSql Connection pool</a></p>
<p><a href=".<%=Utils.getTpoolPath()%>">Tomcat Connection pool</a></p>
<p><a href=".<%=Utils.getHibernatePath()%>">Hibernate</a></p>
</body>
</html>