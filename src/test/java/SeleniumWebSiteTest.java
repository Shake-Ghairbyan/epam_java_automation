import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;


public class SeleniumWebSiteTest {
    @Test
    public void testSeleniumWebDriver() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
        WebDriver driver = new ChromeDriver();
        try {

            driver.manage().window().fullscreen();
            driver.get("https://www.selenium.dev/");
            Thread.sleep(3000);
            driver.findElement(By.xpath("//a[contains(text(),'Downloads')]")).click();
            WebElement latestStableVersion = driver.findElement(By.xpath("//p[contains(text(), 'Latest stable version')]/a"));
            Assert.assertEquals(latestStableVersion.getText(), "3.141.59");
            WebElement searchField = driver.findElement(By.name("search"));
            searchField.sendKeys("selenium webdriver");
            searchField.sendKeys(Keys.ENTER);
            Thread.sleep(3000);
            List<WebElement> searchedWordsElems = driver.findElements(By.xpath("//div[@class='gs-webResult gs-result']/div/div/a"));
            long countOfReqLinks = searchedWordsElems.stream()
                    .filter(e -> e.getText().toLowerCase().contains("selenium webdriver"))
                    .count();
            Assert.assertTrue(countOfReqLinks > 0, "There is no link containing 'selenium webdriver' in it.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
