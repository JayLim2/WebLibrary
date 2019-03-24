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
import java.util.Optional;

import static utils.ParameterHandler.tryParseInteger;

@WebServlet("/updateBookRating")
public class UpdateBookRatingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object userObject = request.getSession().getAttribute("user");
        if (userObject instanceof User) {
            User user = (User) userObject;
            String bookIdParam = Optional.ofNullable(request.getParameter("bookId")).orElse("");
            Book book = DAOInstances.getBookDAO().getById(tryParseInteger(bookIdParam));
            int value = tryParseInteger(Optional.ofNullable(request.getParameter("value")).orElse(""));
            if (book != null && value != -1) {
                BookRating rating = new BookRating();
                rating.setBook(book);
                rating.setUser(user);
                rating.setValue(value);
                DAOInstances.getBookRatingDAO().saveOrUpdate(rating);
            }
        }
    }
}
