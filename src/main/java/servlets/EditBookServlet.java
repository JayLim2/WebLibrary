package servlets;

import models.Book;
import models.Genre;
import models.MessageType;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import utils.DAOInstances;
import utils.ParameterHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.Files.getServletFileUpload;
import static utils.MessageSender.sendMessage;
import static utils.ParameterHandler.getBookParams;
import static utils.Validators.validateBookData;

@WebServlet("/edit/book")
public class EditBookServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dispatchEditBook(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            ServletFileUpload upload = getServletFileUpload();
            List<Genre> genres = new ArrayList<>();
            List<FileItem> formItems = upload.parseRequest(request);
            Map<String, String> fields = getBookParams(formItems, genres);
            if (fields != null && !fields.isEmpty()) {
                Book book = DAOInstances.getBookDAO().getById(
                        ParameterHandler.tryParseInteger(fields.get("id"))
                );
                if (book != null) {
                    String validatingMessage = validateBookData(
                            book,
                            fields.get("title"),
                            fields.get("createdYear"),
                            fields.get("publishedYear"),
                            fields.get("description"),
                            fields.get("imageHash"),
                            fields.get("publisherId"),
                            fields.get("authorId"),
                            genres
                    );
                    if (!validatingMessage.isEmpty()) {
                        sendMessage(request, MessageType.ERROR, validatingMessage);
                    } else {
                        if (DAOInstances.getBookDAO().updateWithGenres(book, genres)) {
                            sendMessage(request, MessageType.INFORMATION, "Книга изменена.");
                        } else {
                            sendMessage(request, MessageType.ERROR, "Ошибка при изменении книги.");
                        }
                    }
                } else {
                    sendMessage(request, MessageType.ERROR, "Книги с таким id не существует.");
                }
            } else {
                sendMessage(request, MessageType.ERROR, "Параметры не переданы.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(request, MessageType.ERROR, "При обработке запроса произошла ошибка: " + ex.getMessage());
        }

        dispatchEditBook(request, response);
    }

    private void dispatchEditBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Book book = DAOInstances.getBookDAO().getById(
                ParameterHandler.tryParseInteger(request.getParameter("id"))
        );
        if (book == null) {
            sendMessage(request, MessageType.ERROR, "Книги с таким id не существует.");
        }
        request.setAttribute("book", book);
        request.getRequestDispatcher("/pages/modify/edit/editBook.jsp")
                .forward(request, response);
    }
}
