package dao;

import exceptions.InvalidCredentialsException;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO extends AbstractDAO<User, Integer> {
    private static UserDAO instance;

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    @Override
    public User getById(Integer id) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM users WHERE user_id = ?");
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
    public List<User> getAll() {
        return runQuery("SELECT * FROM users");
    }

    @Override
    public boolean save(User entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO users(login, password, first_name, last_name) VALUES (?, ?, ?, ?)");
            mapEntityToStatement(entity, statement);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(User entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("UPDATE users SET login = ?, password = ?, first_name = ?, last_name = ? WHERE user_id = ?");
            mapEntityToStatement(entity, statement);
            statement.setInt(3, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(User entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("DELETE FROM users WHERE user_id = ?");
            statement.setInt(1, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void mapEntityToStatement(User entity, PreparedStatement statement) throws SQLException {
        statement.setString(1, entity.getLogin());
        statement.setString(2, entity.getPassword());
        statement.setString(3, entity.getFirstName());
        statement.setString(4, entity.getLastName());
    }

    @Override
    protected User mapResultSetToEntity(ResultSet set) throws SQLException {
        return new User(
                set.getInt(1),
                set.getString("login"),
                set.getString("password"),
                set.getString("first_name"),
                set.getString("last_name")
        );
    }

    public User getUserByCredentials(String login, String password) throws InvalidCredentialsException {
        User user = null;
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE login = ?");
            statement.setString(1, login);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                user = mapResultSetToEntity(set);

                if (!user.getPassword().equals(password)) {
                    throw new InvalidCredentialsException("пароль введен неверно");
                }
            } else {
                throw new InvalidCredentialsException("пользователь с таким логином не найден");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
