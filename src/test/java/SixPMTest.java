import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class SixPMTest {

    private WebDriver driver;
    private Actions actions;
    private WebDriverWait wait;
    private WebElement removedItem;
    String actualPrice;

    @BeforeSuite
    private void setSystemProperties() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
    }

    @BeforeMethod
    private void createDriverInstance() {
        driver = new ChromeDriver();
        actions = new Actions(driver);
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    private void test6PMMainFunctionalities() throws InterruptedException {
        driver.get("https://www.6pm.com/");
        driver.manage().window().maximize();

        By accessoriesButtonLoc = By.xpath("//*[text()=\"Accessories\"]");
        WebElement accessoriesButton = driver.findElement(accessoriesButtonLoc);
        wait.until(ExpectedConditions.elementToBeClickable(accessoriesButtonLoc));
        actions.moveToElement(accessoriesButton).perform();

        By aviatorLoc = By.xpath("//*[text()='Aviators']");
        WebElement aviatorButton = driver.findElement(aviatorLoc);
        wait.until(ExpectedConditions.elementToBeClickable(aviatorButton));

        actions.moveToElement(aviatorButton).click().build().perform();


        By searchPageLoc = By.xpath("//*[@itemtype='http://schema.org/Product']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchPageLoc));

        List<WebElement> aviators = driver.findElements(searchPageLoc);
        WebElement aviator = Utils.randomItem(aviators);

        actions.moveToElement(aviator).click().build().perform();

        By itemPageLoc = By.xpath("//*[@class='Rk-z']");
        wait.until(ExpectedConditions.elementToBeClickable(itemPageLoc));

        String expectedName = driver.findElement(By.xpath("//*[@itemprop='name' and @class='Sw-z']")).getAttribute("innerHTML");
        String expectedPrice = driver.findElement(By.xpath("//*[@class='Ao-z Fo-z']")).getAttribute("aria-label");

        WebElement addToShoppingBagButton = driver.findElement(By.xpath("//button[text()='Add to Shopping Bag']"));
        addToShoppingBagButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='BM-z']")));

        WebElement viewBagButton = driver.findElement(By.xpath("//*[@class='FM-z']"));
        actions.moveToElement(viewBagButton).click().build().perform();

        By itemInBagContainerLoc = By.xpath("//*[@class='Uq-z']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(itemInBagContainerLoc));

        String actualName = driver.findElement(By.xpath("//div[@class='ft-z']/a")).getText().replace('\n', ' ');

        actualPrice = driver.findElement(By.xpath("//em")).getText();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualName, expectedName, "Name of the item doesn't match the expected value.");
        softAssert.assertEquals(actualPrice, expectedPrice, "Price of the item doesn't match the expected value.");
        softAssert.assertAll();
    }

    @AfterMethod
    private void tearDownDriverInstance() throws Exception {
        removedItem = driver.findElement(By.xpath(String.format("//*[contains(text(), '%s')]", actualPrice)));

        WebElement quantityButton = driver.findElement(By.xpath("//*[@name='quantity']"));
        actions.moveToElement(quantityButton).click().build().perform();

        WebElement removeOption = driver.findElement(By.xpath("//*[text()='Remove']"));
        removeOption.click();

        wait.until(ExpectedConditions.invisibilityOf(removedItem));

        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
