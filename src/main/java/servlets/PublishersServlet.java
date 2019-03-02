package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "publishers", urlPatterns = {"/publishers"})
public class PublishersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse) throws ServletException, IOException {

        httpServletRequest
                .getRequestDispatcher("/publishers.jsp")
                .forward(httpServletRequest, httpServletResponse);

        httpServletResponse.getWriter().println("Hello from servlet");
    }
}