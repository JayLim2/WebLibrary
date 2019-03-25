<%@ page import="models.Author" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Objects" %>
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
    <title>Изменить автора</title>
    <%@include file="/libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<jsp:include page="/menu.jsp"/>
<div class="container" style="margin-top: 40px;">
    <h1>Изменить автора</h1>

    <%
        Object error = request.getAttribute("error");
        if (error != null) {
    %>
    <div class="message-box error">
        <% out.println(error); %>
    </div>
    <%
        }

        Object attr = request.getAttribute("author");
        if (Objects.equals(attr, null)) return;
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    %>

    <form action="" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${author.id}"/>
        <div class="table">
            <div class="table-row">
                <div class="table-cell">
                    <b>Имя автора:</b>
                </div>
                <div class="table-cell">
                    <input type="text" name="authorName" style="width:300px;" value="${author.name}"/>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Дата рождения:</b>
                </div>
                <div class="table-cell">
                    <%
                        LocalDate birthDate = ((Author) request.getAttribute("author")).getBirthDate();
                    %>
                    <input type="text" name="birthDate" class="form-control" readonly style="width:300px;"
                           value="<% if(birthDate != null) out.println(formatter.format(birthDate)); %>"
                           id="birthDate"/>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Дата смерти:</b>
                </div>
                <div class="table-cell">
                    <%
                        LocalDate deathDate = ((Author) request.getAttribute("author")).getDeathDate();
                    %>

                    <input type="text" name="deathDate" class="form-control" readonly style="width:300px;float:left"
                           value="<% if(deathDate != null) out.println(formatter.format(deathDate)); %>"
                           id="deathDate"/>

                    <input type="button"
                           class="btn btn-dark"
                           style="float:left;margin-left:10px"
                           value="Очистить"
                           onclick="$('#deathDate').val('')"
                    />
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Об авторе:</b>
                </div>
                <div class="table-cell">
                    <textarea name="description">${author.description}</textarea>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Постер:</b>
                </div>
                <div class="table-cell">
                    <input type="file" id="poster" name="poster"/>
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

    <script type="text/javascript">
        $(document).ready(function () {
            $('#birthDate').datepicker({
                format: 'dd.mm.yyyy'
            });
            $('#deathDate').datepicker({
                format: 'dd.mm.yyyy'
            });
        });
    </script>
</div>
</body>
</html>
