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
import static utils.Validators.validatePublisherData;

@WebServlet("/edit/publisher")
public class EditPublisherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dispatchEditPublisher(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String address = request.getParameter("address");

        Publisher publisher = DAOInstances.getPublisherDAO().getById(
                ParameterHandler.tryParseInteger(request.getParameter("id"))
        );

        if (publisher != null) {
            String validatingMessage = validatePublisherData(publisher, title, address);
            if (!validatingMessage.isEmpty()) {
                sendMessage(request, MessageType.ERROR, validatingMessage);
            } else {
                if (DAOInstances.getPublisherDAO().update(publisher)) {
                    sendMessage(request, MessageType.INFORMATION, "Издатель изменен.");
                } else {
                    sendMessage(request, MessageType.ERROR, "Ошибка при изменении издателя.");
                }
            }
        } else {
            sendMessage(request, MessageType.ERROR, "Издателя с таким id не существует.");
        }

        dispatchEditPublisher(request, response);
    }

    private void dispatchEditPublisher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Publisher publisher = DAOInstances.getPublisherDAO().getById(
                ParameterHandler.tryParseInteger(request.getParameter("id"))
        );
        if (publisher == null) {
            sendMessage(request, MessageType.ERROR, "Издателя с таким id не существует.");
        }
        request.setAttribute("publisher", publisher);
        request.getRequestDispatcher("/pages/modify/edit/editPublisher.jsp")
                .forward(request, response);
    }
}
