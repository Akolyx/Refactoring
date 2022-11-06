package ru.akirakozov.sd.refactoring.servlet.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HTMLUtils {
    public static final String HTML_MAX_BODY = "<h1>Product with max price: </h1>";
    public static final String HTML_MIN_BODY = "<h1>Product with min price: </h1>";
    public static final String HTML_SUM_BODY = "Summary price: ";
    public static final String HTML_CNT_BODY = "Number of products: ";

    public static void printHTML(ResultSet rs, HttpServletResponse response, String responseBody, String mode)
            throws IOException, SQLException {
        response.getWriter().println("<html><body>");

        if (!responseBody.isEmpty()) {
            response.getWriter().println(responseBody);
        }

        while (rs.next()) {
            if (mode.equals("items")) {
                itemsInfo(rs, response);
            } else if (mode.equals("function")) {
                functionResult(rs, response);
            }
        }

        response.getWriter().println("</body></html>");
    }

    public static void HTMLOK(HttpServletResponse response) {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private static void functionResult(ResultSet rs, HttpServletResponse response) throws IOException, SQLException {
        response.getWriter().println(rs.getInt(1));
    }

    private static void itemsInfo(ResultSet rs, HttpServletResponse response) throws IOException, SQLException {
        String name = rs.getString("name");
        int price = rs.getInt("price");
        response.getWriter().println(name + "\t" + price + "</br>");
    }
}
