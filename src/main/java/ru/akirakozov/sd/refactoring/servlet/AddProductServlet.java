package ru.akirakozov.sd.refactoring.servlet;

import static ru.akirakozov.sd.refactoring.servlet.utils.HTMLUtils.HTMLOK;
import static ru.akirakozov.sd.refactoring.servlet.utils.SQLUtils.executeUpdate;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        executeUpdate(name, price);

        HTMLOK(response);

        response.getWriter().println("OK");
    }
}
