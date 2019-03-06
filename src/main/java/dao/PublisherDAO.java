package dao;

import models.Publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PublisherDAO extends AbstractDAO<Publisher, Integer> {

    private static PublisherDAO instance;

    private PublisherDAO() {
    }

    public static PublisherDAO getInstance() {
        if (instance == null) {
            instance = new PublisherDAO();
        }
        return instance;
    }

    @Override
    public Publisher getById(Integer id) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM publishers WHERE publisher_id = ?");
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
    public List<Publisher> getAll() {
        return runQuery("SELECT * FROM publishers");
    }

    @Override
    public boolean save(Publisher entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO publishers(title) VALUES (?)");
            mapEntityToStatement(entity, statement);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Publisher entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("UPDATE publishers SET title = ? WHERE publisher_id = ?");
            mapEntityToStatement(entity, statement);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Publisher entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("DELETE FROM publishers WHERE publisher_id = ?");
            statement.setInt(1, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void mapEntityToStatement(Publisher entity, PreparedStatement statement) throws SQLException {
        statement.setString(1, entity.getTitle());
    }

    @Override
    protected Publisher mapResultSetToEntity(ResultSet set) {
        try {
            Publisher publisher = new Publisher();
            publisher.setId(set.getInt(1));
            publisher.setTitle(set.getString("title"));
            return publisher;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
