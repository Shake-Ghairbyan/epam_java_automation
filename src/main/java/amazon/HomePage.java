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

    @FindBy(id = "deliveryTargetTextIDContent")
    WebElement deliveryTargetLocation;


    private static final String navBarBooksXpathContent = "//*[@value='search-alias=stripbooks-intl-ship']";
    @FindBy(xpath = navBarBooksXpathContent)
    WebElement books;


    private static final String navBarIDContent = "navbar";
    @FindBy(id = "navbar")
    WebElement navBar;


    @FindBy(xpath = "//*[@type='text']")
    WebElement searchTab;


    @FindBy(id = "nav-search-submit-button")
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
