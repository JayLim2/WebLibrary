package utils;

import dao.*;
import models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import static utils.DAOInstances.*;

public class DatabaseInitializer {

    private static volatile Random random;
    private static volatile boolean alreadyInitialized = false;

    public synchronized static void initializeByTestData() {
        if (true) return;

        random = new Random();

        AuthorDAO authorDAO = getAuthorDAO();
        PublisherDAO publisherDAO = getPublisherDAO();
        BookDAO bookDAO = getBookDAO();
        GenreDAO genreDAO = getGenreDAO();
        UserDAO userDAO = getUserDAO();
        BookRatingDAO bookRatingDAO = getBookRatingDAO();

        int authorsCount = 7;
        int publishersCount = 4;
        int booksCount = 25;
        int genresCount = 15;
        int usersCount = 4;
        int bookRatingsCount = 9;

        //authors
        for (int i = 1; i <= authorsCount; i++) {
            Author author = new Author();
            author.setName("Author " + i);
            LocalDate date = randomDate();
            author.setBirthDate(date);
            author.setDeathDate(randomOffsetDate(date));
            author.setDescription("some text about author " + i);
            author.setImageHash(HashUtil.getDefaultAuthor());
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
            String login = "user" + i;
            user.setLogin(login);
            String pass = new String(
                    encoder.encode(login.getBytes())
            );
            user.setPassword(pass);
            user.setFirstName("Name " + i);
            user.setLastName("Last Name " + i);
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
            book.setImageHash(HashUtil.getDefaultBook());
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

        //book genres
        for (int i = 1; i <= booksCount; i++) {
            int bookGenresCount = random.nextInt(genresCount);
            List<Genre> genres = new ArrayList<>();
            for (int j = 0; j < bookGenresCount; j++) {
                genres.add(
                        genreDAO.getById(random.nextInt(genresCount) + 1)
                );
            }
            bookDAO.addBookGenres(
                    bookDAO.getById(random.nextInt(booksCount) + 1),
                    genres
            );
        }

        //fav genres
        for (int i = 1; i <= usersCount; i++) {
            int favGenresCount = random.nextInt(genresCount);
            for (int j = 0; j < favGenresCount; j++) {
                int userId = random.nextInt(usersCount) + 1;
                int genreId = random.nextInt(genresCount) + 1;
                genreDAO.saveFavoriteGenreByUser(
                        userDAO.getById(userId),
                        genreDAO.getById(genreId)
                );
            }
        }

        alreadyInitialized = true;

        System.out.println(alreadyInitialized);
    }

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
