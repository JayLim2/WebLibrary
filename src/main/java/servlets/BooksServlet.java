package servlets;

import models.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static utils.DAOInstances.getBookDAO;

@WebServlet(name = "books", urlPatterns = {"/books"})
public class BooksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        List<Book> books = getBookDAO().getAll();
        request.setAttribute("booksList", books);

        request.getRequestDispatcher("/books.jsp")
                .forward(request, response);
    }
}