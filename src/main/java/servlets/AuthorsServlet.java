package servlets;

import models.Author;
import models.Book;
import utils.ImageHashUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static utils.DAOInstances.getAuthorDAO;
import static utils.DAOInstances.getBookDAO;

@WebServlet(name = "authors", urlPatterns = {"/authors"})
public class AuthorsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse) throws ServletException, IOException {

        List<Author> authors = getAuthorDAO().getAll();
        authors.forEach(author -> {
            if (author.getImageHash() == null) {
                String s = ImageHashUtil.getDefaultAuthor();
                author.setImageHash(s);
            }

            if (author.getBooks() == null || author.getBooks().isEmpty()) {
                List<Book> books = getBookDAO().getByAuthor(author);
                author.setBooks(books);
            }
        });

        httpServletRequest.setAttribute("authorsList", authors);

        httpServletRequest
                .getRequestDispatcher("/authors.jsp")
                .forward(httpServletRequest, httpServletResponse);

    }
}
