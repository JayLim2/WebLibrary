package servlets;

import exceptions.InvalidSessionException;
import models.Genre;
import models.MessageType;
import models.User;
import utils.DAOInstances;
import utils.ParameterHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static utils.MessageSender.sendMessage;

@WebServlet("/user/favoriteGenres")
public class FavoriteGenresServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/login");
        } else {
            Object userObject = request.getSession().getAttribute("user");
            if (userObject instanceof User) {
                User user = (User) userObject;
                List<Genre> currentUserFavoriteGenres = DAOInstances.getGenreDAO().getFavoriteGenresByUser(user);
                request.setAttribute("favGenres", currentUserFavoriteGenres);
            }
            dispatch(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/login");
        } else {
            Object userObject = request.getSession().getAttribute("user");
            if (userObject instanceof User) {
                User user = (User) userObject;

                Map<String, String[]> params = request.getParameterMap();
                String[] genresList = params.get("genresList");

                List<Genre> currentUserFavoriteGenres = DAOInstances.getGenreDAO().getByIds(
                        Arrays.stream(genresList)
                                .map(ParameterHandler::tryParseInteger)
                                .filter(value -> value != -1)
                                .collect(Collectors.toList())
                );
                DAOInstances.getGenreDAO().saveFavoriteGenresByUser(user, currentUserFavoriteGenres);
                request.setAttribute("favGenres", currentUserFavoriteGenres);
                sendMessage(request, MessageType.INFORMATION, "Избранные жанры сохранены.");
            } else {
                try {
                    throw new InvalidSessionException("Атрибут пользователя не соответствует сущности пользователя.");
                } catch (Exception e) {
                    sendMessage(request, MessageType.ERROR, e.getMessage());
                    e.printStackTrace();
                }
            }

            dispatch(request, response);
        }
    }

    private void dispatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/pages/users/favoriteGenres.jsp")
                .forward(request, response);
    }
}
