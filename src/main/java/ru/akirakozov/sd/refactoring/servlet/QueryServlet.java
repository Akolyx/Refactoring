package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.servlet.utils.SQLUtils;
import ru.akirakozov.sd.refactoring.servlet.utils.HTMLUtils;

import static ru.akirakozov.sd.refactoring.servlet.utils.HTMLUtils.HTMLOK;
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
            executeQuery(SQLUtils.SQL_MAX_QUERY, response, HTMLUtils.HTML_MAX_BODY, "items");
        } else if ("min".equals(command)) {
            executeQuery(SQLUtils.SQL_MIN_QUERY, response, HTMLUtils.HTML_MIN_BODY, "items");
        } else if ("sum".equals(command)) {
            executeQuery(SQLUtils.SQL_SUM_QUERY, response, HTMLUtils.HTML_SUM_BODY, "function");
        } else if ("count".equals(command)) {
            executeQuery(SQLUtils.SQL_CNT_QUERY, response, HTMLUtils.HTML_CNT_BODY, "function");
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        HTMLOK(response);
    }

}
