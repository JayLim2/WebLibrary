package dao;

import models.Genre;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GenreDAO extends AbstractDAO<Genre, Integer> {

    private static GenreDAO instance;

    private GenreDAO() {
    }

    public static GenreDAO getInstance() {
        if (instance == null) {
            instance = new GenreDAO();
        }
        return instance;
    }

    @Override
    public Genre getById(Integer id) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM genres WHERE genre_id = ?");
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
    public List<Genre> getAll() {
        return runQuery("SELECT * FROM genres");
    }

    @Override
    public boolean save(Genre entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO genres(genre_name) VALUES (?)");
            mapEntityToStatement(entity, statement);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Genre entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("UPDATE genres SET genre_name = ? WHERE genre_id = ?");
            mapEntityToStatement(entity, statement);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Genre entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("DELETE FROM genres WHERE genre_id = ?");
            statement.setInt(1, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void mapEntityToStatement(Genre entity, PreparedStatement statement) throws SQLException {
        statement.setString(1, entity.getName());
    }

    @Override
    protected Genre mapResultSetToEntity(ResultSet set) {
        try {
            Genre genre = new Genre();
            genre.setId(set.getInt(1));
            genre.setName(set.getString("genre_name"));
            return genre;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveFavoriteGenre(User user, Genre genre) {
        try(Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO fav_genres(user_id, genre_id) VALUES (?, ?)");
            statement.setInt(1, user.getId());
            statement.setInt(2, genre.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
