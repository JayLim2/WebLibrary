<%@ page import="models.Book" %>
<%@ page import="models.Genre" %>
<%@ page import="utils.DAOInstances" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>Мои рекомендации</title>
    <%@include file="/libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<jsp:include page="/menu.jsp"/>
<div class="container" style="margin-top: 40px;">
    <h1>
        Мои рекомендации

        <a class="btn btn-dark" href="${pageContext.request.contextPath}/user/profile">
            Вернуться в профиль
        </a>
    </h1>
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

    <c:if test="${size == 0}">
        К сожалению, рекомендаций для вас нет.
        <br/><br/>
        Выберите хотя бы один <a href="${pageContext.request.contextPath}/user/favoriteGenres">избранный
        жанр</a> и повторите попытку.
    </c:if>
    <c:forEach items="${recommendations}" var="book">
        <div class="data-block">
                <span class="data-block-header">
                    <a href="#">
                            ${book.title}
                    </a>
                </span>
            <div class="table">
                <div class="table-row">
                    <div class="table-cell poster-cell">
                        <img class="poster-img" src="data:image/jpg;base64,<c:out value='${book.imageHash}'/>"/>
                    </div>
                    <div class="table-cell">
                            <%--<div style="margin-bottom:10px;">
                                <input type="button"
                                       class="btn btn-primary"
                                       style="font-size:14pt;"
                                       value="Изменить книгу"
                                       onclick="document.location.href = '/edit/book?id=${book.id}'"/>
                                <input type="button"
                                       class="btn btn-danger"
                                       style="font-size:14pt;"
                                       value="Удалить книгу"
                                       onclick="document.location.href = '/delete/book?id=${book.id}'"/>
                            </div>--%>

                        <b>Год написания:</b>
                        <c:out value="${book.createdYear}"/>
                        <br/>
                        <b>Год издания:</b>
                        <c:out value="${book.publishedYear}"/>
                        <br/>
                        <b>Автор:</b>
                        <c:out value="${book.author.name}"/>
                        <br/>
                        <b>Издатель:</b>
                        <c:out value="${book.publisher.title}"/>
                        <br/>
                        <b>Жанры:</b>
                        <%
                            List<String> genres = new ArrayList<String>();
                            List<Genre> genreList = DAOInstances.getBookDAO()
                                    .getBookGenres((Book) pageContext.getAttribute("book"));
                            for (Genre genre : genreList) {
                                genres.add(genre.getName());
                            }
                            out.println(String.join(", ", genres));
                        %>
                        <br/>

                        <b>Оценка:</b>
                        <%
                            Book book = (Book) pageContext.getAttribute("book");
                            float totalRatingValue = DAOInstances.getBookRatingDAO().getByBook(book);
                            String totalRatingColor;
                            if (Float.compare(totalRatingValue, 2) < 0) {
                                totalRatingColor = "red";
                            } else if (Float.compare(totalRatingValue, 3.5f) < 0) {
                                totalRatingColor = "gray";
                            } else {
                                totalRatingColor = "green";
                            }
                            pageContext.setAttribute("color", totalRatingColor);
                        %>
                        <b style="color:${color}"><%= totalRatingValue %>
                        </b>

                        <div style="margin-top: 20px">
                            <h4>Описание книги</h4>
                                ${book.description}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</div>
</body>
</html>
