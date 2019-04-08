<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Список жанров</title>
    <%@include file="libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<%@include file="menu.jsp" %>
<div class="container" style="margin-top: 25px">
    <input type="button" class="btn btn-dark"
           style="font-size:14pt;"
           value="Добавить жанр"
           onclick="document.location.href = '/add/genre'"/>
    <table class="table" style="margin-top:20px;border:1px solid black;">
        <thead class="thead-dark">
        <tr>
            <%--<th scope="col">
                ID
            </th>--%>
            <th scope="col" style="width:250px;">
                Название жанра
            </th>
            <th scope="col" style="width:10px">

            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${genresList}" var="genre">
            <tr>
                <td style="width:250px;">
                        ${genre.name}
                </td>
                <td style="width: 10px;">
                    <div style="margin-bottom:10px;">
                        <input type="button"
                               class="btn btn-primary"
                               style="font-size:14pt;"
                               value="&#128393;"
                               onclick="document.location.href = '/edit/genre?id=${genre.id}'"/>
                        <input type="button"
                               class="btn btn-danger"
                               style="font-size:14pt;"
                               value="&#128939;"
                               onclick="document.location.href = '/delete/genre?id=${genre.id}'"/>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
