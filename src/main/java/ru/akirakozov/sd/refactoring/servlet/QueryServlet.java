package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.servlet.utils.SQLUtils;

import static ru.akirakozov.sd.refactoring.servlet.utils.SQLUtils.executeQuery;

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
            executeQuery(SQLUtils.SQL_MAX_QUERY, response,
                    "<h1>Product with max price: </h1>", "items");
        } else if ("min".equals(command)) {
            executeQuery(SQLUtils.SQL_MIN_QUERY, response,
                    "<h1>Product with min price: </h1>", "items");
        } else if ("sum".equals(command)) {
            executeQuery(SQLUtils.SQL_SUM_QUERY, response,
                    "Summary price: ", "function");
        } else if ("count".equals(command)) {
            executeQuery(SQLUtils.SQL_CNT_QUERY, response,
                    "Number of products: ", "function");
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
