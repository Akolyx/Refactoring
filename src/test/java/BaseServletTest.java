import ru.akirakozov.sd.refactoring.Main;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BaseServletTest {
    public static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @BeforeClass
    public static void beforeAll() throws InterruptedException {
        executorService.submit(() -> {
            try {
                Main.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread.sleep(1000);
    }

    @AfterClass
    public static void afterALl() {
        executorService.shutdown();
        TestUtils.clearDatabase();
    }
}