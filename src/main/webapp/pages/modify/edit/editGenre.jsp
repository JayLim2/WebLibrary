<%@ page import="java.util.Objects" %>
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
    <title>Изменить жанр</title>
    <%@include file="/libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<jsp:include page="/menu.jsp"/>
<div class="container" style="margin-top: 40px;">
    <h1>Изменить жанр</h1>
    <%
        Object error = request.getAttribute("error");
        if (error != null) {
    %>
    <div class="message-box error">
        <% out.println(error); %>
    </div>
    <%
        }

        Object genreAttr = request.getAttribute("genre");
        if (Objects.equals(genreAttr, null)) return;
    %>

    <%
        Object info = request.getAttribute("info");
        if (info != null) {
    %>
    <div class="message-box info">
        <% out.println(info); %>
    </div>
    <%
        }
    %>

    <form action="" method="post">
        <input type="hidden" name="id" value="${genre.id}"/>
        <div class="table">
            <div class="table-row">
                <div class="table-cell">
                    <b>Название жанра:</b>
                </div>
                <div class="table-cell">
                    <input type="text" name="name" style="width:300px;" value="${genre.name}"/>
                </div>
            </div>

            <div class="table-row">
                <div class="table-cell">
                </div>
                <div class="table-cell">
                    <br/>
                    <input type="submit" class="btn btn-dark" value="Сохранить изменения">
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>
