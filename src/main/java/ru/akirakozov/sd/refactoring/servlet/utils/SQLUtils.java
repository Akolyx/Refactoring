package ru.akirakozov.sd.refactoring.servlet.utils;

import static ru.akirakozov.sd.refactoring.servlet.utils.HTMLUtils.printHTML;

import javax.servlet.http.HttpServletResponse;
import java.sql.*;

public class SQLUtils {
    public static void performQuery(String sql, HttpServletResponse response, String responseBody, String mode) {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                printHTML(rs, response, responseBody, mode);

                rs.close();
                stmt.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
