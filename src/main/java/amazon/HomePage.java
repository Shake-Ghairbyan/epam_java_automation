package amazon;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private final WebDriver driver;

    private static final String deliveryTargetTextIDContent = "glow-ingress-block";
    @FindBy(id = deliveryTargetTextIDContent)
    WebElement deliveryTargetLocation;


    private static final String navBarBooksXpathContent = "//*[@value='search-alias=stripbooks-intl-ship']";
    @FindBy(xpath = navBarBooksXpathContent)
    WebElement books;


    private static final String navBarIDContent = "navbar";
    @FindBy(id = navBarIDContent)
    WebElement navBar;


    private static final String searchTabXpathContent = "//*[@type='text']";
    @FindBy(xpath = searchTabXpathContent)
    WebElement searchTab;


    private static final String searchButtonIDContent = "nav-search-submit-button";
    @FindBy(id = searchButtonIDContent)
    WebElement searchButton;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getDeliveryTargetCountry() {
        return deliveryTargetLocation.getText().replace('\n', ' ');
    }

    public void enterAuthorNameAndSearch(String fullName) {
        searchTab.sendKeys(fullName);
        searchTab.sendKeys(Keys.RETURN);

        searchButton.click();
    }

    public void clickBooksFromNavBar() {
        books.click();
    }

    public void waitUntilPageLoads() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(navBarIDContent)));
        wait.until(ExpectedConditions.visibilityOf(navBar));
    }
}
