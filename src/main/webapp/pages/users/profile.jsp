<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: JayLim
  Date: 11.03.2019
  Time: 21:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Мой профиль</title>
    <%@include file="/libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<jsp:include page="/menu.jsp"/>
<div class="container" style="margin-top: 40px;">
    <h1>Мой профиль</h1>

    <b>Ник пользователя в системе: </b> <c:out value="${login}"/>
    <br/>
    <b>Имя: </b> <c:out value="${firstName}"/>
    <br/>
    <b>Фамилия: </b> <c:out value="${lastName}"/>

    <div style="margin-top: 20px">
        <a class="btn btn-dark" href="${pageContext.request.contextPath}/user/favoriteGenres">Мои любимые жанры</a>
        <a class="btn btn-dark" href="${pageContext.request.contextPath}/user/recommendations">Мои рекомендации</a>
        <a class="btn btn-danger" href="${pageContext.request.contextPath}/logout">Выйти</a>
    </div>
</div>
</body>
</html>
