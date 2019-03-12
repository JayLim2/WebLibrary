package servlets;

import models.Author;
import models.Book;
import models.Publisher;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import utils.DAOInstances;
import utils.ImageHashUtil;
import utils.ParameterHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "add",
        urlPatterns = {
                "/add/author",
                "/add/book",
                "/add/publisher"
        })
public class AddPageServlet extends HttpServlet {

    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
    private static final String ENCODING = "UTF-8";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final String ADD_PREFIX = "/add";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getServletPath();
        switch (path) {
            case ADD_PREFIX + "/author":
                dispatchAddAuthor(request, response);
                break;
            case ADD_PREFIX + "/book":
                dispatchAddBook(request, response);
                break;
            case ADD_PREFIX + "/publisher":
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

        String path = request.getServletPath();
        switch (path) {
            case ADD_PREFIX + "/author":
                handleAddAuthorPostRequest(request, response);
                break;
            case ADD_PREFIX + "/book":
                handleAddBookPostRequest(request, response);
                break;
            case ADD_PREFIX + "/publisher":
                handleAddPublisherPostRequest(request, response);
                break;
            default:
                response.sendError(404);
        }
    }

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
                String validatingMessage = validateAuthorData(
                        author,
                        authorName,
                        birthDate,
                        deathDate,
                        description
                );
                if (!validatingMessage.isEmpty()) {
                    sendMessage(request, MessageType.ERROR, validatingMessage);
                } else {
                    DAOInstances.getAuthorDAO().save(author);
                    sendMessage(request, MessageType.INFORMATION, "Автор добавлен.");
                }

