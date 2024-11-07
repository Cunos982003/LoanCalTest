import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.nio.file.Paths;
import java.time.Duration;

public class LoanCalculationTests {
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
    public void testValidData() throws InterruptedException {
        clearAndEnterText(By.id("customerName"), "John");
        clearAndEnterText(By.id("accountNumber"), "123456");
        clearAndEnterText(By.id("loanAmount"), "2500");
        clearAndEnterText(By.id("loanTerm"), "3");
        clearAndEnterText(By.id("interestRate"), "10");

        // Click submit button
        driver.findElement(By.tagName("button")).click();
        Thread.sleep(1000);  // Delay to observe the result

        WebElement result = driver.findElement(By.id("result"));
        Assertions.assertEquals("Success: Tất cả dữ liệu hợp lệ", result.getText());
    }

    @Test
    public void testShortCustomerName() throws InterruptedException {
        clearAndEnterText(By.id("customerName"), "J");

        // Click submit button
        driver.findElement(By.tagName("button")).click();
        Thread.sleep(1000);  // Delay to observe the result

        WebElement result = driver.findElement(By.id("result"));
        Assertions.assertEquals("Error: Tên khách hàng quá ngắn", result.getText());
    }

    @Test
    public void testInvalidAccountNumber() throws InterruptedException {
        clearAndEnterText(By.id("customerName"), "John");
        clearAndEnterText(By.id("accountNumber"), "123"); // Invalid account number

        // Click submit button
        driver.findElement(By.tagName("button")).click();
        Thread.sleep(1000);  // Delay to observe the result

        WebElement result = driver.findElement(By.id("result"));
        Assertions.assertEquals("Error: Số tài khoản không hợp lệ", result.getText());
    }

    @Test
    public void testLoanAmountExceedingLimit() throws InterruptedException {
        clearAndEnterText(By.id("customerName"), "John");
        clearAndEnterText(By.id("accountNumber"), "123456");
        clearAndEnterText(By.id("loanAmount"), "1000001"); // Exceeds the limit

        // Click submit button
        driver.findElement(By.tagName("button")).click();
        Thread.sleep(1000);  // Delay to observe the result

        WebElement result = driver.findElement(By.id("result"));
        Assertions.assertEquals("Error: Số tiền vay không hợp lệ", result.getText());
    }

    @Test
    public void testInterestRateExceedingLimit() throws InterruptedException {
        clearAndEnterText(By.id("customerName"), "John");
        clearAndEnterText(By.id("accountNumber"), "123456");
        clearAndEnterText(By.id("interestRate"), "21"); // Exceeds the limit

        // Click submit button
        driver.findElement(By.tagName("button")).click();
        Thread.sleep(1000);  // Delay to observe the result

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
        Thread.sleep(1000);  // Delay to observe each field entry
    }
}
