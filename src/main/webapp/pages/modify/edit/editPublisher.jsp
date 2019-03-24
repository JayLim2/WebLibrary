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
    <title>Изменить издателя</title>
    <%@include file="/libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<jsp:include page="/menu.jsp"/>
<div class="container" style="margin-top: 40px;">
    <h1>Изменить издателя</h1>
    <%
        Object error = request.getAttribute("error");
        if (error != null) {
    %>
    <div class="message-box error">
        <% out.println(error); %>
    </div>
    <%
        }

        Object publAttr = request.getAttribute("publisher");
        if (Objects.equals(publAttr, null)) return;
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
        <input type="hidden" name="id" value="${publisher.id}"/>
        <div class="table">
            <div class="table-row">
                <div class="table-cell">
                    <b>Название издателя:</b>
                </div>
                <div class="table-cell">
                    <input type="text" name="title" style="width:300px;" value="${publisher.title}"/>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Адрес:</b>
                </div>
                <div class="table-cell">
                    <input type="text" name="address" style="width:300px;" value="${publisher.address}"/>
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
