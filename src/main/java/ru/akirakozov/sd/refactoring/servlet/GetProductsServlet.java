package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.servlet.utils.SQLUtils;

import static ru.akirakozov.sd.refactoring.servlet.utils.HTMLUtils.HTMLOK;
import static ru.akirakozov.sd.refactoring.servlet.utils.SQLUtils.executeQuery;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        executeQuery(SQLUtils.SQL_SLC_QUERY, response, "", "items");

        HTMLOK(response);
    }
}
