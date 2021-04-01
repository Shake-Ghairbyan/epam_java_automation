package amazon;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private WebDriver driver;

    private By deliveryTargetTextLoc = By.id("glow-ingress-block");
    private By navBarBooksLoc = By.xpath("//*[@value='search-alias=stripbooks-intl-ship']");
    private By navBarLoc = By.id("navbar");
    private By searchTabLoc = By.xpath("//*[@type='text']");
    private By searchButtonLoc = By.id("nav-search-submit-button");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public String getDeliveryTargetCountry() {
        WebElement deliveryTargetLocation = driver.findElement(deliveryTargetTextLoc);
        noSuchElement(deliveryTargetLocation);
        return deliveryTargetLocation.getText().replace('\n', ' ');
    }

    public void enterAuthorNameAndSearch(String fullName) {
        WebElement searchTab = driver.findElement(searchTabLoc);
        noSuchElement(searchTab);
        searchTab.sendKeys(fullName);
        searchTab.sendKeys(Keys.RETURN);

        WebElement searchButton = driver.findElement(searchButtonLoc);
        searchButton.click();
    }

    public void clickBooksFromNavBar() {
        WebElement books = driver.findElement(navBarBooksLoc);
        noSuchElement(books);
        books.click();
    }

    private void noSuchElement(WebElement element) {
        if (element == null) {
            throw new RuntimeException("ERROR: No such element");
        }
    }

    public void waitUntilPageLoads() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(navBarLoc));
        wait.until(ExpectedConditions.visibilityOfElementLocated(navBarLoc));
    }
}
