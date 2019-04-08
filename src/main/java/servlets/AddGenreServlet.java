package servlets;

import models.Genre;
import models.MessageType;
import utils.DAOInstances;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static utils.MessageSender.sendMessage;
import static utils.Validators.validateGenreData;

@WebServlet("/add/genre")
public class AddGenreServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dispatchAddPublisher(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String title = request.getParameter("title");

        Genre genre = new Genre();
        String validatingMessage = validateGenreData(genre, title);
        if (!validatingMessage.isEmpty()) {
            sendMessage(request, MessageType.ERROR, validatingMessage);
        } else {
            DAOInstances.getGenreDAO().save(genre);
            sendMessage(request, MessageType.INFORMATION, "Жанр добавлен.");
        }

        dispatchAddPublisher(request, response);
    }

    private void dispatchAddPublisher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/pages/modify/add/addGenre.jsp")
                .forward(request, response);
    }
}