package services;

import dao.GenreDAO;
import models.Genre;
import models.User;

import java.util.List;

public class GenresService implements AbstractService<Genre, Integer> {

    private GenreDAO genreDAO;

    public GenresService() {
        this.genreDAO = new GenreDAO();
    }

    @Override
    public List<Genre> getAll() {
        return genreDAO.getAll();
    }

    @Override
    public Genre getById(Integer id) {
        return genreDAO.getById(id);
    }

    @Override
    public boolean save(Genre entity) {
        return genreDAO.save(entity);
    }

    @Override
    public boolean update(Genre entity) {
        return genreDAO.update(entity);
    }

    @Override
    public boolean delete(Genre entity) {
        return genreDAO.delete(entity);
    }

    public void saveFavoriteGenre(Genre genre, User user) {

    }
}
