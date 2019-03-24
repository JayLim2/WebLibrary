package dao;

import models.Book;
import models.BookRating;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static utils.DAOInstances.getBookDAO;
import static utils.DAOInstances.getUserDAO;

public class BookRatingDAO extends AbstractDAO<BookRating, Integer> {
    private static BookRatingDAO instance;

    private BookDAO bookDAO;
    private UserDAO userDAO;

    private BookRatingDAO() {
        this.bookDAO = getBookDAO();
        this.userDAO = getUserDAO();
    }

    public static BookRatingDAO getInstance() {
        if (instance == null) {
            instance = new BookRatingDAO();
        }
        return instance;
    }

    @Override
    public BookRating getById(Integer id) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM book_ratings WHERE rating_id = ?");
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return mapResultSetToEntity(set);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BookRating> getAll() {
        return runQuery("SELECT * FROM book_ratings");
    }

    public List<BookRating> getByUser(User user) {
        List<BookRating> bookRatings = null;
        if (user != null) {
            bookRatings = runQueryWithParams("SELECT * FROM book_ratings WHERE user_id = ?", user.getId());
        }
        return bookRatings;
    }

    public float getByBook(Book book) {
        float value = 0F;
        if (book != null) {
            List<BookRating> bookRatings = runQueryWithParams("SELECT * FROM book_ratings WHERE book_id = ?", book.getId());
            for (BookRating bookRating : bookRatings) {
                value += bookRating.getValue();
            }
            value /= bookRatings.size();
            value = (float) Math.round(value * 100) / 100;
        }
        return value;
    }

    public BookRating getByUserAndBook(User user, Book book) {
        BookRating bookRating = null;
        try (Connection connection = getConnection()) {
            if (user != null && book != null) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM book_ratings WHERE user_id = ? AND book_id = ?");
                statement.setInt(1, user.getId());
                statement.setInt(2, book.getId());
                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    bookRating = mapResultSetToEntity(set);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookRating;
    }

    @Override
    public boolean save(BookRating entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO book_ratings(value, book_id, user_id) VALUES (?, ?, ?)");
            mapEntityToStatement(entity, statement);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(BookRating entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("UPDATE book_ratings SET value = ?, book_id = ?, user_id = ? WHERE rating_id = ?");
            mapEntityToStatement(entity, statement);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(BookRating entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("DELETE FROM book_ratings WHERE rating_id = ?");
            statement.setInt(1, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void mapEntityToStatement(BookRating entity, PreparedStatement statement) throws SQLException {
        statement.setInt(1, entity.getValue());
        statement.setInt(2, entity.getBook().getId());
        statement.setInt(3, entity.getUser().getId());
    }

    @Override
    protected BookRating mapResultSetToEntity(ResultSet set) throws SQLException {
        return new BookRating(
                set.getInt(1),
                set.getInt("value"),
                bookDAO.getById(set.getInt("book_id")),
                userDAO.getById(set.getInt("user_id"))
        );
    }
}
