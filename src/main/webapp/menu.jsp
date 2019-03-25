<%@ page import="models.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a class="navbar-brand" href="/">WebLibrary</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
            aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/books">Книги</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/authors">Авторы</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/publishers">Издатели</a>
            </li>
        </ul>
        <div class="dropdown">
            <%
                Object userObject = session.getAttribute("user");
                User user;
                boolean isValidAuth = userObject instanceof User;
                if (isValidAuth) {
                    user = (User) userObject;
            %>
            <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton"
                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <%= user.getLogin() %>
            </button>
            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton">
                <a class="dropdown-item" href="${pageContext.request.contextPath}/user/profile">Мой профиль</a>
                <a class="dropdown-item" href="${pageContext.request.contextPath}/user/recommendations">Мои
                    рекомендации</a>
                <a class="dropdown-item" href="${pageContext.request.contextPath}/user/favoriteGenres">Мои избранные
                    жанры</a>
                <a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Выйти</a>
            </div>
            <%
            } else {
            %>
            <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton"
                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Гость
            </button>
            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton">
                <a class="dropdown-item" href="${pageContext.request.contextPath}/login">Войти</a>
            </div>
            <%
                }
            %>
        </div>
    </div>
</nav>