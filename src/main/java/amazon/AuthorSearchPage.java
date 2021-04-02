package amazon;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AuthorSearchPage {

    private WebDriver driver;

    private By searchResultsLoc = By.xpath("//*[@data-component-type='s-search-result']");
    private By countOfSearchResultsOnFirstPagLoc = By.xpath("//*[@class='a-section a-spacing-small a-spacing-top-small']/span");
    private By searchResultsUnderLineLoc = By.xpath("//h2[@class='a-size-mini a-spacing-none a-color-base s-line-clamp-2']/following-sibling::div");
    private By searchResultsAuthorLinksLoc = By.xpath("//h2[@class='a-size-mini a-spacing-none a-color-base s-line-clamp-2']/following-sibling::div/a");

    public AuthorSearchPage(WebDriver driver) {
        this.driver = driver;
    }


    public void waitUntilPageLoads() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(searchResultsLoc));
        wait.until(ExpectedConditions.numberOfElementsToBe(searchResultsLoc, countOfResultsOnFirstPage()));
    }

    private int parsStringToInt(String line) {
        String result = line.substring(2, 4);
        return Integer.parseInt(result);
    }

    public int countOfResultsOnFirstPage() {
        WebElement countOfSearchResultsOnFirstPage = driver.findElement(countOfSearchResultsOnFirstPagLoc);
        return parsStringToInt(countOfSearchResultsOnFirstPage.getText());
    }

    public int countOfSearchResultsContainingAuthor(String authorName) {
        List<WebElement> searchResultsUnderLine = driver.findElements(searchResultsUnderLineLoc);
        return (int) searchResultsUnderLine.stream()
                .filter(e -> e.getText().toLowerCase().contains(authorName))
                .count();
    }

    public WebElement getLinkWithTargetedAuthorName(String authorName) throws Exception {
        List<WebElement> searchResultsAuthorLinks = driver.findElements(searchResultsAuthorLinksLoc);
        for (WebElement e : searchResultsAuthorLinks) {
            if (e.getText().toLowerCase().equals(authorName)) {
                return e;
            }
        }
        throw new Exception("Author name in search results is not clickable.");
    }
}
