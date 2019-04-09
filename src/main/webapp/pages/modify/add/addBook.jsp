<%@ page import="models.Author" %>
<%@ page import="models.Genre" %>
<%@ page import="models.Publisher" %>
<%@ page import="utils.DAOInstances" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>
<%@ page import="utils.ParameterHandler" %>
<%@ page import="java.util.Optional" %>
<%@ page import="java.util.ArrayList" %>
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
    <title>Добавить книгу</title>
    <%@include file="/libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<jsp:include page="/menu.jsp"/>
<div class="container" style="margin-top: 40px;">
    <h1>Добавить книгу</h1>
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

    <form action="" method="post" enctype="multipart/form-data">
        <div class="table">
            <div class="table-row">
                <div class="table-cell">
                    <b>Название книги:</b>
                </div>
                <div class="table-cell">
                    <input type="text" name="title" style="width:300px;" value="${title}"/>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Год написания:</b>
                </div>
                <div class="table-cell">
                    <input type="text" name="createdYear" style="width:300px;" value="${createdYear}"/>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Год публикации:</b>
                </div>
                <div class="table-cell">
                    <input type="text" name="publishedYear" style="width:300px;" value="${publishedYear}"/>
                </div>
            </div>

            <div class="table-row">
                <div class="table-cell">
                    <b>Автор:</b>
                </div>
                <div class="table-cell">
                    <select name="authorId" style="width:200px">
                        <%
                            List<Author> authors = DAOInstances.getAuthorDAO().getAll();
                            Object selectedAuthorIdAttribute = request.getAttribute("authorId");
                            int selectedAuthorId = selectedAuthorIdAttribute != null ? ParameterHandler.tryParseInteger(selectedAuthorIdAttribute.toString()) : -1;
                            for (Author author : authors) {
                                boolean selected = Objects.equals(selectedAuthorId, author.getId());
                        %>
                        <option value="<%= author.getId() %>"
                                <% if(selected) { %>selected<% } %>><%= author.getName() %>
                        </option>
                        <%
                            }
                        %>
                    </select>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Издатель:</b>
                </div>
                <div class="table-cell">
                    <select name="publisherId" style="width:200px">
                        <%
                            List<Publisher> publishers = DAOInstances.getPublisherDAO().getAll();
                            Object selectedPublisherIdAttribute = request.getAttribute("publisherId");
                            int selectedPublisherId = selectedPublisherIdAttribute != null ? ParameterHandler.tryParseInteger(selectedPublisherIdAttribute.toString()) : -1;
                            for (Publisher publisher : publishers) {
                                boolean selected = Objects.equals(selectedPublisherId, publisher.getId());
                        %>
                        <option value="<%= publisher.getId() %>"
                                <% if(selected) { %>selected<% } %>><%= publisher.getTitle() %>
                        </option>
                        <%
                            }
                        %>
                    </select>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Жанры:</b>
                </div>
                <div class="table-cell">
                    <select name="genresList" multiple style="width:200px">
                        <%
                            List<Genre> genres = DAOInstances.getGenreDAO().getAll();
                            Object selectedGenresAttribute = request.getAttribute("selectedGenres");
                            List<Genre> selectedGenres = selectedGenresAttribute != null ? (List<Genre>) selectedGenresAttribute : new ArrayList<Genre>();

                            for (Genre genre : genres) {
                                boolean selected = selectedGenres.contains(genre);
                        %>
                        <option value="<%= genre.getId() %>" <% if(selected) { %>selected<% } %>><%= genre.getName() %>
                        </option>
                        <%
                            }
                        %>
                    </select>
                </div>
            </div>

            <div class="table-row">
                <div class="table-cell">
                    <b>Описание книги:</b>
                </div>
                <div class="table-cell">
                    <textarea name="description">${description}</textarea>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Постер:</b>
                </div>
                <div class="table-cell">
                    <input type="file" id="poster" name="poster"/>

                    <div style="margin-top:10px;font-size:10pt;font-style:italic;">
                        Если вы оставите это поле пустым, будет загружен постер по умолчанию.
                    </div>
                </div>
            </div>

            <div class="table-row">
                <div class="table-cell">
                </div>
                <div class="table-cell">
                    <br/>
                    <input type="submit" class="btn btn-dark" value="Добавить книгу">
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>
