package ru.akirakozov.sd.refactoring.servlet;

import static ru.akirakozov.sd.refactoring.servlet.SQLUtils.performQuery;

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
        performQuery("SELECT * FROM PRODUCT", response, "", "items");

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
