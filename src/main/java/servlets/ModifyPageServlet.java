package servlets;

import models.Author;
import models.Book;
import models.Genre;
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
import java.util.*;

@WebServlet(name = "modifyPageServlet",
        urlPatterns = {
                "/add/author",
                "/add/book",
                "/add/publisher",
                "/edit/author",
                "/edit/book",
                "/edit/publisher",
                "/delete/author",
                "/delete/book",
                "/delete/publisher"
        })
public class ModifyPageServlet extends HttpServlet {

    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
    private static final String ENCODING = "UTF-8";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final String ADD_PREFIX = "/add";
    private static final String EDIT_PREFIX = "/edit";
    private static final String DELETE_PREFIX = "/delete";

    private static final String AUTHOR_ENTITY = "/author";
    private static final String BOOK_ENTITY = "/book";
    private static final String PUBLISHER_ENTITY = "/publisher";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getServletPath();
        switch (path) {
            case ADD_PREFIX + AUTHOR_ENTITY:
                dispatchAddAuthor(request, response);
                break;
            case ADD_PREFIX + BOOK_ENTITY:
                dispatchAddBook(request, response);
                break;
            case ADD_PREFIX + PUBLISHER_ENTITY:
                dispatchAddPublisher(request, response);
                break;

            case EDIT_PREFIX + AUTHOR_ENTITY:
                dispatchEditAuthor(request, response);
                break;
            case EDIT_PREFIX + BOOK_ENTITY:
                dispatchEditBook(request, response);
                break;
            case EDIT_PREFIX + PUBLISHER_ENTITY:
                dispatchEditPublisher(request, response);
                break;

            case DELETE_PREFIX + AUTHOR_ENTITY:
                handleDeleteAuthorGetRequest(request, response);
                break;
            case DELETE_PREFIX + BOOK_ENTITY:
                handleDeleteBookGetRequest(request, response);
                break;
            case DELETE_PREFIX + PUBLISHER_ENTITY:
                handleDeletePublisherGetRequest(request, response);
                break;
            default:
                response.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String path = request.getServletPath();
        switch (path) {
            case ADD_PREFIX + AUTHOR_ENTITY:
                handleAddAuthorPostRequest(request, response);
                break;
            case ADD_PREFIX + BOOK_ENTITY:
                handleAddBookPostRequest(request, response);
                break;
            case ADD_PREFIX + PUBLISHER_ENTITY:
                handleAddPublisherPostRequest(request, response);
                break;

            case EDIT_PREFIX + AUTHOR_ENTITY:
                handleEditAuthorPostRequest(request, response);
                break;
            case EDIT_PREFIX + BOOK_ENTITY:
                handleEditBookPostRequest(request, response);
                break;
            case EDIT_PREFIX + PUBLISHER_ENTITY:
                handleEditPublisherPostRequest(request, response);
                break;

            default:
                response.sendError(404);
        }
    }

