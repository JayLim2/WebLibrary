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
import static utils.Validators.validateGenreData;

@WebServlet("/edit/genre")
public class EditGenreServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dispatchEditPublisher(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");

        Genre genre = DAOInstances.getGenreDAO().getById(
                ParameterHandler.tryParseInteger(request.getParameter("id"))
        );

        if (genre != null) {
            String validatingMessage = validateGenreData(genre, name);
            if (!validatingMessage.isEmpty()) {
                sendMessage(request, MessageType.ERROR, validatingMessage);
            } else {
                if (DAOInstances.getGenreDAO().update(genre)) {
                    sendMessage(request, MessageType.INFORMATION, "Жанр изменен.");
                } else {
                    sendMessage(request, MessageType.ERROR, "Ошибка при изменении жанра.");
                }
            }
        } else {
            sendMessage(request, MessageType.ERROR, "Жанр с таким id не существует.");
        }

        dispatchEditPublisher(request, response);
    }

    private void dispatchEditPublisher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Genre genre = DAOInstances.getGenreDAO().getById(
                ParameterHandler.tryParseInteger(request.getParameter("id"))
        );
        if (genre == null) {
            sendMessage(request, MessageType.ERROR, "Жанр с таким id не существует.");
        }
        request.setAttribute("genre", genre);
        request.getRequestDispatcher("/pages/modify/edit/editGenre.jsp")
                .forward(request, response);
    }
}
