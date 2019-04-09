package servlets;

import models.MessageType;
import models.Publisher;
import utils.DAOInstances;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static utils.MessageSender.sendMessage;
import static utils.Validators.validatePublisherData;

@WebServlet("/add/publisher")
public class AddPublisherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dispatchAddPublisher(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String title = request.getParameter("title");
        String address = request.getParameter("address");

        Publisher publisher = new Publisher();
        String validatingMessage = validatePublisherData(publisher, title, address);
        if (!validatingMessage.isEmpty()) {
            sendMessage(request, MessageType.ERROR, validatingMessage);
        } else if (DAOInstances.getPublisherDAO().save(publisher)) {
            sendMessage(request, MessageType.INFORMATION, "Издатель добавлен.");
        } else {
            sendMessage(request, MessageType.ERROR, "Произошла ошибка добавления. Возможно, такой издатель уже существует в системе.");
        }

        dispatchAddPublisher(request, response);
    }

    private void dispatchAddPublisher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String publisherTitle = request.getParameter("title");
        String publisherAddress = request.getParameter("address");
        if (publisherTitle != null) {
            request.setAttribute("publisherTitle", publisherTitle);
            request.setAttribute("publisherAddress", publisherAddress);
        }

        request.getRequestDispatcher("/pages/modify/add/addPublisher.jsp")
                .forward(request, response);
    }
}