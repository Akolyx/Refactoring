package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.BaseServletTest;
import ru.akirakozov.sd.refactoring.TestUtils;

import org.apache.http.client.utils.URIBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AddGetServletTest extends BaseServletTest {
    @Before
    public void clearance() {
        TestUtils.clearDatabase();
    }

    @Test
    public void emptyResponseTest() throws Exception {
        checkGetResponse(expectedHTMLBody(new ArrayList<>(), new ArrayList<>()),
                TestUtils.getClient());
    }

    @Test
    public void nonEmptyResponseTest() throws Exception {
        ArrayList<String> names = new ArrayList<>(Arrays.asList("potatoes", "tomatoes", "eggs", "milk", "bread", "brains"));
        ArrayList<Long> prices = new ArrayList<>(Arrays.asList(30L, 42L, 52L, 130L, 845L, 1L));

        assertEquals(names.size(), prices.size());

        TestUtils.clearDatabase();

        HttpClient client = TestUtils.getClient();

        for (int i = 0; i < names.size(); ++i) {
            checkAddResponse(names.get(i), prices.get(i), client);
            checkGetResponse(expectedHTMLBody(names.subList(0, i + 1),
                    prices.subList(0, i + 1)), client);
        }
    }

    @Test
    public void duplicatesTest() throws Exception {
        ArrayList<String> names = new ArrayList<>(Arrays.asList("potatoes", "potatoes", "potatoes", "potatoes"));
        ArrayList<Long> prices = new ArrayList<>(Arrays.asList(32L, 40L, 32L, 52L));

        assertEquals(names.size(), prices.size());

        TestUtils.clearDatabase();

        HttpClient client = TestUtils.getClient();

        for (int i = 0; i < names.size(); ++i) {
            checkAddResponse(names.get(i), prices.get(i), client);
            checkGetResponse(expectedHTMLBody(names.subList(0, i + 1), prices.subList(0, i + 1)), client);
        }
    }

    @Test
    public void invalidParametersTest() throws Exception {
        long price = 52L;
        HttpClient client = TestUtils.getClient();
        String uriString = TestUtils.ADDRESS + TestUtils.SERVLETS_NAMES.ADD_PRODUCT.label;
        URI uri = new URIBuilder(uriString).addParameter("price", Long.toString(price)).build();

        HttpResponse<String> response = TestUtils.getResponse(client, uri);

        assertEquals(OK_RESPONSE_CODE, response.statusCode());
        assertEquals("OK" + System.lineSeparator(), response.body());

        checkGetResponse(expectedHTMLBody(List.of("null"), List.of(price)), client);

        uri = new URIBuilder(uriString).addParameter("name", "potatoes").build();
        response = TestUtils.getResponse(TestUtils.getClient(), uri);

        assertEquals(ERROR_RESPONSE_CODE, response.statusCode());

        uri = new URIBuilder(uriString)
                .addParameter("name", "potatoes")
                .addParameter("price", "price is not a number")
                .build();

        response = TestUtils.getResponse(TestUtils.getClient(), uri);

        assertEquals(ERROR_RESPONSE_CODE, response.statusCode());
    }

    private void checkGetResponse(String expectedBody, HttpClient client) throws Exception {
        String uriString = TestUtils.ADDRESS + TestUtils.SERVLETS_NAMES.GET_PRODUCT.label;
        URI uri = new URIBuilder(uriString).build();

        HttpResponse<String> response = TestUtils.getResponse(client, uri);

        assertEquals(OK_RESPONSE_CODE, response.statusCode());
        assertEquals(expectedBody, response.body());
    }

    private void checkAddResponse(String name, Long price, HttpClient client) throws Exception {
        assertEquals("OK" + System.lineSeparator(), TestUtils.addProduct(name, price, client));
    }

    private String expectedHTMLBody(List<String> names, List<Long> prices) {
        Assert.assertEquals(names.size(), prices.size());

        StringBuilder builder = new StringBuilder("<html><body>" + System.lineSeparator());

        for (int i = 0; i < names.size(); ++i) {
            builder.append(names.get(i)).append('\t').append(prices.get(i)).append("</br>").append(System.lineSeparator());
        }

        builder.append("</body></html>").append(System.lineSeparator());

        return builder.toString();
    }
}