package servlets;

import models.MessageType;
import models.Publisher;
import utils.DAOInstances;
import utils.ParameterHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static utils.MessageSender.sendMessage;

@WebServlet("/delete/publisher")
public class DeletePublisherServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Publisher publisher = DAOInstances.getPublisherDAO().getById(
                ParameterHandler.tryParseInteger(request.getParameter("id"))
        );
        if (publisher == null) {
            sendMessage(request, MessageType.ERROR, "Издатель с таким id не существует.");
        } else if (DAOInstances.getPublisherDAO().delete(publisher)) {
            sendMessage(request, MessageType.INFORMATION, "Издатель успешно удалена.");
        } else {
            sendMessage(request, MessageType.ERROR, "Ошибка при удалении издателя.");
        }

        request.getRequestDispatcher("/pages/modify/delete/deletePublisher.jsp")
                .forward(request, response);
    }
}
