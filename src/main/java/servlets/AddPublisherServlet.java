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
        } else {
            DAOInstances.getPublisherDAO().save(publisher);
            sendMessage(request, MessageType.INFORMATION, "Издатель добавлен.");
        }

        dispatchAddPublisher(request, response);
    }

    private void dispatchAddPublisher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/pages/modify/add/addPublisher.jsp")
                .forward(request, response);
    }
}