    private void handleAddAuthorPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ServletFileUpload upload = getServletFileUpload();
            List<FileItem> formItems = upload.parseRequest(request);
            Map<String, String> fields = getAuthorParams(formItems);
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
                } else {
                    DAOInstances.getAuthorDAO().save(author);
                    sendMessage(request, MessageType.INFORMATION, "Автор добавлен.");
                }
            } else {
                sendMessage(request, MessageType.ERROR, "Параметры не переданы.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(request, MessageType.ERROR, "При обработке запроса произошла ошибка: " + ex.getMessage());
        }

        dispatchAddAuthor(request, response);
    }

    private void handleEditAuthorPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ServletFileUpload upload = getServletFileUpload();
            List<FileItem> formItems = upload.parseRequest(request);
            Map<String, String> fields = getAuthorParams(formItems);
            if (fields != null && !fields.isEmpty()) {
                Author author = DAOInstances.getAuthorDAO().getById(
                        ParameterHandler.tryParseInteger(fields.get("authorId"))
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

        dispatchAddAuthor(request, response);
    }

    private void handleDeleteAuthorGetRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Author author = DAOInstances.getAuthorDAO().getById(
                ParameterHandler.tryParseInteger(request.getParameter("id"))
        );
        if (author == null) {
            sendMessage(request, MessageType.ERROR, "Автор с таким id не существует.");
        } else if (DAOInstances.getAuthorDAO().delete(author)) {
            sendMessage(request, MessageType.INFORMATION, "Автор успешно удален.");
        } else {
            sendMessage(request, MessageType.ERROR, "Ошибка при удалении автора.");
        }

        dispatchDeleteAny(request, response);
    }

    private void handleAddBookPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ServletFileUpload upload = getServletFileUpload();
            List<Genre> genres = new ArrayList<>();
            List<FileItem> formItems = upload.parseRequest(request);
            Map<String, String> fields = getBookParams(formItems, genres);
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
                } else {
                    if (DAOInstances.getBookDAO().saveWithGenres(book, genres)) {
                        sendMessage(request, MessageType.INFORMATION, "Книга добавлена.");
                    } else {
                        sendMessage(request, MessageType.ERROR, "Ошибка при добавлении книги.");
                    }
                }
            } else {
                sendMessage(request, MessageType.ERROR, "Параметры не переданы.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(request, MessageType.ERROR, "При обработке запроса произошла ошибка: " + ex.getMessage());
        }

        dispatchAddBook(request, response);
    }

    private void handleEditBookPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ServletFileUpload upload = getServletFileUpload();
            List<Genre> genres = new ArrayList<>();
            List<FileItem> formItems = upload.parseRequest(request);
            Map<String, String> fields = getBookParams(formItems, genres);
            if (fields != null && !fields.isEmpty()) {
                Book book = DAOInstances.getBookDAO().getById(
                        ParameterHandler.tryParseInteger(fields.get("bookId"))
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

        dispatchAddBook(request, response);
    }

    private void handleDeleteBookGetRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Book book = DAOInstances.getBookDAO().getById(
                ParameterHandler.tryParseInteger(request.getParameter("id"))
        );
        if (book == null) {
            sendMessage(request, MessageType.ERROR, "Книга с таким id не существует.");
        } else if (DAOInstances.getBookDAO().delete(book)) {
            sendMessage(request, MessageType.INFORMATION, "Книга успешно удалена.");
        } else {
            sendMessage(request, MessageType.ERROR, "Ошибка при удалении книги.");
        }

        dispatchDeleteAny(request, response);
    }

    private void handleAddPublisherPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String address = request.getParameter("address");

        Publisher publisher = new Publisher();
        String validatingMessage = validatePublisherData(publisher, title, address);
        if (!validatingMessage.isEmpty()) {
            sendMessage(request, MessageType.ERROR, validatingMessage);
        } else {
            DAOInstances.getPublisherDAO().save(publisher);
            sendMessage(request, MessageType.INFORMATION, "Издатель добавлен.");
        }

        dispatchAddPublisher(request, response);
    }

    private void handleEditPublisherPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String address = request.getParameter("address");

        Publisher publisher = DAOInstances.getPublisherDAO().getById(
                ParameterHandler.tryParseInteger(request.getParameter("publisherId"))
        );

        if (publisher != null) {
            String validatingMessage = validatePublisherData(publisher, title, address);
            if (!validatingMessage.isEmpty()) {
                sendMessage(request, MessageType.ERROR, validatingMessage);
            } else {
                if (DAOInstances.getPublisherDAO().update(publisher)) {
                    sendMessage(request, MessageType.INFORMATION, "Издатель изменен.");
                } else {
                    sendMessage(request, MessageType.ERROR, "Ошибка при изменении издателя.");
                }
            }
        } else {
            sendMessage(request, MessageType.ERROR, "Издателя с таким id не существует.");
        }
        dispatchAddPublisher(request, response);
    }

    private void handleDeletePublisherGetRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Publisher publisher = DAOInstances.getPublisherDAO().getById(
                ParameterHandler.tryParseInteger(request.getParameter("id"))
        );
        if (publisher == null) {
            sendMessage(request, MessageType.ERROR, "Издатель с таким id не существует.");
        } else if (DAOInstances.getPublisherDAO().delete(publisher)) {
            sendMessage(request, MessageType.INFORMATION, "Издатель успешно удалена.");
        } else {
            sendMessage(request, MessageType.ERROR, "Ошибка при удалении издателя.");
        }

        dispatchDeleteAny(request, response);
    }

    //-------------------- parameters of request

    private Map<String, String> getAuthorParams(List<FileItem> formItems) {
        Map<String, String> fields = null;
        try {
            if (formItems != null) {
                fields = new HashMap<>();
                for (FileItem formItem : formItems) {
                    if (!formItem.isFormField() && formItem.getFieldName().equals("poster")) {
                        byte[] fileBytes = formItem.get();
                        fields.put("imageHash", ImageHashUtil.encodeFromBytes(fileBytes));
                    } else {
                        fields.put(formItem.getFieldName(), formItem.getString(ENCODING));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fields;
    }

    private Map<String, String> getBookParams(List<FileItem> formItems, List<Genre> genres) {
        Map<String, String> fields = null;
        try {
            if (formItems != null) {
                fields = new HashMap<>();
                for (FileItem formItem : formItems) {
                    if (!formItem.isFormField() && formItem.getFieldName().equals("poster")) {
                        byte[] fileBytes = formItem.get();
                        fields.put("imageHash", ImageHashUtil.encodeFromBytes(fileBytes));
                    } else {
                        String fieldName = formItem.getFieldName();
                        if (Objects.equals(fieldName, "genresList")) {
                            int id = ParameterHandler.tryParseInteger(formItem.getString(ENCODING));
                            Genre genre = DAOInstances.getGenreDAO().getById(id);
                            if (genre != null) genres.add(genre);
                        } else {
                            fields.put(fieldName, formItem.getString(ENCODING));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fields;
    }

    //--------------------------- validators

    // TODO: 12.03.2019 restrictions?
    //author restrictions
    private static final int AUTHOR_NAME_MIN = 2;
    private static final int AUTHOR_NAME_MAX = 65;
    private static final int AUTHOR_DESCRIPTION_MAX = 5000;

    //book restrictions
    private static final int BOOK_TITLE_MIN = 1;
    private static final int BOOK_TITLE_MAX = 100;
    private static final int BOOK_DESCRIPTION_MAX = 5000;

    //publisher restrictions
    private static final int PUBLISHER_TITLE_MAX = 150;

    @Deprecated
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

    private String validateAuthorData(Author author, String name,
                                      String birthDate, String deathDate,
                                      String imageHash, String description) {
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

            String resultHash = author.getImageHash();
            boolean imageHashIsNull = imageHash == null || imageHash.isEmpty();
            boolean resultHashIsNull = resultHash == null || resultHash.isEmpty();
            if (!imageHashIsNull) {
                author.setImageHash(imageHash);
            } else if (resultHashIsNull) {
                author.setImageHash(ImageHashUtil.getDefaultAuthor());
            }
        }

        return message;
    }

    private String validateBookData(Book book, String title, String createdYear, String publishedYear,
                                    String description, String imageHash, String publisherId, String authorId,
                                    List<Genre> genres) {
        String message = "";

        if (book == null) {
            message = "Передан некорректный объект книги";
            return message;
        }

        int createdYearValue = ParameterHandler.tryParseInteger(createdYear);
        int publishedYearValue = ParameterHandler.tryParseInteger(publishedYear);
        int authorIdValue = ParameterHandler.tryParseInteger(authorId);
        int publisherIdValue = ParameterHandler.tryParseInteger(publisherId);
        int currentYear = LocalDate.now().getYear();

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
        } else if (createdYearValue > currentYear) {
            message = "Год написания должен быть не больше текущего.";
        } else if (publishedYearValue == -1) {
            message = "Год публикации должен быть числом.";
        } else if (publishedYearValue > currentYear) {
            message = "Год написания должен быть не больше текущего.";
        } else if (author == null) {
            message = "Выберите автора.";
        } else if (publisher == null) {
            message = "Выберите издателя.";
        } else if (description.length() > BOOK_DESCRIPTION_MAX) {
            message = "Описание книги не должно превышать " + BOOK_DESCRIPTION_MAX + " символов";
        } else if (genres == null || genres.isEmpty()) {
            message = "Выберите хотя бы 1 жанр.";
        }

        if (message.isEmpty()) {
            book.setTitle(title);
            book.setCreatedYear(createdYearValue);
            book.setPublishedYear(publishedYearValue);
            book.setDescription(description);
            book.setAuthor(author);
            book.setPublisher(publisher);

            String resultHash = book.getImageHash();
            boolean imageHashIsNull = imageHash == null || imageHash.isEmpty();
            boolean resultHashIsNull = resultHash == null || resultHash.isEmpty();
            if (!imageHashIsNull) {
                book.setImageHash(imageHash);
            } else if (resultHashIsNull) {
                book.setImageHash(ImageHashUtil.getDefaultBook());
            }
        }

        return message;
    }

    private String validatePublisherData(Publisher publisher, String title, String address) {
        String message = "";

        if (publisher == null) {
            message = "Передан некорректный объект издателя.";
            return message;
        }

        if (title == null || title.isEmpty()) {
            message = "Название издателя не может быть пустым.";
        } else if (title.length() > PUBLISHER_TITLE_MAX) {
            message = "Название издателя не должно быть больше " + PUBLISHER_TITLE_MAX + " символов.";
        }

        if (!message.isEmpty()) {
            publisher.setTitle(title);
            publisher.setAddress(address);
        }

        return message;
    }

    //-------------- dispatchers
    private void dispatchAddAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(buildModifyPageName(ADD_PREFIX, "Author"))
                .forward(request, response);
    }

    private void dispatchAddBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(buildModifyPageName(ADD_PREFIX, "Book"))
                .forward(request, response);
    }

    private void dispatchAddPublisher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(buildModifyPageName(ADD_PREFIX, "Publisher"))
                .forward(request, response);
    }

    private void dispatchEditAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Author author = DAOInstances.getAuthorDAO().getById(
                ParameterHandler.tryParseInteger(request.getParameter("id"))
        );
        if (author == null) {
            sendMessage(request, MessageType.ERROR, "Автора с таким id не существует.");
        }
        request.setAttribute("author", author);
        request.getRequestDispatcher(buildModifyPageName(EDIT_PREFIX, "Author"))
                .forward(request, response);
    }

    private void dispatchEditBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Book book = DAOInstances.getBookDAO().getById(
                ParameterHandler.tryParseInteger(request.getParameter("id"))
        );
        if (book == null) {
            sendMessage(request, MessageType.ERROR, "Книги с таким id не существует.");
        }
        request.setAttribute("book", book);
        request.getRequestDispatcher(buildModifyPageName(EDIT_PREFIX, "Book"))
                .forward(request, response);
    }

    private void dispatchEditPublisher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Publisher publisher = DAOInstances.getPublisherDAO().getById(
                ParameterHandler.tryParseInteger(request.getParameter("id"))
        );
        if (publisher == null) {
            sendMessage(request, MessageType.ERROR, "Издателя с таким id не существует.");
        }
        request.setAttribute("publisher", publisher);
        request.getRequestDispatcher(buildModifyPageName(EDIT_PREFIX, "Publisher"))
                .forward(request, response);
    }

    private void dispatchDeleteAny(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(buildModifyPageName(DELETE_PREFIX, ""))
                .forward(request, response);
    }

    //-------------- support methods

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

    private ServletFileUpload getServletFileUpload() {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        return upload;
    }

    private String buildModifyPageName(String operation, String entityName) {
        return "/pages/modify" + operation + operation + entityName + ".jsp";
    }

    private String getEntityName(HttpServletRequest request) {
        return Optional.ofNullable(request.getParameter("entity")).orElse("")
                .toLowerCase();
    }
}
