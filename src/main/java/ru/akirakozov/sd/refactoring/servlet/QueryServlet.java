package ru.akirakozov.sd.refactoring.servlet;

import static ru.akirakozov.sd.refactoring.servlet.utils.SQLUtils.performQuery;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            performQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1", response,
                    "<h1>Product with max price: </h1>", "items");
        } else if ("min".equals(command)) {
            performQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1", response,
                    "<h1>Product with min price: </h1>", "items");
        } else if ("sum".equals(command)) {
            performQuery("SELECT SUM(price) FROM PRODUCT", response,
                    "Summary price: ", "function");
        } else if ("count".equals(command)) {
            performQuery("SELECT COUNT(*) FROM PRODUCT", response,
                    "Number of products: ", "function");
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
