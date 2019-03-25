package servlets;

import models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user/profile")
public class UserProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object userObject = request.getSession().getAttribute("user");
        if (userObject instanceof User) {
            User user = (User) userObject;
            request.setAttribute("login", user.getLogin());
            request.setAttribute("firstName", user.getFirstName());
            request.setAttribute("lastName", user.getLastName());

            request.getRequestDispatcher("/pages/users/profile.jsp")
                    .forward(request, response);
        } else {
            response.sendRedirect("/login");
        }
    }
}
