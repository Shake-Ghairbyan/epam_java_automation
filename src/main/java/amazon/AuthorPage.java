package amazon;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorPage {

    private final WebDriver driver;

    private static final String booksBySectionIDContent = "booksBySection";
    @FindBy(id = booksBySectionIDContent)
    WebElement booksByAuthor;

    private static final String priceOfBookDivXpathContent = "//*[@class='a-fixed-left-grid a-spacing-small']";
    @FindBy(xpath = priceOfBookDivXpathContent)
    WebElement priceOfBookDiv;

    private static final String pricesOfBookXpathContent = "//span[@class='a-size-base-plus a-color-price a-text-bold']/span";
    @FindBys({
            @FindBy(xpath = pricesOfBookXpathContent)
    })
    List<WebElement> pricesOfBooks;

    private static final String sortBySelectorsIDContent = "sortBySelectors";
    @FindBy(id = sortBySelectorsIDContent)
    WebElement sortBySelectorsButton;

    private static final String sortByPriceLowToHighButtonXpathContent = "//a[contains(text(),'Price: Low to High')]";
    @FindBy(xpath = sortByPriceLowToHighButtonXpathContent)
    WebElement sortByPriceLowToHighButton;


    public AuthorPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void waitUntilPageLoads() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfAllElements(priceOfBookDiv));
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(priceOfBookDivXpathContent), 12));
    }

    public String booksByAuthorText() {
        return booksByAuthor.getText().toLowerCase();
    }

    public void sortByPriceLowToHigh() {

        sortBySelectorsButton.click();

        sortByPriceLowToHighButton.click();
    }

    public List<Double> getPricesOfBooks() {
        return pricesOfBooks.stream()
                .map(priceStr -> parseStringToDouble(priceStr.getText()))
                .collect(Collectors.toList());
    }

    private double parseStringToDouble(String price) {
        String result = price.substring(1);
        return Double.parseDouble(result);
    }
}
