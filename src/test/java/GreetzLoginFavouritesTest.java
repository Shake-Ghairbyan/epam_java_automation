import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class GreetzLoginFavouritesTest {

    private WebDriver driver = null;

    private ChromeDriver createDriverInstance() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
        return new ChromeDriver();
    }

    private void testLoginImpl() throws InterruptedException {
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
        Assert.assertTrue(isLoggedIn("test"), "Failed to log in user " + email);
        Thread.sleep(5000);
    }

    public boolean isLoggedIn(String firstName) {
        try {
            driver.findElement(By.xpath("//div[@class='header-message']//span[text()='Welkom " + firstName + "']"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Test
    public void testGreetzLogin() throws InterruptedException {
        driver = createDriverInstance();
        try {
            testLoginImpl();
        } finally {
            driver.quit();
        }
    }

    @Test
    public void testBasketFunctionality() throws InterruptedException {
        driver = createDriverInstance();
        try {
            testLoginImpl();

            driver.get("https://www.greetz.nl/ballonnen/denken-aan");
            Thread.sleep(3000);

            WebElement chosenItem = driver.findElement(By.xpath("//div[text()=\"Ballon 'op fleuren'\"]/ancestor::a/following-sibling::a/div"));
            chosenItem.click();
            String expectedItemTitle = driver.findElement(By.xpath("//div[text()=\"Ballon 'op fleuren'\"]/ancestor::a")).getText();
            String expectedItemPrice = driver.findElement(By.xpath("//div[text()=\"Ballon 'op fleuren'\"]/ancestor::a//span/span")).getText();
            Thread.sleep(3000);

            WebElement myGreetz = driver.findElement(By.linkText("MyGreetz"));
            myGreetz.click();
            Thread.sleep(3000);

            WebElement favorieten = driver.findElement(By.xpath("//div[@class=\"hamburgermenu profilehamburgermenu hamburgermenu_visible\"]//span[text()='Favorieten']"));
            favorieten.click();
            Thread.sleep(3000);

            WebElement favItem = driver.findElement(By.xpath("//*[@class='favorite-item']"));
            favItem.click();
            Thread.sleep(3000);

            String actualItemTitle = driver.findElement(By.xpath("//*[@ng-bind=\"gift.title\"]")).getText();
            String actualItemPrice = driver.findElement(By.xpath("//*[@class=\"price-normal\"]")).getText();
            Assert.assertTrue(actualItemPrice.contains(expectedItemPrice), "Prices of basket item: " + actualItemPrice + " and of chosen item: " + expectedItemPrice + " are different.");
            Assert.assertTrue(actualItemTitle.contains(expectedItemTitle), "Names of basket item: " + actualItemTitle + " and chosen item: " + expectedItemTitle + "are different.");
        } finally {
            driver.quit();
        }
    }

    @Test
    public void testCardsPricingChanges() throws InterruptedException {
        driver = createDriverInstance();
        try {
            testLoginImpl();

            driver.get("https://www.greetz.nl/kaarten/denken-aan");
            Thread.sleep(3000);

            List<WebElement> cards = driver.findElements(By.xpath("//div[@class=\"b-card-preview__container\"]"));
            WebElement randomChosenItem = cards.get(getRandomNumberUsingNextInt(cards.size()));
            randomChosenItem.click();
            Thread.sleep(3000);
            WebElement randomChosenItemQuantity = driver.findElement(By.xpath("//input[@type='number']"));
            int chosenQuantity = getRandomNumberUsingNextInt(10) + 1;
            randomChosenItemQuantity.clear();
            randomChosenItemQuantity.sendKeys("" + chosenQuantity);
            Thread.sleep(2000);
            String price = driver.findElement(By.xpath("//*[@class='price-card price-block']/span/span")).getText();
            double priceOfItem = parsePriceStrToDouble(price);
            String expectedTotalPrice = "" + chosenQuantity * priceOfItem;
            String expectedTotalPriceFinal = expectedTotalPrice.replace('.', ',');
            String actualTotalPriceLine = driver.findElement(By.xpath("//div[@class='price-total']")).getText();
            Assert.assertTrue(actualTotalPriceLine.contains(expectedTotalPriceFinal), "Actual total price: " + actualTotalPriceLine + " and Expected price: " + expectedTotalPriceFinal + " are different.");
            Thread.sleep(2000);
        } finally {
            driver.quit();
        }
    }

    //not mine
    private int getRandomNumberUsingNextInt(double max) {
        Random random = new Random();
        int maxInt = (int) max;
        return random.nextInt(maxInt - 0) + 0;
    }

    private double parsePriceStrToDouble(String s) {
        String subSt = s.substring(2);
        String result = subSt.replace(',', '.');
        return Double.parseDouble(result);
    }
}
