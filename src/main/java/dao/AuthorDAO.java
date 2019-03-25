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
        Date birthDate = entity.getBirthDate() != null ? Date.valueOf(entity.getBirthDate()) : null;
        Date deathDate = entity.getDeathDate() != null ? Date.valueOf(entity.getDeathDate()) : null;
        statement.setDate(2, birthDate);
        statement.setDate(3, deathDate);
        statement.setString(4, entity.getImageHash());
        statement.setString(5, entity.getDescription());
    }

    protected Author mapResultSetToEntity(ResultSet set) {
        try {
            Author author = new Author();
            author.setId(set.getInt(1));
            author.setName(set.getString("name"));
            Date birthDateFromSql = set.getDate("birth_date");
            Date deathDateFromSql = set.getDate("death_date");
            LocalDate birthDate = birthDateFromSql != null ? birthDateFromSql.toLocalDate() : null;
            LocalDate deathDate = deathDateFromSql != null ? deathDateFromSql.toLocalDate() : null;
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
}
