package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class Utils {

    static void performQuery(String sql, HttpServletResponse response, String responseBody, String mode) {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                response.getWriter().println("<html><body>");

                if (!responseBody.isEmpty()) {
                    response.getWriter().println(responseBody);
                }

                while (rs.next()) {
                    if (mode.equals("items")) {
                        String name = rs.getString("name");
                        int price = rs.getInt("price");
                        response.getWriter().println(name + "\t" + price + "</br>");
                    } else if (mode.equals("function")) {
                        response.getWriter().println(rs.getInt(1));
                    }
                }
                response.getWriter().println("</body></html>");

                rs.close();
                stmt.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
