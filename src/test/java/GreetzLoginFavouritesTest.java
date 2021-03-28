import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class GreetzLoginFavouritesTest {

    private WebDriver driver = null;


    @BeforeSuite
    private void setSystemProperties() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
    }

    @BeforeMethod
    private void createDriverInstance() throws InterruptedException {
        driver = new ChromeDriver();

        driver.get("https://www.greetz.nl/auth/login");
        Thread.sleep(4000);

        WebElement loginFormElem = driver.findElement(By.id("loginForm"));
        String email = "test_testing@greetz.com";
        String pass = "testgreetz94";
        loginFormElem.findElement(By.name("email")).sendKeys(email);
        loginFormElem.findElement(By.name("password")).sendKeys(pass);
        Thread.sleep(3000);
        driver.findElement(By.id("login-cta")).click();
        Thread.sleep(5000);
        Assert.assertTrue(isLoggedIn("test"), String.format("Failed to log in user %s", email));
    }

    @AfterMethod
    private void tearDownDriverInstance() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }


    public boolean isLoggedIn(String firstName) {
        try {
            driver.findElement(By.xpath(String.format("//div[@class='header-message']//span[text()='Welkom %s']", firstName)));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }


    @Test
    public void testFavouritesFunctionality() throws InterruptedException {

        driver.get("https://www.greetz.nl/ballonnen/denken-aan");
        Thread.sleep(6000);

        List<WebElement> balloons = driver.findElements(By.xpath("//*[@class='b-products-grid__item']"));
        WebElement chosenBalloon = randomItem(balloons);
        WebElement starButton = chosenBalloon.findElement(By.xpath(".//a[2]/div"));
        if (starButton.getAttribute("class").equals("b-favourite b-favourite_selected")) {
            starButton.click();
            Thread.sleep(3000);
        }

        starButton.click();

        String expectedItemTitle = chosenBalloon.findElement(By.xpath(".//div[2]")).getText();
        String expectedItemPrice = chosenBalloon.findElement(By.xpath(".//div[3]")).getText();
        Thread.sleep(3000);

        WebElement myGreetz = driver.findElement(By.linkText("MyGreetz"));
        myGreetz.click();
        Thread.sleep(3000);

        WebElement favoriteItemsButton = driver.findElement(By.xpath("//span[text()='Favorieten']"));
        favoriteItemsButton.click();
        Thread.sleep(3000);

        WebElement favItem = driver.findElement(By.xpath("//*[@class='favorite-item']"));
        favItem.click();
        Thread.sleep(3000);

        String actualItemTitle = driver.findElement(By.xpath("//*[@ng-bind='gift.title']")).getText();
        String actualItemPrice = driver.findElement(By.xpath("//*[@class='price-normal']")).getText().substring(2);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualItemPrice, expectedItemPrice, "Price of the item doesn't match the expected value.");
        softAssert.assertEquals(actualItemTitle, expectedItemTitle, "Name of the item doesn't match the expected value.");
        softAssert.assertAll();
    }

    @Test(priority = 1)
    public void testCardsPricingChange() throws InterruptedException {

        driver.get("https://www.greetz.nl/kaarten/denken-aan");
        Thread.sleep(3000);

        List<WebElement> cards = driver.findElements(By.xpath("//div[@class=\"b-card-preview__container\"]"));
        WebElement randomChosenItem = randomItem(cards);
        randomChosenItem.click();
        Thread.sleep(3000);

        WebElement randomChosenItemQuantity = driver.findElement(By.xpath("//input[@type='number']"));
        int chosenQuantity = getRandomNumber(10) + 1;
        randomChosenItemQuantity.clear();
        randomChosenItemQuantity.sendKeys("" + chosenQuantity);
        Thread.sleep(2000);

        String price = driver.findElement(By.xpath("//*[@class='price-normal']")).getText();
        double priceOfItem = parsePriceStrToDouble(price);

        double expectedTotalPrice = chosenQuantity * priceOfItem;

        String actualTotalPriceLine = driver.findElement(By.xpath("//div[@class='price-total']")).getText();
        double actualTotalPrice = Double.parseDouble(actualTotalPriceLine.split(" ")[2].replace(',', '.'));

        Assert.assertEquals(actualTotalPrice, expectedTotalPrice, 0.001, "Total price of the item doesn't match the expected value.");
        Thread.sleep(2000);
    }

    //not original
    private int getRandomNumber(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    private WebElement randomItem(List<WebElement> list) {
        return list.get(getRandomNumber(list.size()));
    }

    private double parsePriceStrToDouble(String s) {
        String subSt = s.substring(2);
        String result = subSt.replace(',', '.');
        return Double.parseDouble(result);
    }
}
