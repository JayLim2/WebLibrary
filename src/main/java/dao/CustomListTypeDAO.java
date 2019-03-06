package dao;

import models.CustomListType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CustomListTypeDAO extends AbstractDAO<CustomListType, Integer> {

    private static CustomListTypeDAO instance;

    private CustomListTypeDAO() {
    }

    public static CustomListTypeDAO getInstance() {
        if (instance == null) {
            instance = new CustomListTypeDAO();
        }
        return instance;
    }

    @Override
    public CustomListType getById(Integer id) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM custom_list_types WHERE custom_list_type_id = ?");
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
    public List<CustomListType> getAll() {
        return runQuery("SELECT * FROM custom_list_types");
    }

    @Override
    public boolean save(CustomListType entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO custom_list_types(type_name) VALUES (?)");
            mapEntityToStatement(entity, statement);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(CustomListType entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("UPDATE custom_list_types SET type_name = ? WHERE custom_list_type_id = ?");
            mapEntityToStatement(entity, statement);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(CustomListType entity) {
        if (entity == null) return false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement("DELETE FROM custom_list_types WHERE custom_list_type_id = ?");
            statement.setInt(1, entity.getId());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void mapEntityToStatement(CustomListType entity, PreparedStatement statement) throws SQLException {
        statement.setString(1, entity.getName());
    }

    @Override
    protected CustomListType mapResultSetToEntity(ResultSet set) throws SQLException {
        return new CustomListType(
                set.getInt(1),
                set.getString("type_name")
        );
    }
}
