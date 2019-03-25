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
    <title>Добавить издателя</title>
    <%@include file="/libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<jsp:include page="/menu.jsp"/>
<div class="container" style="margin-top: 40px;">
    <h1>Добавить издателя</h1>
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
                    <b>Название издателя:</b>
                </div>
                <div class="table-cell">
                    <input type="text" name="title" style="width:300px;" value="test издатель"/>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Адрес:</b>
                </div>
                <div class="table-cell">
                    <input type="text" name="address" style="width:300px;"/>
                </div>
            </div>

            <div class="table-row">
                <div class="table-cell">
                </div>
                <div class="table-cell">
                    <br/>
                    <input type="submit" class="btn btn-dark" value="Добавить издателя">
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>
