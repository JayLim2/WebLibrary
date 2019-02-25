package dao;

import models.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO extends AbstractDAO<Book, Integer> {
    private PublisherDAO publisherDAO;
    private AuthorDAO authorDAO;

    public BookDAO() {
        this.publisherDAO = new PublisherDAO();
        this.authorDAO = new AuthorDAO();
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
    public List<Book> runQuery(String sql) {
        List<Book> books = new ArrayList<>(20);
        try (Connection connection = getConnection()) {
            ResultSet set = connection
                    .createStatement()
                    .executeQuery(sql);
            while (set.next()) {
                Book book = mapResultSetToEntity(set);
                if (book != null) {
                    books.add(book);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> runQueryWithParams(String sql, Object... params) {
        List<Book> books = new ArrayList<>(20);
        try (Connection connection = getConnection()) {
            ResultSet set = getPreparedStatementResult(connection, sql, params);
            while (set.next()) {
                Book book = mapResultSetToEntity(set);
                if (book != null) {
                    books.add(book);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
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
    private void mapEntityToStatement(Book entity, PreparedStatement statement) throws SQLException {
        statement.setString(1, entity.getTitle());
        statement.setInt(2, entity.getCreatedYear());
        statement.setInt(3, entity.getPublishedYear());
        statement.setString(4, entity.getDescription());
        statement.setString(5, entity.getImageHash());
        statement.setInt(6, entity.getPublisher().getId());
        statement.setInt(7, entity.getAuthor().getId());
    }

    private Book mapResultSetToEntity(ResultSet set) {
        try {
            Book book = new Book();
            book.setId(set.getInt("book_id"));
            book.setTitle(set.getString("title"));
            book.setCreatedYear(set.getInt("created_year"));
            book.setPublishedYear(set.getInt("published_year"));
            book.setDescription(set.getString("description"));
            book.setImageHash(set.getString("image_hash"));
            book.setPublisher(publisherDAO.getById(set.getInt("publisher_id")));
            book.setAuthor(authorDAO.getById(set.getInt("author_id")));
            return book;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
