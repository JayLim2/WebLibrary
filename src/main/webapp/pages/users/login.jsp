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
    <title>Войти в систему</title>
    <%@include file="/libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<jsp:include page="/menu.jsp"/>
<div class="container" style="margin-top: 40px;">
    <h1>Войти в систему</h1>
    <%
        Object error = request.getAttribute("error");
        if (error != null) {
    %>
    <div class="message-box error">
        <% out.println(error); %>
    </div>
    <%
        }
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
        <div class="table">
            <div class="table-row">
                <div class="table-cell">
                    <b>Логин:</b>
                </div>
                <div class="table-cell">
                    <input type="text" name="login" style="width:300px;" value="${login}"/>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Пароль:</b>
                </div>
                <div class="table-cell">
                    <input type="password" name="password" style="width:300px;" value="${password}"/>
                </div>
            </div>

            <div class="table-row">
                <div class="table-cell">
                </div>
                <div class="table-cell">
                    <br/>
                    <input type="submit" class="btn btn-dark" value="Войти">
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>
