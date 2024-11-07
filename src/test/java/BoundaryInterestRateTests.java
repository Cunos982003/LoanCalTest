import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.nio.file.Paths;
import java.time.Duration;

public class BoundaryInterestRateTests {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // Initialize ChromeDriver
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Load the local HTML file
        String pathToHtml = Paths.get("src/test/resources/loan_calculation_system.html").toUri().toString();
        driver.get(pathToHtml);
    }

    @Test
    public void testInterestRateLowerBoundary() throws InterruptedException {
        clearAndEnterText(By.id("customerName"), "John");
        clearAndEnterText(By.id("accountNumber"), "123456");
        clearAndEnterText(By.id("interestRate"), "0"); // Below the limit

        driver.findElement(By.tagName("button")).click();
        Thread.sleep(1000); // Observe result

        WebElement result = driver.findElement(By.id("result"));
        Assertions.assertEquals("Error: Lãi suất không hợp lệ", result.getText());
    }

    @Test
    public void testInterestRateUpperBoundary() throws InterruptedException {
        clearAndEnterText(By.id("customerName"), "John");
        clearAndEnterText(By.id("accountNumber"), "123456");
        clearAndEnterText(By.id("interestRate"), "20"); // Exactly the limit

        driver.findElement(By.tagName("button")).click();
        Thread.sleep(1000); // Observe result

        WebElement result = driver.findElement(By.id("result"));
        Assertions.assertEquals("Success: Tất cả dữ liệu hợp lệ", result.getText());
    }

    @Test
    public void testInterestRateExceedsLimit() throws InterruptedException {
        clearAndEnterText(By.id("customerName"), "John");
        clearAndEnterText(By.id("accountNumber"), "123456");
        clearAndEnterText(By.id("interestRate"), "21"); // Exceeds the limit

        driver.findElement(By.tagName("button")).click();
        Thread.sleep(1000); // Observe result

        WebElement result = driver.findElement(By.id("result"));
        Assertions.assertEquals("Error: Lãi suất vượt quá giới hạn", result.getText());
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void clearAndEnterText(By locator, String text) throws InterruptedException {
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(text);
        Thread.sleep(1000); // Observe each field entry
    }
}
