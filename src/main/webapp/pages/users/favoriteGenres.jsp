<%@ page import="models.Genre" %>
<%@ page import="utils.DAOInstances" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: JayLim
  Date: 11.03.2019
  Time: 21:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Добавить издателя</title>
    <%@include file="/libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<jsp:include page="/menu.jsp"/>
<div class="container" style="margin-top: 40px;">
    <h1>
        Избранные жанры

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

    <form action="" method="post">
        <div class="table">
            <div class="table-row">
                <div class="table-cell" style="width:300px">
                    <b>Выберите один или несколько жанров:</b>
                </div>
                <div class="table-cell">
                    <select name="genresList" multiple style="width:300px;min-height: 250px">
                        <%
                            List<Genre> currentUserFavoriteGenres = (List<Genre>) request.getAttribute("favGenres");
                            List<Genre> genres = DAOInstances.getGenreDAO().getAll();
                            for (Genre genre : genres) {
                        %>
                        <option value="<%= genre.getId() %>"
                                <%
                                    if (currentUserFavoriteGenres.contains(genre)) {
                                        out.print("selected");
                                    }
                                %>
                        >
                            <%= genre.getName() %>
                        </option>
                        <%
                            }
                        %>
                    </select>
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
