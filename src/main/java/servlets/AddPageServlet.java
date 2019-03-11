package servlets;

import models.Author;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import utils.ImageHashUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "add", urlPatterns = {"/add"})
public class AddPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String entityName = getEntityName(request);
        switch (entityName) {
            case "author":
                dispatchAddAuthor(request, response);
                break;
            case "book":
                dispatchAddBook(request, response);
                break;
            case "publisher":
                dispatchAddPublisher(request, response);
                break;
            default:
                response.sendError(404);
        }
    }

    private void dispatchAddAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(buildPageName("Author"))
                .forward(request, response);
    }

    private void dispatchAddBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(buildPageName("Book"))
                .forward(request, response);
    }

    private void dispatchAddPublisher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(buildPageName("Publisher"))
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String entityName = getEntityName(request);
        switch (entityName) {
            case "author":
                handleAddAuthorPostRequest(request, response);
                break;
            case "book":
                handleAddBookPostRequest(request, response);
                break;
            case "publisher":
                handleAddPublisherPostRequest(request, response);
                break;
            default:
                response.sendError(404);
        }
    }

    // location to store file uploaded
    private static final String UPLOAD_DIRECTORY = "upload";

    // upload settings
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    private static final String ENCODING = "UTF-8";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private void handleAddAuthorPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        try {
            String authorName = null;
            String birthDate = null;
            String deathDate = null;
            String description = null;
            String imageHash = null;
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null) {
                for (FileItem formItem : formItems) {
                    if (!formItem.isFormField()) {
                        if (formItem.getFieldName().equals("poster")) {
                            byte[] fileBytes = formItem.get();
                            imageHash = ImageHashUtil.encodeFromBytes(fileBytes);
                        }
                    } else {
                        String fieldName = formItem.getFieldName();

                        switch (fieldName) {
                            case "authorName":
                                authorName = formItem.getString(ENCODING);
                                break;
                            case "birthDate":
                                birthDate = formItem.getString(ENCODING);
                                break;
                            case "deathDate":
                                deathDate = formItem.getString(ENCODING);
                                break;
                            case "description":
                                description = formItem.getString(ENCODING);
                                break;
                        }
                    }
                }

                Author author = new Author();
                author.setName(authorName);
                if (birthDate != null && !birthDate.isEmpty())
                    author.setBirthDate(LocalDate.parse(birthDate, DATE_TIME_FORMATTER));
                if (deathDate != null && !deathDate.isEmpty())
                    author.setDeathDate(LocalDate.parse(deathDate, DATE_TIME_FORMATTER));
                author.setDescription(description);
                author.setImageHash(imageHash);

                String validatingMessage = validateAuthor(author);
                if (!validatingMessage.isEmpty()) {
                    sendMessage(request, MessageType.ERROR, validatingMessage);
                } else {
                    System.out.println(imageHash.length());
                    //DAOInstances.getAuthorDAO().save(author);
                }

                System.out.println(author);

                sendMessage(request, MessageType.INFORMATION, "Автор добавлен.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(request, MessageType.ERROR, "При обработке запроса произошла ошибка: " + ex.getMessage());
        }

        dispatchAddAuthor(request, response);
    }

    private void handleAddBookPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> params = request.getParameterMap();

        dispatchAddBook(request, response);
    }

    private void handleAddPublisherPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> params = request.getParameterMap();

        dispatchAddPublisher(request, response);
    }

    private static final int AUTHOR_NAME_MIN = 2;
    private static final int AUTHOR_NAME_MAX = 60;
    private static final int AUTHOR_DESCR_MAX = 5000;

    private String validateAuthor(Author author) {
        String message = "";
        String name = author.getName();
        String description = Optional.ofNullable(author.getDescription()).orElse("");
        if (name == null || name.isEmpty()) {
            message = "Имя не должно быть пустым.";
        } else if (name.length() < AUTHOR_NAME_MIN || name.length() > AUTHOR_NAME_MAX) {
            message = "Имя автора должно быть от "
                    + AUTHOR_NAME_MIN
                    + " до "
                    + AUTHOR_NAME_MAX
                    + " символов";
        } else if (author.getBirthDate() == null) {
            message = "Дата рождения автора не должна быть пустой.";
        } else if (description.length() > AUTHOR_DESCR_MAX) {
            message = "Описание не должно превышать " + AUTHOR_DESCR_MAX;
        }
        return message;
    }

    private void sendMessage(HttpServletRequest request, MessageType type, String message) {
        switch (type) {
            case ERROR:
                request.setAttribute("error", message);
                break;
            case INFORMATION:
                request.setAttribute("info", message);
                break;
            case WARNING:
                request.setAttribute("warn", message);
                break;
        }
    }

    enum MessageType {
        ERROR, INFORMATION, WARNING
    }

    //-------------- support methods

    private String buildPageName(String entityName) {
        return "/pages/modify/add/add" + entityName + ".jsp";
    }

    private String getEntityName(HttpServletRequest request) {
        return Optional.ofNullable(request.getParameter("entity")).orElse("")
                .toLowerCase();
    }
}
