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

public class AuthorSearchPage {

    private final WebDriver driver;

    private static final String searchResultsXpathContent = "//*[@data-component-type='s-search-result']";
    @FindBy(xpath = searchResultsXpathContent)
    WebElement searchResults;


    private static final String countOfSearchResultsOnFirstPagXpathContent = "//*[@class='a-section a-spacing-small a-spacing-top-small']/span";
    @FindBy(xpath = countOfSearchResultsOnFirstPagXpathContent)
    WebElement countOfSearchResultsOnFirstPage;


    private static final String searchResultsUnderLineXpathContent = "//h2[@class='a-size-mini a-spacing-none a-color-base s-line-clamp-2']/following-sibling::div";
    @FindBys({
            @FindBy(xpath = searchResultsUnderLineXpathContent)
    })
    List<WebElement> searchResultsUnderLine;


    private static final String searchResultsAuthorLinksXpathContent = "//h2[@class='a-size-mini a-spacing-none a-color-base s-line-clamp-2']/following-sibling::div/a";
    @FindBys({
            @FindBy(xpath = searchResultsAuthorLinksXpathContent)
    })
    List<WebElement> searchResultsAuthorLinks;


    public AuthorSearchPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void waitUntilPageLoads() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(searchResultsXpathContent), countOfResultsOnFirstPage()));
    }

    private int parsStringToInt(String line) {
        String result = line.substring(2, 4);
        return Integer.parseInt(result);
    }

    public int countOfResultsOnFirstPage() {
        return parsStringToInt(countOfSearchResultsOnFirstPage.getText());
    }

    public int countOfSearchResultsContainingAuthor(String authorName) {
        return (int) searchResultsUnderLine.stream()
                .filter(e -> e.getText().toLowerCase().contains(authorName))
                .count();
    }

    public WebElement getLinkWithTargetedAuthorName(String authorName) throws Exception {
        for (WebElement e : searchResultsAuthorLinks) {
            if (e.getText().toLowerCase().equals(authorName)) {
                return e;
            }
        }
        throw new Exception("Author name in search results is not clickable.");
    }
}
