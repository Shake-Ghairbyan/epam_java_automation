package amazon;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorPage {

    private WebDriver driver;

    private By booksBySectionLoc = By.id("booksBySection");
    private By priceOfBookDivLoc = By.xpath("//*[@class='a-fixed-left-grid a-spacing-small']");
    private By priceOfBookLoc = By.xpath("//span[@class='a-size-base-plus a-color-price a-text-bold']/span");
    private By sortBySelectorsLoc = By.id("sortBySelectors");
    private By sortByPriceLowToHighButtonLoc = By.xpath("//a[contains(text(),'Price: Low to High')]");


    public AuthorPage(WebDriver driver) {
        this.driver = driver;
    }

    public void waitUntilPageLoads() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(priceOfBookDivLoc));
        wait.until(ExpectedConditions.numberOfElementsToBe(priceOfBookDivLoc, 12));
    }

    public String booksByAuthorText() {
        WebElement booksByAuthor = driver.findElement(booksBySectionLoc);
        return booksByAuthor.getText().toLowerCase();
    }

    public void sortByPriceLowToHigh() {
        WebElement sortBySelectorsButton = driver.findElement(sortBySelectorsLoc);
        sortBySelectorsButton.click();

        WebElement sortByPriceLowToHighButton = driver.findElement(sortByPriceLowToHighButtonLoc);
        sortByPriceLowToHighButton.click();
    }

    public List<Double> getPricesOfBooks() {
        List<WebElement> pricesOfBooks = driver.findElements(priceOfBookLoc);
        return pricesOfBooks.stream()
                .map(priceStr -> parseStringToDouble(priceStr.getText()))
                .collect(Collectors.toList());
    }

    private double parseStringToDouble(String price) {
        String result = price.substring(1);
        return Double.parseDouble(result);
    }
}
