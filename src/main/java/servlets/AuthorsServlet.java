package servlets;

import models.Author;
import services.AuthorsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "authors", urlPatterns = {"/authors"})
public class AuthorsServlet extends HttpServlet {
    private AuthorsService authorsService;

    public AuthorsServlet() {
        this.authorsService = new AuthorsService();
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse) throws ServletException, IOException {

        List<Author> authors = authorsService.getAll();

        httpServletRequest.setAttribute("authorsList", authors);

        httpServletRequest
                .getRequestDispatcher("/authors.jsp")
                .forward(httpServletRequest, httpServletResponse);

    }
}
