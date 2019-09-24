<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 24.09.2019
  Time: 14:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div id="result">
    <h3>${requestScope.message}</h3>
</div>
<table>
    <tr>
        <th>Name</th>
        <th>Flag</th>
        <th>Email</th>
    </tr>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>${user.value}</td>
            <td>${user.flag}</td>
            <td>${user.email}</td>
        </tr>
    </c:forEach>
</table>
<div>
    <a href="/">Zurück</a>
</div>

</body>
</html>
