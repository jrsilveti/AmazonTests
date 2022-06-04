import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

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

    private Set<String> parseWords(String sourceString) {
        Set<String> parsedStrings = new HashSet<>();
        StringBuilder currentWord = new StringBuilder();
        char currentCharacter;

        for(int i =0; i < sourceString.length(); i++) {
            currentCharacter = sourceString.charAt(i);
            //spaces, commas, and periods end words.
            if(currentCharacter == ' ' || currentCharacter == ',' || currentCharacter == '.'
                    || currentCharacter == '\n'){
                if(currentWord.length() > 0) {
                    parsedStrings.add(currentWord.toString());
                    currentWord.delete(0, currentWord.length());
                }
            } else {
                currentWord.append(currentCharacter);
            }
        }

        return parsedStrings;
    }

    @Test
    public void testParseWords() {

        String testString = "Hi This is a test, hope it goes well.";
        Set<String> expectedSet = new HashSet<>();

        expectedSet.add("Hi");
        expectedSet.add("This");
        expectedSet.add("is");
        expectedSet.add("a");
        expectedSet.add("test");
        expectedSet.add("hope");
        expectedSet.add("it");
        expectedSet.add("goes");
        expectedSet.add("well");


        Set<String> resultSet = parseWords(testString);

        Assertions.assertIterableEquals(expectedSet, resultSet);
    }

    @Test
    public void verifyAmazonSearch() {
        driver.get("https://www.amazon.com/");

        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));

        searchBox.click();
        searchBox.sendKeys("playstation 5");
        searchBox.submit();

        WebElement searchResults = driver.findElement(By.id("search"));

        String searchText = searchResults.getText();

        Set<String> searchSet = parseWords(searchText);


        Assertions.assertTrue(searchSet.contains("PlayStation"));
    }

    @Test
    public void verifyAddToCard() {
        driver.get("https://www.amazon.com/LEGO-Star-Wars-Millennium-Minifigures/dp/B07QQ396NH/ref=sr_1_3?crid=1A163B33LNAZW&keywords=star%2Bwars%2Blego&qid=1654347481&sprefix=star%2Bwars%2Blego%2Caps%2C62&sr=8-3&th=1/");

        WebElement addToCartButton = driver.findElement(By.id("add-to-cart-button"));

        addToCartButton.click();

        WebElement proceedCheckout = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"attach-sidesheet-checkout-button\"]/span/input")));

        proceedCheckout.click();

        driver.get("https://www.amazon.com/gp/cart/view.html?ref_=nav_cart");

        WebElement activeCart = driver.findElement(By.id("sc-active-cart"));

        Set<String> resultSet = parseWords(activeCart.getText());

        for (String s : resultSet) {
            System.out.println("current word: " + s);
        }

        Assertions.assertTrue(resultSet.contains("LEGO"));
        Assertions.assertTrue(resultSet.contains("Star"));
        Assertions.assertTrue(resultSet.contains("Wars:"));
        Assertions.assertTrue(resultSet.contains("Falcon"));
        Assertions.assertTrue(resultSet.contains("Millennium"));
        Assertions.assertTrue(resultSet.contains("75257"));

        //cheeky way of checking if the quantity is right
        Assertions.assertTrue(resultSet.contains("Qty:1"));

    }
}
