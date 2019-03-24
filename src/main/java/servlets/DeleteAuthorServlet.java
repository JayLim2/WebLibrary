package servlets;

import models.Author;
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

@WebServlet("/delete/author")
public class DeleteAuthorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Author author = DAOInstances.getAuthorDAO().getById(
                ParameterHandler.tryParseInteger(request.getParameter("id"))
        );
        if (author == null) {
            sendMessage(request, MessageType.ERROR, "Автор с таким id не существует.");
        } else if (DAOInstances.getAuthorDAO().delete(author)) {
            sendMessage(request, MessageType.INFORMATION, "Автор успешно удален.");
        } else {
            sendMessage(request, MessageType.ERROR, "Ошибка при удалении автора.");
        }

        request.getRequestDispatcher("/pages/modify/delete/deleteAuthor.jsp")
                .forward(request, response);
    }
}