                System.out.println(author);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(request, MessageType.ERROR, "При обработке запроса произошла ошибка: " + ex.getMessage());
        }

        dispatchAddAuthor(request, response);
    }

    private void handleAddBookPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        try {
            String title = null;
            String createdYear = null;
            String publishedYear = null;
            String description = null;
            String imageHash = null;
            String publisherId = null;
            String authorId = null;
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
                            case "title":
                                title = formItem.getString(ENCODING);
                                break;
                            case "createdYear":
                                createdYear = formItem.getString(ENCODING);
                                break;
                            case "publishedYear":
                                publishedYear = formItem.getString(ENCODING);
                                break;
                            case "description":
                                description = formItem.getString(ENCODING);
                                break;
                            case "publisherId":
                                publisherId = formItem.getString(ENCODING);
                                break;
                            case "authorId":
                                authorId = formItem.getString(ENCODING);
                                break;
                        }
                    }
                }

                Book book = new Book();
                String validatingMessage = validateBookData(
                        book,
                        title,
                        createdYear,
                        publishedYear,
                        description,
                        imageHash,
                        publisherId,
                        authorId
                );
                if (!validatingMessage.isEmpty()) {
                    sendMessage(request, MessageType.ERROR, validatingMessage);
                } else {
                    DAOInstances.getBookDAO().save(book);
                    sendMessage(request, MessageType.INFORMATION, "Книга добавлена.");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(request, MessageType.ERROR, "При обработке запроса произошла ошибка: " + ex.getMessage());
        }

        dispatchAddBook(request, response);
    }

    private void handleAddPublisherPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> params = request.getParameterMap();

        dispatchAddPublisher(request, response);
    }

    private static final int AUTHOR_NAME_MIN = 2;
    private static final int AUTHOR_NAME_MAX = 65;
    private static final int AUTHOR_DESCRIPTION_MAX = 5000;

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
        } else if (description.length() > AUTHOR_DESCRIPTION_MAX) {
            message = "Описание не должно превышать " + AUTHOR_DESCRIPTION_MAX;
        }
        return message;
    }

    private String validateAuthorData(Author author, String name, String birthDate,
                                      String deathDate, String description) {
        String message = "";

        if (author == null) {
            message = "Передан некорректный объект автора.";
            return message;
        }

        LocalDate birthDateValue = ParameterHandler.tryParseDate(DATE_TIME_FORMATTER, birthDate);
        LocalDate deathDateValue = ParameterHandler.tryParseDate(DATE_TIME_FORMATTER, deathDate);

        if (name == null || name.isEmpty()) {
            message = "Имя не должно быть пустым.";
        } else if (name.length() < AUTHOR_NAME_MIN || name.length() > AUTHOR_NAME_MAX) {
            message = "Имя автора должно быть от "
                    + AUTHOR_NAME_MIN
                    + " до "
                    + AUTHOR_NAME_MAX
                    + " символов";
        } else if (birthDateValue == null) {
            message = "Дата рождения автора не должна быть пустой.";
        } else if (birthDateValue.isAfter(LocalDate.now())) {
            message = "Дата рождения автора не должна быть после текущей.";
        } else if (deathDateValue != null && !deathDateValue.isAfter(birthDateValue)) {
            message = "Дата смерти раньше или совпадает с датой рождения.";
        } else if (description.length() > AUTHOR_DESCRIPTION_MAX) {
            message = "Описание не должно превышать " + AUTHOR_DESCRIPTION_MAX;
        }

        if (message.isEmpty()) {
            author.setName(name);
            author.setBirthDate(birthDateValue);
            author.setDeathDate(deathDateValue);
            author.setBooks(new ArrayList<>());
            author.setDescription(description);
        }

        return message;
    }

    // TODO: 12.03.2019 restrictions?
    private static final int BOOK_TITLE_MIN = 1;
    private static final int BOOK_TITLE_MAX = 100;
    private static final int BOOK_DESCRIPTION_MAX = 5000;

    private static final int CURRENT_YEAR = LocalDate.now().getYear();

    private String validateBookData(Book book, String title, String createdYear, String publishedYear,
                                    String description, String imageHash, String publisherId, String authorId) {
        String message = "";

        if (book == null) {
            message = "Передан некорректный объект книги";
            return message;
        }

        int createdYearValue = ParameterHandler.tryParseInteger(createdYear);
        int publishedYearValue = ParameterHandler.tryParseInteger(publishedYear);
        int authorIdValue = ParameterHandler.tryParseInteger(authorId);
        int publisherIdValue = ParameterHandler.tryParseInteger(publisherId);

        Author author = authorIdValue != -1 ?
                DAOInstances.getAuthorDAO().getById(authorIdValue) :
                null;
        Publisher publisher = publisherIdValue != -1 ?
                DAOInstances.getPublisherDAO().getById(publisherIdValue) :
                null;

        if (title == null || title.isEmpty()) {
            message = "Название книги не должно быть пустым.";
        } else if (title.length() > BOOK_TITLE_MAX) {
            message = "Название книги не должно превышать длину от " + BOOK_TITLE_MIN + " до " + BOOK_TITLE_MAX;
        } else if (createdYearValue == -1) {
            message = "Год написания должен быть числом.";
        } else if (createdYearValue > CURRENT_YEAR) {
            message = "Год написания должен быть не больше текущего.";
        } else if (publishedYearValue == -1) {
            message = "Год публикации должен быть числом.";
        } else if (publishedYearValue > CURRENT_YEAR) {
            message = "Год написания должен быть не больше текущего.";
        } else if (author == null) {
            message = "Выберите автора.";
        } else if (publisher == null) {
            message = "Выберите издателя.";
        } else if (description.length() > BOOK_DESCRIPTION_MAX) {
            message = "Описание книги не должно превышать " + BOOK_DESCRIPTION_MAX + " символов";
        }

        if (message.isEmpty()) {
            book.setTitle(title);
            book.setCreatedYear(createdYearValue);
            book.setPublishedYear(publishedYearValue);
            book.setDescription(description);
            book.setImageHash((imageHash != null && !imageHash.isEmpty() ? imageHash : ImageHashUtil.getDefaultBook()));
            book.setAuthor(author);
            book.setPublisher(publisher);
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
