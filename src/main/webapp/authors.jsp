<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Список авторов</title>
    <%@include file="libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<jsp:include page="menu.jsp"/>
<c:set var="pattern" value="dd.MM.yyyy"/>
<div class="container" style="margin-top: 25px">
    <div>
        <c:forEach items="${authorsList}" var="author">
            <div class="data-block">
                <span class="data-block-header">
                    <a href="#">
                            ${author.name}
                    </a>
                </span>
                <div class="table">
                    <div class="table-row">
                        <div class="table-cell poster-cell">
                            <img class="poster-img" src="data:image/jpg;base64,<c:out value='${author.imageHash}'/>"/>
                        </div>
                        <div class="table-cell">
                            <b>Дата рождения:</b>
                            <fmt:parseDate value="${author.birthDate}" pattern="yyyy-MM-dd" var="parsedDate"
                                           type="date"/>
                            <fmt:formatDate value="${parsedDate}" type="date" pattern="${pattern}"/>
                            <c:if test="${author.deathDate != null}">
                                <div class="table-cell">
                                    <b>Дата смерти:</b>
                                    <fmt:parseDate value="${author.deathDate}" pattern="yyyy-MM-dd" var="parsedDate"
                                                   type="date"/>
                                    <fmt:formatDate value="${parsedDate}" type="date" pattern="${pattern}"/>
                                </div>
                            </c:if>
                            <div style="margin-top: 20px">
                                <h4>Об авторе</h4>
                                    ${author.description}
                            </div>
                            <div style="margin-top: 20px">
                                <h4>Библиография</h4>
                                <c:choose>
                                    <c:when test="${author.books.size() > 0}">
                                        <ul>
                                            <c:forEach items="${author.books}" var="book">
                                                <li>${book.title}</li>
                                            </c:forEach>
                                        </ul>
                                    </c:when>
                                    <c:otherwise>
                                        У нас еще нет книг этого автора.
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
