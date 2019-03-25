package utils;

import dao.*;

public class DAOInstances {

    public static BookDAO getBookDAO() {
        return BookDAO.getInstance();
    }

    public static AuthorDAO getAuthorDAO() {
        return AuthorDAO.getInstance();
    }

    public static PublisherDAO getPublisherDAO() {
        return PublisherDAO.getInstance();
    }

    public static BookRatingDAO getBookRatingDAO() {
        return BookRatingDAO.getInstance();
    }

    public static GenreDAO getGenreDAO() {
        return GenreDAO.getInstance();
    }

    public static UserDAO getUserDAO() {
        return UserDAO.getInstance();
    }

}
