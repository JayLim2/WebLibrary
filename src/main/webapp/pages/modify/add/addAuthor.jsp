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
    <title>Add Item</title>
    <%@include file="/libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<jsp:include page="/menu.jsp"/>
<div class="container" style="margin-top: 40px;">
    <h1>Добавить автора</h1>

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

    <form action="" method="post">
        <div class="table">
            <div class="table-row">
                <div class="table-cell">
                    <b>Имя автора:</b>
                </div>
                <div class="table-cell">
                    <input type="text" name="authorName" value="test автор"/>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Дата рождения:</b>
                </div>
                <div class="table-cell">
                    <input type="text" name="birthDate" value="test автор"/>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Дата смерти:</b>
                </div>
                <div class="table-cell">
                    <input type="text" name="deathDate" value="test автор"/>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Об авторе:</b>
                </div>
                <div class="table-cell">
                    <textarea name="description">some description</textarea>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                </div>
                <div class="table-cell">
                    <input type="submit" class="btn btn-dark" value="Добавить автора">
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>
