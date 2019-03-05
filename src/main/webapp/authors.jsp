<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hello world</title>
    <%@include file="libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<jsp:include page="menu.jsp"/>
<div class="container" style="margin-top: 25px">
    ${authorsList}
    <%--<table>
        <c:forEach items="authorsList" var="author">
            <tr>
                <td><c:out value="${author.id}" /></td>
                <td><c:out value="${author.name}" /></td>
                <td><c:out value="${author.birthDate}" /></td>
                <td><c:out value="${author.deathDate}" /></td>
                <td><c:out value="${author.description}" /></td>
            </tr>
        </c:forEach>
    </table>--%>
</div>
</body>
</html>
