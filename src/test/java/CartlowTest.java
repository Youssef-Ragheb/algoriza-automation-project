import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.time.Duration;

public class CartlowTest {

    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;
    Actions actions;
    ExtentReports report;
    ExtentTest test;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        actions = new Actions(driver);
        ExtentSparkReporter spark = new ExtentSparkReporter("CartlowReport.html");
        report = new ExtentReports();
        report.attachReporter(spark);
    }

    @Test(priority = 1)
    public void testLogin() throws InterruptedException {
        driver.get("https://cartlow.com/customer/login");
        test = report.createTest("testLogin");
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("identifier")));
        emailField.sendKeys("mario.sherif25@gmail.com");

        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitBtn.click();


        WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        password.sendKeys("@Password1");

        WebElement submitBtnForPassword = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitBtnForPassword.click();

        WebElement accountButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@role='button' and text()='Account']")));
        actions.moveToElement(accountButton).perform();
        Thread.sleep(1000);
        WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='font-medium' and normalize-space()='Youssef Sherif']")));

        Assert.assertTrue(usernameElement.isDisplayed(), "Login failed");
    }

    @Test(priority = 2)
    public void testRegionChanges() {
        WebElement intlButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[.//span[text()='INTL']]")));
        intlButton.click();
        test = report.createTest("testRegionChanges");
        WebElement uaeOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'UAE')]")));
        uaeOption.click();
        wait.until(ExpectedConditions.urlContains("/uae/en"));
    }

    @Test(priority = 3)
    public void testLaptopIsFound() throws InterruptedException {
        WebElement laptopsLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Laptops")));
        laptopsLink.click();
        test = report.createTest("testLaptopIsFound");
        WebElement searchBox = driver.findElement(By.name("query"));
        searchBox.sendKeys("Dell Latitude 7490 Intel Core i7-8650U");
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("div.icon-search")).click();

        String laptopXPath = "//img[contains(@src,'10073320002')]";
        selectItem(laptopXPath);
        WebElement productName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class, 'pdp-variant-name')]")));
        Assert.assertTrue(productName.getText().contains("Dell Latitude 7490 Intel Core i7-8650U 14"), "Wrong Item");

    }

    @Test(priority = 4)
    public void testAddLaptopToCart() throws InterruptedException {
        js.executeScript("window.scrollTo(0, 700);");
        Thread.sleep(2000);
        test = report.createTest("testAddLaptopToCart");
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Add To Cart']")));
        addToCartBtn.click();

        Thread.sleep(2000);
        js.executeScript("window.scrollTo(0, -800);");
        Thread.sleep(1000);
        WebElement cartBadge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class,'bg-navyBlue') and contains(@class,'font-semibold')]")));
        String actualCount = cartBadge.getText().trim();
        String expectedCount = "1";
        Thread.sleep(1000);
        Assert.assertEquals(actualCount, expectedCount, "Item didnt add to cart");
    }

    @Test(priority = 5)
    public void testWatchIsFound() throws InterruptedException {
        test = report.createTest("testWatchIsFound");
        WebElement smartwatchesLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li/a[@title='Smartwatches']")));
        smartwatchesLink.click();
        WebElement searchBox = driver.findElement(By.name("query"));
        searchBox.sendKeys("Apple Watch Series 6 (40mm, GPS + Cellular)");
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("div.icon-search")).click();

        String smartwatchXPath = "//img[contains(@src,'10001470184')]";
        selectItem(smartwatchXPath);

        WebElement productName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class,'pdp-variant-name')]")));

        String actualName = productName.getText().trim();
        String expectedName = "Apple Watch Series 6 (40mm, GPS + Cellular) Gold Aluminum Case with Pink Sand Sport Band";

        Assert.assertEquals(actualName, expectedName, "Product is not found");

        js.executeScript("window.scrollTo(0, 450);");
        Thread.sleep(1500);
        WebElement silverOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@title='Silver']")));
        silverOption.click();
    }

    @Test(priority = 6)
    public void testAddWatchToCart() throws InterruptedException {
        test = report.createTest("testAddWatchToCart");
        WebElement addToCartBtnForWatch = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Add To Cart')]")));
        addToCartBtnForWatch.click();

        Thread.sleep(2000);
        js.executeScript("window.scrollTo(0, -450);");
        Thread.sleep(2000);

        WebElement cartBadge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class,'bg-navyBlue') and contains(@class,'font-semibold')]")));
        String actualCount = cartBadge.getText().trim();
        String expectedCount = "2";

        Assert.assertEquals(actualCount, expectedCount, "Cart item count mismatch — expected [" + expectedCount + "] but found [" + actualCount + "]");
    }

    @Test(priority = 7)
    public void testItemsAreAddedToCart() throws InterruptedException {
        test = report.createTest("testItemsAreAddedToCart");
        Thread.sleep(3000);
        WebElement cartIcon = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("span.icon-cart")));
        cartIcon.click();
        Thread.sleep(2000);
        WebElement firstItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Apple Watch Series 6 (GPS + Cellular, 44mm) - Silver Aluminium Case with White Sport Band')]")));

        WebElement secondItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Dell Latitude 7490 Intel Core i7-8650U 14\" FHD Display, 16GB RAM 512GB SSD, Windows 10 Pro')]")));

        Assert.assertTrue(firstItem.isDisplayed(), "Apple Watch is not found in the cart!");
        Assert.assertTrue(secondItem.isDisplayed(), "Dell Latitude 7490 is not found in the cart!");
    }

    @Test(priority = 8)
    public void testRemoveItemFromCart() throws InterruptedException {
        test = report.createTest("testRemoveItemFromCart");
        WebElement removeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@title='Remove']")));
        removeBtn.click();

        WebElement agreeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Agree']")));
        agreeBtn.click();
        Thread.sleep(2000);

        WebElement closeCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("span.icon-cancel.cursor-pointer.text-3xl.max-md\\:text-2xl")));
        Thread.sleep(3000);
        closeCartBtn.click();
        Thread.sleep(3000);
        WebElement cartBadge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class,'bg-navyBlue') and contains(@class,'font-semibold')]")));
        String actualCount = cartBadge.getText().trim();
        String expectedCount = "1";

        Assert.assertEquals(actualCount, expectedCount, "Laptop didn't remove");
        WebElement cartIcon = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("span.icon-cart")));
        cartIcon.click();
        Thread.sleep(1000);

    }

    @Test(priority = 9)
    public void testAddToCartAndCheckout() throws InterruptedException {
        test = report.createTest("testAddToCartAndCheckout");
        WebElement checkoutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()=' Continue to Checkout ']")));
        checkoutBtn.click();
        Thread.sleep(4000);
        Assert.assertTrue(driver.getCurrentUrl().contains("/checkout/onepage"), "checkout page didnt open");
    }

    private void selectItem(String xpath) {
        int loadMoreTimes = 0;
        while (true) {
            try {
                WebElement item = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
                item.click();
                break;
            } catch (Exception e) {
                js.executeScript("window.scrollBy(0,350);");
            }
            try {
                Thread.sleep(1000);
                WebElement loadMoreBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()=' Load More ']")));
                loadMoreBtn.click();
                loadMoreTimes++;
            } catch (Exception e) {
                if (loadMoreTimes > 15) {
                    System.out.println("Item not found");
                    break;
                }
            }
        }
    }

    @AfterClass
    public void teardown() {
        driver.quit();
        report.flush();
    }
}