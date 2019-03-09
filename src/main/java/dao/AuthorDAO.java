package dao;

import models.Author;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO extends AbstractDAO<Author, Integer> {
    private static AuthorDAO instance;

    private AuthorDAO() {
    }

    public static AuthorDAO getInstance() {
        if (instance == null) {
            instance = new AuthorDAO();
        }
        return instance;
    }

    @Override
    public Author getById(Integer id) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM authors WHERE author_id = ?");
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
    public List<Author> getAll() {
        return runQuery("SELECT * FROM authors");
    }

    @Override
    public boolean save(Author entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection
                            .prepareStatement("INSERT INTO authors(name, birth_date, death_date, image_hash, description) VALUES (?, ?, ?, ?, ?)");
            mapEntityToStatement(entity, statement);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Author entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("UPDATE authors SET name = ?, birth_date = ?, death_date = ?, image_hash = ?, description = ? WHERE author_id = ?");
            mapEntityToStatement(entity, statement);
            statement.setInt(6, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Author entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("DELETE FROM authors WHERE author_id = ?");
            statement.setInt(1, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Вспомогательные методы маппинга рукотворного
    protected void mapEntityToStatement(Author entity, PreparedStatement statement) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setDate(2, Date.valueOf(entity.getBirthDate()));
        statement.setDate(3, Date.valueOf(entity.getDeathDate()));
        statement.setString(4, entity.getImageHash());
        statement.setString(5, entity.getDescription());
    }

    protected Author mapResultSetToEntity(ResultSet set) {
        try {
            Author author = new Author();
            author.setId(set.getInt(1));
            author.setName(set.getString("name"));
            LocalDate birthDate = set.getDate("birth_date").toLocalDate();
            LocalDate deathDate = set.getDate("death_date").toLocalDate();
            author.setBirthDate(birthDate);
            author.setDeathDate(deathDate);
            author.setImageHash(set.getString("image_hash"));
            author.setDescription(set.getString("description"));
            author.setBooks(new ArrayList<>());
            return author;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Author> getByBirthDateBetween(LocalDate date1, LocalDate date2) {
        String sql = "SELECT * FROM authors WHERE birth_date BETWEEN ? AND ?";
        return runQueryWithParams(sql, Date.valueOf(date1), Date.valueOf(date2));
    }

    public List<Author> getByDeathDateBetween(LocalDate date1, LocalDate date2) {
        String sql = "SELECT * FROM authors WHERE death_date BETWEEN ? AND ?";
        return runQueryWithParams(sql, Date.valueOf(date1), Date.valueOf(date2));
    }

    public List<Author> getByDescription(String description) {
        String sql = "SELECT * FROM authors WHERE description like ?";
        return runQueryWithParams(sql, "%" + description + "%");
    }
}
