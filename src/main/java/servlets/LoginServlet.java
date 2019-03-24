package servlets;

import exceptions.InvalidCredentialsException;
import models.MessageType;
import models.User;
import utils.DAOInstances;
import utils.HashUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static utils.MessageSender.sendMessage;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") != null) {
            response.sendRedirect("/");
        } else {
            dispatch(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = Optional.ofNullable(request.getParameter("login")).orElse("");
        String password = Optional.ofNullable(request.getParameter("password")).orElse("");
        password = HashUtil.encodeFromBytes(password.getBytes());

        try {
            User user = DAOInstances.getUserDAO().getUserByCredentials(login, password);
            if (user != null) {
                request.getSession().setAttribute("user", user);
                response.sendRedirect("/");
            }
        } catch (InvalidCredentialsException e) {
            sendMessage(request, MessageType.ERROR, e.getMessage());
            dispatch(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/pages/users/login.jsp")
                .forward(request, response);
    }
}
