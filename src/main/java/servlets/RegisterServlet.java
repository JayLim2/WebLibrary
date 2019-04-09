package servlets;

import models.MessageType;
import models.User;
import utils.DAOInstances;
import utils.Validators;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static utils.MessageSender.sendMessage;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dispatch(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String verifyPassword = request.getParameter("verifyPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        User user = new User();
        String validationResult = Validators.validateUserData(user, login, password, verifyPassword, firstName, lastName);
        if (!validationResult.isEmpty()) {
            sendMessage(request, MessageType.ERROR, validationResult);
        } else if (DAOInstances.getUserDAO().save(user)) {
            sendMessage(request, MessageType.INFORMATION, "Регистрация завершена.");
        } else {
            sendMessage(request, MessageType.ERROR, "Такой пользователь уже зарегистрирован.");
        }

        request.setAttribute("login", login);
        request.setAttribute("password", password);
        request.setAttribute("verifyPassword", verifyPassword);
        request.setAttribute("firstName", firstName);
        request.setAttribute("lastName", lastName);

        dispatch(request, response);
    }

    private void dispatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/pages/users/register.jsp")
                .forward(request, response);
    }
}
