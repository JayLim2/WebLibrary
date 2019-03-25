package servlets;

import models.Book;
import models.Genre;
import models.User;
import utils.DAOInstances;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/recommendations")
public class UserRecommendationsServlet extends HttpServlet {

    private static final int RECOMMENDATIONS_LIST_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object userObject = request.getSession().getAttribute("user");
        if (userObject instanceof User) {
            User user = (User) userObject;
            List<Genre> favoriteGenres = DAOInstances.getGenreDAO().getFavoriteGenresByUser(user);
            List<Book> books = DAOInstances.getBookDAO().getByGenres(favoriteGenres, RECOMMENDATIONS_LIST_SIZE, true);
            request.setAttribute("recommendations", books);
            request.setAttribute("size", books.size());

            request.getRequestDispatcher("/pages/users/recommendations.jsp")
                    .forward(request, response);
        } else {
            response.sendRedirect("/login");
        }
    }
}
