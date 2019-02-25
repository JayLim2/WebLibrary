package dao;

import entities.Author;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO extends AbstractDAO<Author, Integer> {
    @Override
    public Author getById(Integer id) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection
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
        List<Author> authors = new ArrayList<>(20);
        try (Connection connection = getConnection()) {
            ResultSet set = connection
                    .createStatement()
                    .executeQuery("SELECT * FROM authors");
            while (set.next()) {
                Author author = mapResultSetToEntity(set);
                if (author != null) {
                    authors.add(author);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authors;
    }

    @Override
    public boolean save(Author entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection
                            .prepareStatement("INSERT INTO authors(name, birth_date, death_date, description) VALUES (?, ?, ?, ?)");
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
                    connection
                            .prepareStatement("UPDATE authors SET name = ?, birth_date = ?, death_date = ?, description = ? WHERE author_id = ?");
            mapEntityToStatement(entity, statement);
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
                    connection
                            .prepareStatement("DELETE FROM authors WHERE author_id = ?");
            statement.setInt(1, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Вспомогательные методы маппинга рукотворного
    private void mapEntityToStatement(Author entity, PreparedStatement statement) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setDate(2, Date.valueOf(entity.getBirthDate()));
        statement.setDate(3, Date.valueOf(entity.getDeathDate()));
        statement.setString(4, entity.getDescription());
    }

    private Author mapResultSetToEntity(ResultSet set) {
        try {
            Author author = new Author();
            author.setId(set.getInt("author_id"));
            author.setName(set.getString("name"));
            LocalDate birthDate = set.getDate("birth_date").toLocalDate();
            LocalDate deathDate = set.getDate("death_date").toLocalDate();
            author.setBirthDate(birthDate);
            author.setDeathDate(deathDate);
            author.setDescription(set.getString("description"));
            return author;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
