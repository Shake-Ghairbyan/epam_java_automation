import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class SixPMTest {

    private WebDriver driver = null;

    @BeforeSuite
    private void setSystemProperties() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
    }

    @BeforeMethod
    private void createDriverInstance() {
        driver = new ChromeDriver();
    }

    @AfterMethod
    private void tearDownDriverInstance() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Test
    private void test6PMMainFunctionalities() throws InterruptedException {
        driver.get("https://www.6pm.com/");
        driver.manage().window().maximize() ;

        Actions actions = new Actions(driver);

        WebDriverWait wait = new WebDriverWait(driver, 10);

        By accessoriesButtonLoc = By.xpath("//*[text()=\"Accessories\"]");
        WebElement accessoriesButton = driver.findElement(accessoriesButtonLoc);
        wait.until(ExpectedConditions.elementToBeClickable(accessoriesButtonLoc));
        actions.moveToElement(accessoriesButton).perform();

        By aviatorLoc = By.xpath("//*[text()='Aviators']");
        WebElement aviatorButton = driver.findElement(aviatorLoc);
        wait.until(ExpectedConditions.elementToBeClickable(aviatorButton));

        actions.moveToElement(aviatorButton).click().build().perform();


        By searchPageLoc = By.xpath("//*[@class='py-z ns-z']");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchPageLoc));

        List<WebElement> aviators = driver.findElements(By.xpath("//*[@class='py-z ns-z']"));
        WebElement aviator = Utils.randomItem(aviators);

        actions.moveToElement(aviator).click().build().perform();

        By itemPageLoc = By.xpath("//*[@class='Gb-z']");
        wait.until(ExpectedConditions.elementToBeClickable(itemPageLoc));

        String expectedName = driver.findElement(By.xpath("//*[@class='fN-z']")).getAttribute("innerHTML");
        String expectedPrice = driver.findElement(By.xpath("//*[@class='tK-z yK-z']")).getAttribute("aria-label");

        WebElement addToShoppingBagButton = driver.findElement(By.xpath("//button[text()='Add to Shopping Bag']"));
        actions.moveToElement(addToShoppingBagButton).click().build().perform();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='oz-z']")));

        WebElement viewBagButton = driver.findElement(By.xpath("//*[@class='sz-z']"));
        actions.moveToElement(viewBagButton).click().build().perform();

        By itemInBagContainerLoc = By.xpath("//*[@class='pc-z']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(itemInBagContainerLoc));

        String actualName = driver.findElement(By.xpath("//div[@class='Wk-z']/a")).getText().replace('\n', ' ');

        String actualPrice = driver.findElement(By.xpath("//*[@class='yh-z Bh-z']")).getText();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualName, expectedName, "Name of the item doesn't match the expected value.");
        softAssert.assertEquals(actualPrice, expectedPrice, "Price of the item doesn't match the expected value.");
        softAssert.assertAll();

        WebElement quantityButton = driver.findElement(By.name("quantity"));
        actions.moveToElement(quantityButton).click().build().perform();

        WebElement removeOption = driver.findElement(By.xpath("//*[text()='Remove']"));
        removeOption.click();

        String textCheck = "Nothing to see here yet! Sign in to see items that you've previously placed in your Bag or check out all the awesome things you can buy on 6pm!";
        wait.until(ExpectedConditions.textToBe(By.xpath("//h1/following-sibling::p"), textCheck));
    }
}
