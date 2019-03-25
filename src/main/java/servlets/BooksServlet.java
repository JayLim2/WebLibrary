package servlets;

import models.Book;
import models.BookRating;
import models.User;
import utils.DAOInstances;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.DAOInstances.getBookDAO;

@WebServlet(name = "books", urlPatterns = {"/books"})
public class BooksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        List<Book> books = getBookDAO().getAll();
        request.setAttribute("booksList", books);

        Object userObject = request.getSession().getAttribute("user");
        Map<Integer, BookRating> booksRatingsMap = new HashMap<>();
        if (userObject instanceof User) {
            User user = (User) userObject;
            for (Book book : books) {
                booksRatingsMap.put(
                        book.getId(),
                        DAOInstances.getBookRatingDAO().getByUserAndBook(user, book)
                );
            }
        }
        request.setAttribute("booksRatings", booksRatingsMap);

        request.getRequestDispatcher("/books.jsp")
                .forward(request, response);
    }
}