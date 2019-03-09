package dao;

import models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static utils.DAOInstances.getAuthorDAO;
import static utils.DAOInstances.getPublisherDAO;

public class BookDAO extends AbstractDAO<Book, Integer> {
    private static BookDAO instance;

    private PublisherDAO publisherDAO;
    private AuthorDAO authorDAO;

    public BookDAO() {
        this.publisherDAO = getPublisherDAO();
        this.authorDAO = getAuthorDAO();
    }

    public static BookDAO getInstance() {
        if (instance == null) {
            instance = new BookDAO();
        }
        return instance;
    }

    @Override
    public Book getById(Integer id) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM books WHERE book_id = ?");
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
    public List<Book> getAll() {
        return runQuery("SELECT * FROM books");
    }

    @Override
    public boolean save(Book entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO books(title, created_year, published_year, description, image_hash, publisher_id, author_id) VALUES (?, ?, ?, ?, ?, ?, ?)");
            mapEntityToStatement(entity, statement);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Book entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("UPDATE books SET title = ?, created_year = ?, published_year = ?, description = ?, image_hash = ?, publisher_id = ?, author_id = ? WHERE book_id = ?");
            mapEntityToStatement(entity, statement);
            statement.setInt(8, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Book entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("DELETE FROM books WHERE book_id = ?");
            statement.setInt(1, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Вспомогательные методы маппинга рукотворного
    protected void mapEntityToStatement(Book entity, PreparedStatement statement) throws SQLException {
        statement.setString(1, entity.getTitle());
        statement.setInt(2, entity.getCreatedYear());
        statement.setInt(3, entity.getPublishedYear());
        statement.setString(4, entity.getDescription());
        statement.setString(5, entity.getImageHash());
        statement.setInt(6, entity.getPublisher().getId());
        statement.setInt(7, entity.getAuthor().getId());
    }

    protected Book mapResultSetToEntity(ResultSet set) throws SQLException {
        Book book = new Book();
        book.setId(set.getInt(1));
        book.setTitle(set.getString("title"));
        book.setCreatedYear(set.getInt("created_year"));
        book.setPublishedYear(set.getInt("published_year"));
        book.setDescription(set.getString("description"));
        book.setImageHash(set.getString("image_hash"));
        book.setPublisher(publisherDAO.getById(set.getInt("publisher_id")));
        book.setAuthor(authorDAO.getById(set.getInt("author_id")));
        return book;
    }

    //custom queries
    public void addBookGenre(Book book, Genre genre) {
        try(Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO book_genres(book_id, genre_id) VALUES (?, ?)");
            statement.setInt(1, book.getId());
            statement.setInt(2, genre.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Book> getByTitle(String title) {
        return null;
    }

    public List<Book> getByCreatedYears(int leftBorder, int rightBorder) {
        String sql = "SELECT * FROM books WHERE created_year BETWEEN ? AND ?";
        return runQueryWithParams(sql, leftBorder, rightBorder);
    }

    public List<Book> getByPublishedYears(int leftBorder, int rightBorder) {
        String sql = "SELECT * FROM books WHERE published_year BETWEEN ? AND ?";
        return runQueryWithParams(sql, leftBorder, rightBorder);
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
        String sql = "SELECT * FROM books WHERE author_id = ?";
        return runQueryWithParams(sql, author.getId());
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
}
