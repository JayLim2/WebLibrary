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
import java.util.List;
import java.util.Map;

import static utils.Files.getServletFileUpload;
import static utils.MessageSender.sendMessage;
import static utils.ParameterHandler.getAuthorParams;
import static utils.Validators.validateAuthorData;

@WebServlet("/add/author")
public class AddAuthorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dispatchAddAuthor(request, response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Map<String, String> fields = null;
        try {
            ServletFileUpload upload = getServletFileUpload();
            List<FileItem> formItems = upload.parseRequest(request);
            fields = getAuthorParams(formItems);
            if (fields != null && !fields.isEmpty()) {
                Author author = new Author();
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
                } else if (DAOInstances.getAuthorDAO().save(author)) {
                    sendMessage(request, MessageType.INFORMATION, "Автор добавлен.");
                } else {
                    sendMessage(request, MessageType.ERROR, "Произошла ошибка добавления автора. Возможно, такой автор уже существует в системе.");
                }
            } else {
                sendMessage(request, MessageType.ERROR, "Параметры не переданы.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(request, MessageType.ERROR, "При обработке запроса произошла ошибка: " + ex.getMessage());
        }

        dispatchAddAuthor(request, response, fields);
    }

    private void dispatchAddAuthor(HttpServletRequest request, HttpServletResponse response,
                                   Map<String, String> fields) throws ServletException, IOException {
        if (fields != null) {
            request.setAttribute("authorName", fields.get("authorName"));
            request.setAttribute("birthDate", fields.get("birthDate"));
            request.setAttribute("deathDate", fields.get("deathDate"));
            request.setAttribute("description", fields.get("description"));
        }

        request.getRequestDispatcher("/pages/modify/add/addAuthor.jsp")
                .forward(request, response);
    }
}
