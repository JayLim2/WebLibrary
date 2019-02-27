import dao.*;
import models.*;
import services.AuthorsService;
import services.BooksService;
import services.GenresService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public class WebLibraryApplication {
    public static void main(String[] args) {
        //initialize();

        AuthorsService authorsService = new AuthorsService();

        List<Author> authors = authorsService.getByBirthDateBetween(
                LocalDate.of(1921, 1, 1),
                LocalDate.of(1943, 1, 1)
        );
        authors = authorsService.getByDescription("som");
        authors.forEach(System.out::println);
    }

    public static void initialize() {
        AuthorDAO authorDAO = new AuthorDAO();
        PublisherDAO publisherDAO = new PublisherDAO();
        BookDAO bookDAO = new BookDAO();
        GenreDAO genreDAO = new GenreDAO();
        UserDAO userDAO = new UserDAO();
        BookRatingDAO bookRatingDAO = new BookRatingDAO();
        CustomListTypeDAO customListTypeDAO = new CustomListTypeDAO();

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
        }

        //books
        for (int i = 1; i <= booksCount; i++) {
            Book book = new Book();
            book.setTitle("Book " + i);
            book.setAuthor(authorDAO.getById(random.nextInt(authorsCount)));
            book.setPublisher(publisherDAO.getById(random.nextInt(publishersCount)));
            int createdYear = randomYear(1900, 2000);
            book.setCreatedYear(createdYear);
            book.setPublishedYear(createdYear + randomYear(10, 20));
            bookDAO.save(book);
        }

        //book ratings
        for (int i = 1; i <= bookRatingsCount; i++) {
            BookRating rating = new BookRating();
            rating.setValue(random.nextInt(5) + 2);
            rating.setBook(bookDAO.getById(random.nextInt(booksCount)));
            rating.setUser(userDAO.getById(random.nextInt(usersCount)));
            bookRatingDAO.save(rating);
        }

        //list types
        for (int i = 1; i <= customListTypesCount; i++) {
            CustomListType type = new CustomListType();
            type.setName("List Type " + i);
            customListTypeDAO.save(type);
        }

        BooksService booksService = new BooksService();
        GenresService genresService = new GenresService();

        //book genres
        for (int i = 1; i <= booksCount; i++) {
            int bookGenresCount = random.nextInt(genresCount);
            for (int j = 0; j < bookGenresCount; j++) {
                booksService.addBookGenre(
                        booksService.getById(random.nextInt(booksCount)),
                        genresService.getById(random.nextInt(genresCount))
                );
            }
        }

        //fav genres
        for (int i = 1; i <= usersCount; i++) {
            int favGenresCount = random.nextInt(genresCount);
            for (int j = 0; j < favGenresCount; j++) {
                genresService.saveFavoriteGenre(
                        genresService.getById(random.nextInt(genresCount)),
                        userDAO.getById(random.nextInt(usersCount))
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
