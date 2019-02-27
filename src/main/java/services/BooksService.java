package services;

import dao.BookDAO;
import models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class BooksService implements AbstractService<Book, Integer> {
    private BookDAO bookDAO;

    public BooksService() {
        this.bookDAO = new BookDAO();
    }

    @Override
    public List<Book> getAll() {
        return bookDAO.getAll();
    }

    @Override
    public Book getById(Integer id) {
        return bookDAO.getById(id);
    }

    @Override
    public boolean save(Book entity) {
        return bookDAO.save(entity);
    }

    @Override
    public boolean update(Book entity) {
        return bookDAO.update(entity);
    }

    @Override
    public boolean delete(Book entity) {
        return bookDAO.delete(entity);
    }

    public List<Book> getByTitle(String title) {
        return null;
    }

    public List<Book> getByCreatedYears(int leftBorder, int rightBorder) {
        String sql = "SELECT * FROM books WHERE created_year BETWEEN ? AND ?";
        return bookDAO.runQueryWithParams(sql, leftBorder, rightBorder);
    }

    public List<Book> getByPublishedYears(int leftBorder, int rightBorder) {
        String sql = "SELECT * FROM books WHERE published_year BETWEEN ? AND ?";
        return bookDAO.runQueryWithParams(sql, leftBorder, rightBorder);
    }

    public void setBookRating(Book book, User user, int value) {

    }

    public BookRating getRating(Book book, User user) {
        return null;
    }

    public List<BookRating> getAllRatings(Book book) {
        return null;
    }

    public float getTotalRating(Book book) {
        return 0;
    }

    public List<Book> getByRatings(float leftBorder, float rightBorder) {
        return null;
    }

    public List<Book> getByAuthor(Author author) {
        return null;
    }

    public List<Book> getByPublisher(Publisher publisher) {
        return null;
    }

    public List<Book> getCustomList(Book book, User user, CustomListType type) {
        return null;
    }

    public List<Book> getByGenres(List<Genre> genres) {
        return null;
    }

    public void addBookGenre(Book book, Genre genre) {
        bookDAO.addBookGenres(book, genre);
    }
}
