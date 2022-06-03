import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class AmazonSearchTest {

    private WebDriver driver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver",
                System.getProperty("user.dir") + "/src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
    }


    @AfterEach
    public void cleanUp() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void verifyAmazonName() {
        driver.get("https://www.amazon.com/");

        Assertions.assertEquals("Amazon.com. Spend less. Smile more.", driver.getTitle());
    }
}
