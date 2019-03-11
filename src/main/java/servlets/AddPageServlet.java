package servlets;

import models.Author;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
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

    private void handleAddAuthorPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String authorName = request.getParameter("authorName");
        String birthDate = request.getParameter("birthDate");
        String deathDate = request.getParameter("deathDate");
        String description = request.getParameter("description");

        Author author = new Author();
        author.setName(authorName);
        author.setBirthDate(LocalDate.parse(birthDate));
        author.setDeathDate(LocalDate.parse(deathDate));
        author.setDescription(description);

        String validatingMessage = validateAuthor(author);
        if (!validatingMessage.isEmpty()) {
            sendMessage(request, MessageType.ERROR, validatingMessage);
        } else {
            //DAOInstances.getAuthorDAO().save(author);
            System.out.println(author);
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
