package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "error", urlPatterns = {"/error"}, loadOnStartup = 1)
public class ErrorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse) throws ServletException, IOException {

        httpServletRequest
                .getRequestDispatcher("/errorPage.jsp")
                .forward(httpServletRequest, httpServletResponse);

        httpServletResponse.getWriter().println("Hello from servlet");
    }
}
