package ru.akirakozov.sd.refactoring.servlet.utils;

import static ru.akirakozov.sd.refactoring.servlet.utils.HTMLUtils.printHTML;

import javax.servlet.http.HttpServletResponse;
import java.sql.*;

public class SQLUtils {
    public static final String SQL_MAX_QUERY = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
    public static final String SQL_MIN_QUERY = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
    public static final String SQL_SUM_QUERY = "SELECT SUM(price) FROM PRODUCT";
    public static final String SQL_CNT_QUERY = "SELECT COUNT(*) FROM PRODUCT";
    public static final String SQL_SLC_QUERY = "SELECT * FROM PRODUCT";
    public static final String SQL_INS_HEAD = "INSERT INTO PRODUCT (NAME, PRICE) VALUES (";
    public static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS PRODUCT" +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " NAME           TEXT    NOT NULL, " +
            " PRICE          INT     NOT NULL)";

    public static void executeQuery(String sql, HttpServletResponse response, String responseBody, String mode) {
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

    public static void executeUpdate(String name, long price) {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                String sql = constructInsert(name, price);

                Statement stmt = c.createStatement();
                stmt.executeUpdate(sql);

                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String constructInsert(String name, long price) {
        return SQL_INS_HEAD + "\"" + name + "\"," + price + ")";
    }
}
