package services;

import dao.AuthorDAO;
import models.Author;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class AuthorsService implements AbstractService<Author, Integer> {
    private AuthorDAO authorDAO;

    public AuthorsService() {
        this.authorDAO = new AuthorDAO();
    }

    @Override
    public List<Author> getAll() {
        return authorDAO.getAll();
    }

    @Override
    public Author getById(Integer id) {
        return authorDAO.getById(id);
    }

    @Override
    public boolean save(Author entity) {
        return authorDAO.save(entity);
    }

    @Override
    public boolean update(Author entity) {
        return authorDAO.update(entity);
    }

    @Override
    public boolean delete(Author entity) {
        return authorDAO.delete(entity);
    }

    public List<Author> getByBirthDateBetween(LocalDate date1, LocalDate date2) {
        String sql = "SELECT * FROM authors WHERE birth_date BETWEEN ? AND ?";
        return authorDAO.runQueryWithParams(sql, Date.valueOf(date1), Date.valueOf(date2));
    }

    public List<Author> getByDeathDateBetween(LocalDate date1, LocalDate date2) {
        String sql = "SELECT * FROM authors WHERE death_date BETWEEN ? AND ?";
        return authorDAO.runQueryWithParams(sql, Date.valueOf(date1), Date.valueOf(date2));
    }

    public List<Author> getByDescription(String description) {
        String sql = "SELECT * FROM authors WHERE description like ?";
        return authorDAO.runQueryWithParams(sql, "%" + description + "%");
    }
}
