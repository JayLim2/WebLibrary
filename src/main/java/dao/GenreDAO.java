package dao;

import models.Genre;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // FIXME: 13.03.2019 
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

    public List<Genre> getByIds(List<Integer> ids) {
        List<Genre> genres = new ArrayList<>();
        if(ids == null || ids.isEmpty()) return genres;
        try (Connection connection = getConnection()) {
            List<String> idsStrings = ids.stream()
                    .map(id -> Integer.toString(id))
                    .collect(Collectors.toList());
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT * FROM genres WHERE genre_id IN (");
            builder.append(String.join(",", idsStrings));
            builder.append(")");
            String sql = builder.toString();
            PreparedStatement statement = connection.prepareStatement(sql);
            //statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                Genre genre = mapResultSetToEntity(set);
                if(genre != null) {
                    genres.add(genre);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return genres;
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
            statement.setInt(2, entity.getId());
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

    public void saveFavoriteGenreByUser(User user, Genre genre) {
        try (Connection connection = getConnection()) {
            removeFavoriteGenresByUser(user);

            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO fav_genres(user_id, genre_id) VALUES (?, ?)");
            statement.setInt(1, user.getId());
            statement.setInt(2, genre.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFavoriteGenresByUser(User user, List<Genre> genres) {
        try(Connection connection = getConnection()) {
            if (user == null || genres == null) {
                return;
            }

            removeFavoriteGenresByUser(user);
            if (!genres.isEmpty()) {
                List<String> genresPartsOfInsert = new ArrayList<>();
                for (Genre genre : genres) {
                    genresPartsOfInsert.add("(" + user.getId() + ", " + genre.getId() + ")");
                }
                connection.createStatement().executeUpdate("INSERT INTO fav_genres(user_id, genre_id) VALUES " + String.join(", ", genresPartsOfInsert));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Genre> getFavoriteGenresByUser(User user) {
        List<Genre> genres = null;
        try (Connection connection = getConnection()) {
            if (user != null) {
                ResultSet set = connection.createStatement()
                        .executeQuery("SELECT genre_id FROM fav_genres WHERE user_id = " + user.getId());
                List<Integer> ids = new ArrayList<>();
                while (set.next()) {
                    ids.add(set.getInt(1));
                }
                genres = getByIds(ids);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(genres).orElse(new ArrayList<>());
    }

    public void removeFavoriteGenresByUser(User user) {
        try (Connection connection = getConnection()) {
            if (user != null) {
                connection.createStatement().executeUpdate("DELETE FROM fav_genres WHERE user_id = " + user.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
