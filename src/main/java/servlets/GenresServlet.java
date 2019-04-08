package servlets;

import models.Genre;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static utils.DAOInstances.getGenreDAO;

@WebServlet(name = "genres", urlPatterns = {"/genres"})
public class GenresServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Genre> genres = getGenreDAO().getAll();
        request.setAttribute("genresList", genres);

        request.getRequestDispatcher("/genres.jsp")
                .forward(request, response);
    }
}