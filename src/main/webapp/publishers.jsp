<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Список издателей</title>
    <%@include file="libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<%@include file="menu.jsp" %>
<div class="container" style="margin-top: 25px">
    <input type="button" class="btn btn-dark"
           style="font-size:14pt;"
           value="Добавить издателя"
           onclick="document.location.href = '/add/publisher'"/>
    <table class="table" style="margin-top:20px;border:1px solid black;">
        <thead class="thead-dark">
        <tr>
            <%--<th scope="col">
                ID
            </th>--%>
            <th scope="col" style="width:250px;">
                Название издателя
            </th>
            <th scope="col">
                Адрес издателя
            </th>
            <th scope="col">

            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${publishersList}" var="publisher">
            <tr>
                <%--<td>
                        ${publisher.id}
                </td>--%>
                <td style="width:250px;">
                        ${publisher.title}
                </td>
                <td>
                    <c:choose>
                        <c:when test="${publisher.address == null}">
                            Не указан
                        </c:when>
                        <c:otherwise>
                            ${publisher.address}
                        </c:otherwise>
                    </c:choose>
                </td>
                <td style="width: 130px;">
                    <div style="margin-bottom:10px;">
                        <input type="button"
                               class="btn btn-primary"
                               style="font-size:14pt;"
                               value="&#128393;"
                               onclick="document.location.href = '/edit/publisher?id=${publisher.id}'"/>
                        <input type="button"
                               class="btn btn-danger"
                               style="font-size:14pt;"
                               value="&#128939;"
                               onclick="document.location.href = '/delete/publisher?id=${publisher.id}'"/>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
