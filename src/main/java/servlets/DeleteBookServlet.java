package servlets;

import models.Book;
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

@WebServlet("/delete/book")
public class DeleteBookServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Book book = DAOInstances.getBookDAO().getById(
                ParameterHandler.tryParseInteger(request.getParameter("id"))
        );
        if (book == null) {
            sendMessage(request, MessageType.ERROR, "Книга с таким id не существует.");
        } else if (DAOInstances.getBookDAO().delete(book)) {
            sendMessage(request, MessageType.INFORMATION, "Книга успешно удалена.");
        } else {
            sendMessage(request, MessageType.ERROR, "Ошибка при удалении книги.");
        }

        request.getRequestDispatcher("/pages/modify/delete/deleteBook.jsp")
                .forward(request, response);
    }
}
