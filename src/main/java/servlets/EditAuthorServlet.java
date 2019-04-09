package servlets;

import models.Author;
import models.MessageType;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import utils.DAOInstances;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static utils.Files.getServletFileUpload;
import static utils.MessageSender.sendMessage;
import static utils.ParameterHandler.getAuthorParams;
import static utils.ParameterHandler.tryParseInteger;
import static utils.Validators.validateAuthorData;

@WebServlet("/edit/author")
public class EditAuthorServlet extends HttpServlet {
    private static final String ENCODING = "UTF-8";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dispatchEditAuthor(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            ServletFileUpload upload = getServletFileUpload();
            List<FileItem> formItems = upload.parseRequest(request);
            Map<String, String> fields = getAuthorParams(formItems);
            if (fields != null && !fields.isEmpty()) {
                Author author = DAOInstances.getAuthorDAO().getById(
                        tryParseInteger(fields.get("id"))
                );
                if (author != null) {
                    String validatingMessage = validateAuthorData(
                            author,
                            fields.get("authorName"),
                            fields.get("birthDate"),
                            fields.get("deathDate"),
                            fields.get("imageHash"),
                            fields.get("description")
                    );
                    if (!validatingMessage.isEmpty()) {
                        sendMessage(request, MessageType.ERROR, validatingMessage);
                    } else {
                        DAOInstances.getAuthorDAO().update(author);
                        sendMessage(request, MessageType.INFORMATION, "Автор изменен.");
                    }
                } else {
                    sendMessage(request, MessageType.ERROR, "Автор с таким id не существует.");
                }
            } else {
                sendMessage(request, MessageType.ERROR, "Параметры не переданы.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(request, MessageType.ERROR, "При обработке запроса произошла ошибка: " + ex.getMessage());
        }

        dispatchEditAuthor(request, response);
    }

    private void dispatchEditAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Author author = DAOInstances.getAuthorDAO().getById(
                tryParseInteger(request.getParameter("id"))
        );
        if (author == null) {
            sendMessage(request, MessageType.ERROR, "Автора с таким id не существует.");
        }
        request.setAttribute("author", author);
        request.getRequestDispatcher("/pages/modify/edit/editAuthor.jsp")
                .forward(request, response);
    }
}
