<%--
  Created by IntelliJ IDEA.
  User: JayLim
  Date: 02.03.2019
  Time: 17:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
    <%@include file="libs.jsp" %>
</head>
<body>
<div class="container" style="margin-top: 150px">
    <div class="page-header">
        <h1>Упс, страница не найдена</h1>
    </div>
    <div class="page-information">
        Такой страницы нет и никогда не было. Ну, возможно, и была когда-то. Но сейчас ее точно нет. Наверное.
    </div>
    <br/>
    <p>
        <input type="button" class="btn btn-primary" value="Вернуться назад" onclick="window.location.href = '/'">
    </p>
</div>
</body>
</html>
