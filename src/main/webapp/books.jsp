<%@ page import="models.Book" %>
<%@ page import="models.Genre" %>
<%@ page import="utils.DAOInstances" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Список книг</title>
    <%@include file="libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<jsp:include page="menu.jsp"/>
<c:set var="pattern" value="dd.MM.yyyy"/>
<div class="container" style="margin-top: 25px">
    <%
        String path = request.getRequestURI();
        if (Objects.equals(path, "/") || path.isEmpty()) {
            response.sendRedirect("/books");
        }
    %>
    <div>
        <input type="button" class="btn btn-dark" style="font-size:14pt;" value="Добавить книгу"
               onclick="document.location.href = '/add/book'"/>
        <c:forEach items="${booksList}" var="book">
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
                            <c:out value="-"/>

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
</div>
</body>
</html>
