<%@ page import="models.Book" %>
<%@ page import="models.BookRating" %>
<%@ page import="models.Genre" %>
<%@ page import="utils.DAOInstances" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
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
        <input type="button"
               class="btn btn-dark"
               style="font-size:14pt;"
               value="Добавить книгу"
               onclick="document.location.href = '/add/book'"/>

        <%
            boolean isAuthenticated = session.getAttribute("user") != null;
            Map<Integer, BookRating> ratingsMap = (Map<Integer, BookRating>) request.getAttribute("booksRatings");
        %>


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
                            <div style="margin-bottom:10px;">
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
                            </div>

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
                            <!-- stars -->

                            <b>Оценка:</b>

                            <%
                                Book book = (Book) pageContext.getAttribute("book");
                                BookRating currentBookRating = ratingsMap.get(book.getId());
                                int ratingValue = currentBookRating != null ? currentBookRating.getValue() : 0;
                                float totalRatingValue = DAOInstances.getBookRatingDAO().getByBook(book);
                            %>

                            <div style="display:table;margin-top: 10px;">
                                <div style="display:table-row;">
                                    <div style="display:table-cell;width:110px;vertical-align: top; padding-right:10px;">
                                        <%
                                            if (isAuthenticated) {
                                        %>
                                        <div class="review_stars_wrap">
                                            <div id="review_stars">
                                                <%
                                                    for (int i = 5; i > 0; i--) {
                                                %>
                                                <input id="star-<%=i-1%>-${book.id}" type="radio"
                                                       name="stars-${book.id}"
                                                       onclick="updateRating(${book.id}, <%=i%>)"
                                                       <% if(ratingValue == i) { %>checked<% } %>
                                                />
                                                <label for="star-<%=i-1%>-${book.id}">
                                                    <i class="fas fa-star"></i>
                                                </label>
                                                <%
                                                    }
                                                %>
                                            </div>
                                        </div>
                                        <%
                                        } else {
                                        %>
                                        <span style="font-size:10pt">
                                                <a target="_blank" href="${pageContext.request.contextPath}/login">Войдите</a>, чтобы оценивать
                                            </span>
                                        <%
                                            }
                                        %>


                                    </div>
                                    <div style="display:table-cell;vertical-align: top;padding-right:10px;">
                                        <b><%= totalRatingValue %>
                                        </b>
                                    </div>
                                </div>
                            </div>

                            <!-- /stars -->

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
