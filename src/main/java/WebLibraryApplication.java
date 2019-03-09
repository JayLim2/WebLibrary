import dao.*;
import models.*;
import utils.ImageHashUtil;

import java.time.LocalDate;
import java.util.Base64;
import java.util.Random;

import static utils.DAOInstances.*;

public class WebLibraryApplication {
    public static void main(String[] args) {
        //initialize();

        try {
            //getBookDAO().getAll().forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initialize() {
        AuthorDAO authorDAO = getAuthorDAO();
        PublisherDAO publisherDAO = getPublisherDAO();
        BookDAO bookDAO = getBookDAO();
        GenreDAO genreDAO = getGenreDAO();
        UserDAO userDAO = getUserDAO();
        BookRatingDAO bookRatingDAO = getBookRatingDAO();
        CustomListTypeDAO customListTypeDAO = getCustomListTypeDAO();

        int authorsCount = 7;
        int publishersCount = 4;
        int booksCount = 25;
        int genresCount = 15;
        int usersCount = 4;
        int bookRatingsCount = 9;
        int customListTypesCount = 3;

        //authors
        for (int i = 1; i <= authorsCount; i++) {
            Author author = new Author();
            author.setName("Author " + i);
            LocalDate date = randomDate();
            author.setBirthDate(date);
            author.setDeathDate(randomOffsetDate(date));
            author.setDescription("some text about author " + i);
            author.setImageHash(ImageHashUtil.getDefaultAuthor());
            authorDAO.save(author);
        }

        //publishers
        for (int i = 1; i <= publishersCount; i++) {
            Publisher publisher = new Publisher();
            publisher.setTitle("Publisher " + i);
            publisherDAO.save(publisher);
        }

        //genres
        for (int i = 1; i <= genresCount; i++) {
            Genre genre = new Genre();
            genre.setName("Genre " + i);
            genreDAO.save(genre);
        }

        //users
        Base64.Encoder encoder = Base64.getEncoder();
        for (int i = 1; i <= usersCount; i++) {
            User user = new User();
            String login = "user"+i;
            user.setLogin(login);
            String pass = new String(
                    encoder.encode(login.getBytes())
            );
            user.setPassword(pass);
            userDAO.save(user);
        }

        //books
        for (int i = 1; i <= booksCount; i++) {
            Book book = new Book();
            book.setTitle("Book " + i);
            book.setAuthor(authorDAO.getById(random.nextInt(authorsCount) + 1));
            book.setPublisher(publisherDAO.getById(random.nextInt(publishersCount) + 1));
            int createdYear = randomYear(1900, 2000);
            book.setCreatedYear(createdYear);
            book.setPublishedYear(createdYear + randomYear(10, 20));
            book.setDescription("some description " + i);
            book.setImageHash(ImageHashUtil.getDefaultBook());
            bookDAO.save(book);
        }

        //book ratings
        for (int i = 1; i <= bookRatingsCount; i++) {
            BookRating rating = new BookRating();
            rating.setValue(random.nextInt(5) + 2);
            rating.setBook(bookDAO.getById(random.nextInt(booksCount) + 1));
            rating.setUser(userDAO.getById(random.nextInt(usersCount) + 1));
            bookRatingDAO.save(rating);
        }

        //list types
        for (int i = 1; i <= customListTypesCount; i++) {
            CustomListType type = new CustomListType();
            type.setName("List Type " + i);
            customListTypeDAO.save(type);
        }

        //book genres
        for (int i = 1; i <= booksCount; i++) {
            int bookGenresCount = random.nextInt(genresCount);
            for (int j = 0; j < bookGenresCount; j++) {
                bookDAO.addBookGenre(
                        bookDAO.getById(random.nextInt(booksCount) + 1),
                        genreDAO.getById(random.nextInt(genresCount) + 1)
                );
            }
        }

        //fav genres
        for (int i = 1; i <= usersCount; i++) {
            int favGenresCount = random.nextInt(genresCount);
            for (int j = 0; j < favGenresCount; j++) {
                int userId = random.nextInt(usersCount) + 1;
                int genreId = random.nextInt(genresCount) + 1;
                genreDAO.saveFavoriteGenre(
                        userDAO.getById(userId),
                        genreDAO.getById(genreId)
                );
            }
        }

        //custom lists
        for (int i = 1; i <= 25; i++) {

        }
    }

    private static final Random random = new Random();

    private static LocalDate randomDate() {
        return LocalDate.of(
                randomYear(1900, 2000),
                random.nextInt(12) + 1,
                random.nextInt(28) + 1
        );
    }

    private static LocalDate randomOffsetDate(LocalDate date) {
        int minYears = 25;
        int maxYears = 100;

        return date
                .plusYears(random.nextInt(maxYears - minYears) + minYears + 1)
                .plusMonths(random.nextInt(12) + 1)
                .plusDays(random.nextInt(28) + 1);
    }

    private static int randomYear(int left, int right) {
        return random.nextInt(right - left) + left + 1;
    }
}
