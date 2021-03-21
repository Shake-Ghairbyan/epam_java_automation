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

            driver.manage().window().maximize();
            driver.get("https://www.selenium.dev/");
            driver.findElement(By.xpath("//a[contains(text(),'Downloads')]")).click();
            WebElement latestStableVersion = driver.findElement(By.xpath("//p[contains(text(), 'Latest')]/a"));
            Assert.assertEquals(latestStableVersion.getText(), "3.141.59");
            WebElement searchField = driver.findElement(By.name("search"));
            searchField.sendKeys("selenium webdriver");
            searchField.sendKeys(Keys.ENTER);
            List<WebElement> searchedWordsElems = driver.findElements(By.xpath("//div[@class='gs-webResult gs-result']/div/div/a"));
            long countOfReqLinks = searchedWordsElems.stream()
                    .filter(e -> e.getText().toLowerCase().contains("selenium webdriver"))
                    .count();
            Assert.assertTrue(countOfReqLinks > 0);
        } finally {
            driver.close();
        }
    }
}
