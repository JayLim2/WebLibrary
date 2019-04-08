package servlets;

import models.Genre;
import models.MessageType;
import utils.DAOInstances;
import utils.ParameterHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static utils.MessageSender.sendMessage;

@WebServlet("/delete/genre")
public class DeleteGenreServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Genre genre = DAOInstances.getGenreDAO().getById(
                ParameterHandler.tryParseInteger(request.getParameter("id"))
        );
        if (genre == null) {
            sendMessage(request, MessageType.ERROR, "Жанр с таким id не существует.");
        } else if (DAOInstances.getGenreDAO().delete(genre)) {
            sendMessage(request, MessageType.INFORMATION, "Жанр успешно удален.");
        } else {
            sendMessage(request, MessageType.ERROR, "Ошибка при удалении жанра.");
        }

        request.getRequestDispatcher("/pages/modify/delete/deleteGenre.jsp")
                .forward(request, response);
    }
}
