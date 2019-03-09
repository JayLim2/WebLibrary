package servlets;

import models.Publisher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static utils.DAOInstances.getPublisherDAO;

@WebServlet(name = "publishers", urlPatterns = {"/publishers"})
public class PublishersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Publisher> publishers = getPublisherDAO().getAll();
        request.setAttribute("publishersList", publishers);

        request.getRequestDispatcher("/publishers.jsp")
                .forward(request, response);
    }
}