package servlets;

import models.*;
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

@WebServlet("/add/book")
public class AddBookServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dispatchAddBook(request, response, null, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Map<String, String> fields = null;
        List<Genre> genres = null;
        try {
            ServletFileUpload upload = getServletFileUpload();
            List<FileItem> formItems = upload.parseRequest(request);
            genres = new ArrayList<>();
            fields = getBookParams(formItems, genres);
            if (fields != null && !fields.isEmpty()) {
                Book book = new Book();
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
                } else if (DAOInstances.getBookDAO().saveWithGenres(book, genres)) {
                    sendMessage(request, MessageType.INFORMATION, "Книга добавлена.");
                } else {
                    sendMessage(request, MessageType.ERROR, "Произошла ошибка при добавлении книги.");
                }
            } else {
                sendMessage(request, MessageType.ERROR, "Параметры не переданы.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(request, MessageType.ERROR, "При обработке запроса произошла ошибка: " + ex.getMessage());
        }

        dispatchAddBook(request, response, fields, genres);
    }

    private void dispatchAddBook(HttpServletRequest request, HttpServletResponse response,
                                 Map<String, String> fields,
                                 List<Genre> genres) throws ServletException, IOException {
        if (fields != null && genres != null) {
            request.setAttribute("title", fields.get("title"));
            request.setAttribute("createdYear", fields.get("createdYear"));
            request.setAttribute("publishedYear", fields.get("publishedYear"));
            request.setAttribute("authorId", fields.get("authorId"));
            request.setAttribute("publisherId", fields.get("publisherId"));
            request.setAttribute("description", fields.get("description"));
            request.setAttribute("selectedGenres", genres);
        }

        request.getRequestDispatcher("/pages/modify/add/addBook.jsp")
                .forward(request, response);
    }
}
