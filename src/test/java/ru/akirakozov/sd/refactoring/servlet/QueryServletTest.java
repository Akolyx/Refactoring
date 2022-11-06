package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.BaseServletTest;
import ru.akirakozov.sd.refactoring.TestUtils;

import org.apache.http.client.utils.URIBuilder;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class QueryServletTest extends BaseServletTest {
    private static final HashMap<String, String> commands = new HashMap<>() {{
        put("max", "<h1>Product with max price: </h1>");
        put("min", "<h1>Product with min price: </h1>");
        put("sum", "Summary price: ");
        put("count", "Number of products: ");
        put("other", "Unknown command: other");
        put("unknown", "Unknown command: unknown");
        put(null, "Unknown command: null");
    }};

    private static final ArrayList<String> TEST_NAMES = new ArrayList<>(Arrays.asList(
            "potatoes", "tomatoes", "eggs", "milk", "bread", "brains"));
    
    private static final ArrayList<Long> TEST_PRICES = new ArrayList<>(Arrays.asList(
            30L, 42L, 52L, 130L, 845L, 1L));

    private static final int OK_RESPONSE_CODE = 200;
    
    @Before
    public void clearance() {
        TestUtils.clearDatabase();
    }

    @Test
    public void maxQueryTest() throws Exception {
        HttpClient client = TestUtils.getClient();
        String command = "max";
        
        HttpResponse<String> maxResponse = getQueryResponse(client, command);
        
        assertEquals(OK_RESPONSE_CODE, maxResponse.statusCode());
        assertEquals("Not equal body for command " + command,
                expectedHTMLBody(List.of(commands.get(command))),
                maxResponse.body());
        
        SortedMap<Long, String> currentState = new TreeMap<>(Collections.reverseOrder());
        
        for (int i = 0; i < TEST_NAMES.size(); ++i) {
            String curName = TEST_NAMES.get(i);
            long curPrice = TEST_PRICES.get(i);
            
            TestUtils.addProduct(curName, curPrice, client);
            
            currentState.put(curPrice, curName);
            
            maxResponse = getQueryResponse(client, command);
            
            assertEquals(OK_RESPONSE_CODE, maxResponse.statusCode());
            assertEquals("Not equal body for command " + command,
                    expectedHTMLBody(
                            List.of(commands.get(command),
                                    currentState.get(currentState.firstKey()) +
                                            '\t' + currentState.firstKey() + "</br>")),
                    maxResponse.body());
        }
    }

    @Test
    public void minQueryTest() throws Exception {
        HttpClient client = TestUtils.getClient();
        String command = "min";
        
        HttpResponse<String> minResponse = getQueryResponse(client, command);
        
        assertEquals(OK_RESPONSE_CODE, minResponse.statusCode());
        assertEquals("Not equal body for command " + command,
                expectedHTMLBody(List.of(commands.get(command))),
                minResponse.body());
        
        SortedMap<Long, String> currentState = new TreeMap<>();
        
        for (int i = 0; i < TEST_NAMES.size(); ++i) {
            String curName = TEST_NAMES.get(i);
            long curPrice = TEST_PRICES.get(i);
            
            TestUtils.addProduct(curName, curPrice, client);
            
            currentState.put(curPrice, curName);
            
            minResponse = getQueryResponse(client, command);
            
            assertEquals(OK_RESPONSE_CODE, minResponse.statusCode());
            assertEquals("Not equal body for command " + command,
                    expectedHTMLBody(
                            List.of(commands.get(command),
                                    currentState.get(currentState.firstKey()) +
                                            '\t' + currentState.firstKey() + "</br>")),
                    minResponse.body());
        }
    }

    @Test
    public void sumQueryTest() throws Exception {
        HttpClient client = TestUtils.getClient();
        String command = "sum";
        
        HttpResponse<String> sumResponse = getQueryResponse(client, command);
        
        assertEquals(OK_RESPONSE_CODE, sumResponse.statusCode());
        assertEquals("Not equal body for command " + command,
                expectedHTMLBody(List.of(commands.get(command), "0")),
                sumResponse.body());
        
        long sum = 0L;
        
        for (int i = 0; i < TEST_NAMES.size(); ++i) {
            String curName = TEST_NAMES.get(i);
            long curPrice = TEST_PRICES.get(i);
            
            sum += curPrice;
            
            TestUtils.addProduct(curName, curPrice, client);
            
            sumResponse = getQueryResponse(client, command);
            
            assertEquals(OK_RESPONSE_CODE, sumResponse.statusCode());
            assertEquals("Not equal body for command " + command,
                    expectedHTMLBody(
                            List.of(commands.get(command), Long.toString(sum))),
                    sumResponse.body());
        }
    }

    @Test
    public void countQueryTest() throws Exception {
        HttpClient client = TestUtils.getClient();
        String command = "count";
        
        HttpResponse<String> countResponse = getQueryResponse(client, command);
        
        assertEquals(OK_RESPONSE_CODE, countResponse.statusCode());
        assertEquals("Not equal body for command " + command,
                expectedHTMLBody(List.of(commands.get(command), "0")),
                countResponse.body());
        
        for (int i = 0; i < TEST_NAMES.size(); ++i) {
            String curName = TEST_NAMES.get(i);
            long curPrice = TEST_PRICES.get(i);
            
            TestUtils.addProduct(curName, curPrice, client);
            
            countResponse = getQueryResponse(client, command);
            
            assertEquals(OK_RESPONSE_CODE, countResponse.statusCode());
            assertEquals("Not equal body for command " + command,
                    expectedHTMLBody(
                            List.of(commands.get(command), Integer.toString(i + 1))),
                    countResponse.body());
        }
    }

    @Test
    public void wrongQueriesTest() throws Exception {
        HttpClient client = TestUtils.getClient();
        List<String> wrongQueries = new ArrayList<>(Arrays.asList("other", "unknown", null));
        
        for (String command : wrongQueries) {
            HttpResponse<String> minResponse = getQueryResponse(client, command);
            
            assertEquals(OK_RESPONSE_CODE, minResponse.statusCode());
            assertEquals("Not equal body for command " + command,
                    commands.get(command) + System.lineSeparator(),
                    minResponse.body());
        }
    }

    private String expectedHTMLBody(List<String> lines) {
        StringBuilder builder = new StringBuilder("<html><body>" + System.lineSeparator());
        
        for (String line : lines) {
            builder.append(line).append(System.lineSeparator());
        }
        
        builder.append("</body></html>").append(System.lineSeparator());
        
        return builder.toString();
    }

    private HttpResponse<String> getQueryResponse(HttpClient client, String commandParameter) throws Exception {
        String uriString = TestUtils.ADDRESS + TestUtils.SERVLETS_NAMES.QUERY.label;
        URI uri;
        
        if (commandParameter == null) {
            uri = new URIBuilder(uriString).build();
        } else {
            uri = new URIBuilder(uriString).addParameter("command", commandParameter).build();
        }
        
        return TestUtils.getResponse(client, uri);
    }
}
