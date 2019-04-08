package utils;

import models.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static utils.ParameterHandler.tryParseInteger;

public class Validators {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

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

    //genre restrictions
    private static final int GENRE_TITLE_MAX = 150;

    //user restrictions
    private static final int USER_LOGIN_MIN = 3;
    private static final int USER_LOGIN_MAX = 20;
    private static final int USER_PASSWORD_MIN = 6;
    private static final int USER_PASSWORD_MAX = 20;
    private static final int USER_FIRST_NAME_MAX = 60;
    private static final int USER_LAST_NAME_MAX = 60;

    public static String validateAuthorData(Author author, String name,
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
                author.setImageHash(HashUtil.getDefaultAuthor());
            }
        }

        return message;
    }

    public static String validateBookData(Book book, String title, String createdYear, String publishedYear,
                                          String description, String imageHash, String publisherId, String authorId,
                                          List<Genre> genres) {
        String message = "";

        if (book == null) {
            message = "Передан некорректный объект книги";
            return message;
        }

        int createdYearValue = tryParseInteger(createdYear);
        int publishedYearValue = tryParseInteger(publishedYear);
        int authorIdValue = tryParseInteger(authorId);
        int publisherIdValue = tryParseInteger(publisherId);
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
                book.setImageHash(HashUtil.getDefaultBook());
            }
        }

        return message;
    }

    public static String validatePublisherData(Publisher publisher, String title, String address) {
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

        if (message.isEmpty()) {
            publisher.setTitle(title);
            publisher.setAddress(address);
        }

        return message;
    }

    public static String validateGenreData(Genre genre, String title) {
        String message = "";

        if (genre == null) {
            message = "Передан некорректный объект жанра.";
            return message;
        }

        if (title == null || title.isEmpty()) {
            message = "Название жанра не может быть пустым.";
        } else if (title.length() > GENRE_TITLE_MAX) {
            message = "Название жанра не должно быть больше " + GENRE_TITLE_MAX + " символов.";
        }

        if (message.isEmpty()) {
            genre.setName(title);
        }

        return message;
    }

    public static String validateUserData(User user, String login, String password, String verifyPassword, String firstName, String lastName) {
        String message = "";

        if (user == null) {
            message = "Передан некорректный объект пользователя.";
            return message;
        }

        if (login == null || login.isEmpty()) {
            message = "Логин не должен быть пустым.";
        } else if (login.length() < USER_LOGIN_MIN || login.length() > USER_LOGIN_MAX) {
            message = "Логин должен быть длиной от "
                    + USER_LOGIN_MIN
                    + " до "
                    + USER_LOGIN_MAX
                    + " символов.";
        } else if (password == null || password.isEmpty()) {
            message = "Пароль не должен быть пустым.";
        } else if (password.length() < USER_PASSWORD_MIN || password.length() > USER_PASSWORD_MAX) {
            message = "Пароль должен быть длиной от "
                    + USER_PASSWORD_MIN
                    + " до "
                    + USER_PASSWORD_MAX
                    + " символов.";
        } else if (!Objects.equals(password, verifyPassword)) {
            message = "Пароли не совпадают.";
        } else if (firstName == null || firstName.isEmpty()) {
            message = "Имя не должно быть пустым.";
        } else if (firstName.length() > USER_FIRST_NAME_MAX) {
            message = "Имя не должно превышать " + USER_FIRST_NAME_MAX + " символов.";
        } else if (lastName == null || lastName.isEmpty()) {
            message = "Фамилия не должна быть пустой.";
        } else if (lastName.length() > USER_LAST_NAME_MAX) {
            message = "Фамилия не должна превышать " + USER_LAST_NAME_MAX + " символов.";
        }

        if (message.isEmpty()) {
            user.setLogin(login);
            user.setPassword(HashUtil.encodeFromBytes(password.getBytes()));
            user.setFirstName(firstName);
            user.setLastName(lastName);
        }

        return message;
    }
}
