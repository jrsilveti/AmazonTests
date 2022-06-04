import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashSet;
import java.util.Set;

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
            if(currentCharacter == ' ' || currentCharacter == ',' || currentCharacter == '.'){
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
}
