package dao;

import models.Publisher;

import java.util.List;

public class PublisherDAO extends AbstractDAO<Publisher, Integer> {
    @Override
    public List<Publisher> runQuery(String sql) {
        return null;
    }

    @Override
    public List<Publisher> runQueryWithParams(String sql, Object... params) {
        return null;
    }

    @Override
    public Publisher getById(Integer id) {
        return null;
    }

    @Override
    public List<Publisher> getAll() {
        return null;
    }

    @Override
    public boolean save(Publisher entity) {
        return false;
    }

    @Override
    public boolean update(Publisher entity) {
        return false;
    }

    @Override
    public boolean delete(Publisher entity) {
        return false;
    }
}
